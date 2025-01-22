#!/bin/bash

# Habilita modo estrito
set -euo pipefail

# Configurações
readonly NAMESPACE="fastfood"
readonly MIN_MEMORY="4096"
readonly MIN_CPU="2"
readonly K8S_VERSION="v1.29.0"
readonly STORAGE_CLASS="standard"

readonly APP_NAME="uaifood"
readonly APP_VERSION="1.0.0"
readonly MYSQL_VERSION="8.0.35"
readonly TIMEOUT=300

# Estrutura de diretórios
readonly BASE_DIR="k8s"
readonly CONFIGMAP_DIR="$BASE_DIR/configmap"
readonly AUTOSCALING_DIR="$BASE_DIR/autoscaling"
readonly DEPLOYMENTS_DIR="$BASE_DIR/base/deployments"
readonly SERVICES_DIR="$BASE_DIR/base/services"
readonly STORAGE_DIR="$BASE_DIR/base/storage"

# Definição de cores
readonly VERMELHO='\033[0;31m'
readonly VERDE='\033[0;32m'
readonly AMARELO='\033[1;33m'
readonly AZUL='\033[0;34m'
readonly SEM_COR='\033[0m'

# Funções de log
log() { echo -e "${AZUL}[$(date +'%Y-%m-%d %H:%M:%S')] $1${SEM_COR}"; }
log_sucesso() { echo -e "${VERDE}✓ $1${SEM_COR}"; }
log_erro() { echo -e "${VERMELHO}✗ $1${SEM_COR}" >&2; }
log_aviso() { echo -e "${AMARELO}! $1${SEM_COR}"; }

# Tratamento de erros
tratar_erro() {
    local codigo_saida=$1
    local numero_linha=$2
    log_erro "Erro no script na linha $numero_linha"
    coletar_info_debug
    exit $codigo_saida
}

trap 'tratar_erro $? $LINENO' ERR

esperar_storage_provisioner() {
    log "Aguardando pod do storage-provisioner..."
    local tentativas=0
    local max_tentativas=30
    local tempo_espera=10

    while [[ $tentativas -lt $max_tentativas ]]; do
        # Verifica se o addon está habilitado
        if ! minikube addons list | grep "storage-provisioner" | grep "enabled" &>/dev/null; then
            log_aviso "Storage-provisioner não está habilitado. Habilitando..."
            minikube addons enable storage-provisioner
            sleep 5
        fi

        # Verifica se o pod existe e está rodando
        if kubectl get pods -n kube-system | grep "storage-provisioner" | grep "Running" &>/dev/null; then
            log_sucesso "Storage provisioner está rodando"
            return 0
        fi

        log_aviso "Aguardando storage-provisioner iniciar (Tentativa $((tentativas + 1))/$max_tentativas)"
        ((tentativas++))
        sleep $tempo_espera
    done

    log_erro "Falha ao iniciar storage-provisioner"
    return 1
}

verificar_requisitos() {
    log "Verificando requisitos..."
    local comandos=("docker" "minikube" "kubectl")

    for cmd in "${comandos[@]}"; do
        if ! command -v "$cmd" &> /dev/null; then
            log_erro "Comando necessário não encontrado: $cmd"
            exit 1
        fi
    done

    log_sucesso "Todos os requisitos satisfeitos"
}

inicializar_minikube() {
    log "Inicializando Minikube..."

    # Para e remove cluster existente
    if minikube status &>/dev/null; then
        log "Parando cluster Minikube existente..."
        minikube stop || true
        sleep 5
        minikube delete || true
        sleep 5
    fi

    # Inicia novo cluster com configurações específicas
    log "Iniciando novo cluster Minikube..."
    minikube start \
        --driver=docker \
        --kubernetes-version=$K8S_VERSION \
        --memory=$MIN_MEMORY \
        --cpus=$MIN_CPU \
        --disk-size=20g \
        --bootstrapper=kubeadm \
        --extra-config=kubelet.cgroup-driver=systemd \
        --extra-config=apiserver.enable-admission-plugins="StorageObjectInUseProtection,DefaultStorageClass" \
        --wait=all

    # Habilita addons necessários
    log "Habilitando addons..."
    minikube addons enable storage-provisioner
    minikube addons enable default-storageclass
    minikube addons enable metrics-server

    # Aguarda node estar pronto
    log "Aguardando node estar pronto..."
    kubectl wait --for=condition=ready node/minikube --timeout=120s

    log_sucesso "Minikube inicializado com sucesso"
}

configurar_storage() {
    log "Configurando storage provisioner..."

    # Reinicia o addon
    log "Reiniciando storage provisioner..."
    minikube addons disable storage-provisioner
    sleep 5
    minikube addons enable storage-provisioner
    sleep 5

    # Verifica a configuração
    if ! verificar_storage; then
        log_erro "Falha na configuração do storage"
        log "Coletando informações de diagnóstico..."

        echo "Status dos addons:"
        minikube addons list | grep storage

        echo "Eventos do kube-system:"
        kubectl get events -n kube-system

        echo "Pods no kube-system:"
        kubectl get pods -n kube-system

        return 1
    fi

    log_sucesso "Storage configurado com sucesso"
    return 0
}

verificar_storage() {
    log "Verificando configuração de storage..."
    local max_tentativas=30
    local tempo_espera=10
    local tentativas=0

    while [[ $tentativas -lt $max_tentativas ]]; do
        # Primeiro verifica se o storage-provisioner está habilitado
        if ! minikube addons list | grep "storage-provisioner" | grep "enabled" &>/dev/null; then
            log_aviso "Storage provisioner não está habilitado. Habilitando..."
            minikube addons enable storage-provisioner
            sleep 5
            ((tentativas++))
            continue
        fi

        # Aguarda o pod ser criado e ficar Running
        if kubectl get pods -n kube-system -l k8s-app=storage-provisioner --no-headers 2>/dev/null | grep -q "Running"; then
            log_sucesso "Storage provisioner está rodando"

            # Verifica storage class
            if ! kubectl get storageclass standard &>/dev/null; then
                log_erro "Storage class padrão não encontrada"
                return 1
            fi

            # Verifica se é a classe padrão
            if ! kubectl get storageclass standard -o jsonpath='{.metadata.annotations.storageclass\.kubernetes\.io/is-default-class}' 2>/dev/null | grep -q "true"; then
                log_aviso "Configurando storage class como padrão..."
                kubectl patch storageclass standard -p '{"metadata": {"annotations":{"storageclass.kubernetes.io/is-default-class":"true"}}}'
            fi

            log_sucesso "Verificação de storage concluída com sucesso"
            return 0
        fi

        log_aviso "Aguardando storage provisioner iniciar (Tentativa $((tentativas + 1))/$max_tentativas)"
        kubectl get pods -n kube-system -l k8s-app=storage-provisioner
        sleep $tempo_espera
        ((tentativas++))
    done

    log_erro "Timeout aguardando storage provisioner"
    kubectl describe pods -n kube-system -l k8s-app=storage-provisioner
    return 1
}

implantar_aplicacao() {
    log "Iniciando implantação da aplicação..."

    # Cria namespace
    kubectl create namespace $NAMESPACE --dry-run=client -o yaml | kubectl apply -f -

    # Implanta componentes em ordem
    log "Implantando storage..."
    kubectl apply -f "$STORAGE_DIR/" -n $NAMESPACE

    # Aguarda PVC com retry
    local pvc_retries=0
    local max_pvc_retries=30
    local pvc_sleep=10

    while [[ $pvc_retries -lt $max_pvc_retries ]]; do
        if kubectl get pvc mysql-pvc -n $NAMESPACE -o jsonpath='{.status.phase}' | grep -q "Bound"; then
            log_sucesso "PVC mysql-pvc vinculado com sucesso"
            break
        fi
        log_aviso "Aguardando PVC ser vinculado (Tentativa $((pvc_retries + 1))/$max_pvc_retries)"
        ((pvc_retries++))
        sleep $pvc_sleep
    done

    log "Implantando ConfigMaps e Secrets..."
    kubectl apply -f "$CONFIGMAP_DIR/" -n $NAMESPACE

    log "Implantando Serviços..."
    kubectl apply -f "$SERVICES_DIR/" -n $NAMESPACE

    log "Implantando MySQL..."
    kubectl apply -f "$DEPLOYMENTS_DIR/mysql-deployment.yaml" -n $NAMESPACE
    kubectl wait --for=condition=available deployment/mysql -n $NAMESPACE --timeout=$TIMEOUT

    log "Implantando Aplicação..."
    kubectl apply -f "$DEPLOYMENTS_DIR/uaifood-deployment.yaml" -n $NAMESPACE
    kubectl wait --for=condition=available deployment/fastfood-api -n $NAMESPACE --timeout=$TIMEOUT

    log "Configurando Autoscaling..."
    kubectl apply -f "$AUTOSCALING_DIR/" -n $NAMESPACE

    log_sucesso "Implantação da aplicação concluída"
}

verificar_implantacao() {
    log "Verificando implantação..."

    # Verifica deployments
    kubectl get deployments -n $NAMESPACE

    # Verifica serviços
    kubectl get services -n $NAMESPACE

    # Verifica pods
    kubectl get pods -n $NAMESPACE

    # Obtém URL da aplicação
    local url_servico=$(minikube service fastfood-api-service -n $NAMESPACE --url)
    log_sucesso "Aplicação disponível em: $url_servico"
}

coletar_info_debug() {
    log "Coletando informações de debug..."

    echo -e "\nStatus do Minikube:"
    minikube status || true

    echo -e "\nLogs do Minikube:"
    minikube logs | grep -i "storage" || true

    echo -e "\nAddons do Minikube:"
    minikube addons list | grep -i "storage" || true

    echo -e "\nPods no kube-system:"
    kubectl get pods -n kube-system || true

    echo -e "\nEventos do kube-system:"
    kubectl get events -n kube-system | grep -i "storage" || true

    echo -e "\nStorage Classes:"
    kubectl get storageclass || true

    echo -e "\nRecursos do Namespace:"
    kubectl get all -n $NAMESPACE || true
}

main() {
    log "Iniciando processo de implantação..."

    verificar_requisitos
    inicializar_minikube
    configurar_storage
    verificar_storage
    implantar_aplicacao
    verificar_implantacao

    log_sucesso "Implantação concluída com sucesso!"

    # Exibe status final
    echo -e "\nClasses de Storage:"
    kubectl get storageclass

    echo -e "\nStatus do Storage Provisioner:"
    kubectl get pods -n kube-system -l k8s-app=storage-provisioner
}

# Executa função principal
main