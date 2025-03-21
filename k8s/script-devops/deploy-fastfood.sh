#!/bin/bash

# Cores para melhor visualização
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}=== Script de Implantação do FastFood no Minikube ===${NC}"

# Verificar se o Minikube está instalado
if ! command -v minikube &> /dev/null; then
    echo -e "${RED}Minikube não encontrado. Por favor, instale o Minikube primeiro.${NC}"
    exit 1
fi

# Verificar se o kubectl está instalado
if ! command -v kubectl &> /dev/null; then
    echo -e "${RED}kubectl não encontrado. Por favor, instale o kubectl primeiro.${NC}"
    exit 1
fi

# Criar diretório temporário para os arquivos YAML
TEMP_DIR=$(mktemp -d)
echo -e "${YELLOW}Criando arquivos YAML no diretório temporário: $TEMP_DIR${NC}"

# Função para criar os arquivos YAML
create_yaml_files() {
    # ConfigMap
    cat > "${TEMP_DIR}/configmap.yaml" << 'EOF'
apiVersion: v1
kind: ConfigMap
metadata:
  name: fastfood-config
  labels:
    app: fastfood
data:
  SPRING_PROFILES_ACTIVE: "dev"
  SPRING_DATASOURCE_URL: "jdbc:mysql://mysql-svc:3306/fastfood_db?useSSL=false&allowPublicKeyRetrieval=true"
  SPRING_JPA_HIBERNATE_DDL_AUTO: "update"
  DATABASE_DIALECT: "org.hibernate.dialect.MySQLDialect"
  SERVER_PORT: "8080"
  TZ: "America/Sao_Paulo"
EOF

    # Secret
    cat > "${TEMP_DIR}/secret.yaml" << 'EOF'
apiVersion: v1
kind: Secret
metadata:
  name: fastfood-secrets
  labels:
    app: fastfood
type: Opaque
data:
  MYSQL_ROOT_PASSWORD: cm9vdHBhc3N3b3Jk
  MYSQL_DATABASE: ZmFzdGZvb2RfZGI=
  MYSQL_USER: ZmFzdGZvb2R1c2Vy
  MYSQL_PASSWORD: ZmFzdGZvb2RwYXNz
  MERCADO_PAGO_KEY: WU9VUl9NRVJDQURPUEFHT19LRVk=
  SPRING_DATASOURCE_USERNAME: ZmFzdGZvb2R1c2Vy
  SPRING_DATASOURCE_PASSWORD: ZmFzdGZvb2RwYXNz
EOF

    # MySQL PVC
    cat > "${TEMP_DIR}/mysql-pvc.yaml" << 'EOF'
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: mysql-pvc
  labels:
    app: mysql
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 1Gi
EOF

    # MySQL Deployment
    cat > "${TEMP_DIR}/mysql-deployment.yaml" << 'EOF'
apiVersion: apps/v1
kind: Deployment
metadata:
  name: mysql
  labels:
    app: mysql
spec:
  replicas: 1
  selector:
    matchLabels:
      app: mysql
  template:
    metadata:
      labels:
        app: mysql
    spec:
      containers:
        - name: mysql
          image: mysql:8.0.35
          imagePullPolicy: IfNotPresent
          ports:
            - containerPort: 3306
          resources:
            requests:
              cpu: "250m"
              memory: "512Mi"
            limits:
              cpu: "500m"
              memory: "1Gi"
          envFrom:
            - secretRef:
                name: fastfood-secrets
          volumeMounts:
            - name: mysql-persistent-storage
              mountPath: /var/lib/mysql
      volumes:
        - name: mysql-persistent-storage
          persistentVolumeClaim:
            claimName: mysql-pvc
EOF

    # MySQL Service
    cat > "${TEMP_DIR}/mysql-service.yaml" << 'EOF'
apiVersion: v1
kind: Service
metadata:
  name: mysql-svc
  labels:
    app: mysql
spec:
  type: ClusterIP
  selector:
    app: mysql
  ports:
    - port: 3306
      targetPort: 3306
      protocol: TCP
EOF

    # FastFood API Deployment
    cat > "${TEMP_DIR}/uaifood-deployment.yaml" << 'EOF'
apiVersion: apps/v1
kind: Deployment
metadata:
  name: fastfood-api
  labels:
    app: fastfood-api
spec:
  replicas: 2
  selector:
    matchLabels:
      app: fastfood-api
  template:
    metadata:
      labels:
        app: fastfood-api
    spec:
      containers:
        - name: fastfood-api
          image: devops3ng1nn3r/uaifood:1.0.0
          imagePullPolicy: IfNotPresent
          ports:
            - containerPort: 8080
          resources:
            requests:
              cpu: "100m"
              memory: "256Mi"
            limits:
              cpu: "300m"
              memory: "512Mi"
          envFrom:
            - configMapRef:
                name: fastfood-config
            - secretRef:
                name: fastfood-secrets
          livenessProbe:
            httpGet:
              path: /actuator/health/liveness
              port: 8080
            initialDelaySeconds: 60
            periodSeconds: 10
            failureThreshold: 3
          readinessProbe:
            httpGet:
              path: /actuator/health/readiness
              port: 8080
            initialDelaySeconds: 40
            periodSeconds: 10
            failureThreshold: 3
          startupProbe:
            httpGet:
              path: /actuator/health
              port: 8080
            failureThreshold: 30
            periodSeconds: 10
EOF

    # API Service
    cat > "${TEMP_DIR}/api-service.yaml" << 'EOF'
apiVersion: v1
kind: Service
metadata:
  name: fastfood-api-service
  labels:
    app: fastfood-api
spec:
  type: NodePort
  selector:
    app: fastfood-api
  ports:
    - port: 80
      targetPort: 8080
      protocol: TCP
EOF

    # HPA
    cat > "${TEMP_DIR}/hpa.yaml" << 'EOF'
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: fastfood-api-hpa
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: fastfood-api
  minReplicas: 3
  maxReplicas: 6
  metrics:
    - type: Resource
      resource:
        name: cpu
        target:
          type: Utilization
          averageUtilization: 70
EOF
}

# Iniciar ou verificar o Minikube
echo -e "${YELLOW}Verificando status do Minikube...${NC}"
if minikube status | grep -q "Running"; then
    echo -e "${GREEN}Minikube já está em execução.${NC}"
else
    echo -e "${YELLOW}Iniciando Minikube...${NC}"
    minikube start --memory=4096 --cpus=2

    if [ $? -ne 0 ]; then
        echo -e "${RED}Falha ao iniciar o Minikube. Tentando com configurações mais básicas...${NC}"
        minikube delete
        minikube start --memory=2048 --cpus=2 --kubernetes-version=v1.25.0

        if [ $? -ne 0 ]; then
            echo -e "${RED}Falha ao iniciar o Minikube. Saindo.${NC}"
            exit 1
        fi
    fi
fi

# Habilitar addons necessários
echo -e "${YELLOW}Habilitando addons necessários...${NC}"
minikube addons enable metrics-server
minikube addons enable storage-provisioner
minikube addons enable default-storageclass

# Verificar se os addons estão habilitados corretamente
echo -e "${YELLOW}Verificando status dos addons...${NC}"
if ! minikube addons list | grep -E "storage-provisioner.*enabled" > /dev/null; then
    echo -e "${RED}Addon storage-provisioner não está habilitado. Isto pode causar problemas com o PVC.${NC}"
    echo -e "${YELLOW}Tentando habilitar novamente...${NC}"
    minikube addons enable storage-provisioner
fi

# Verificar se a classe de armazenamento padrão está disponível
echo -e "${YELLOW}Verificando StorageClass padrão...${NC}"
if ! kubectl get storageclass standard &> /dev/null; then
    echo -e "${RED}StorageClass 'standard' não encontrada. Tentando verificar outras classes de armazenamento...${NC}"
    kubectl get storageclass

    if ! kubectl get storageclass &> /dev/null; then
        echo -e "${RED}Nenhuma StorageClass encontrada. Isto pode causar problemas com o PVC.${NC}"
    else
        echo -e "${YELLOW}Outras StorageClasses encontradas. Continuando...${NC}"
    fi
fi

# Criar arquivos YAML
create_yaml_files

# Aplicar os arquivos YAML na ordem correta
echo -e "${YELLOW}Aplicando recursos ao cluster na sequência correta...${NC}"

echo -e "${YELLOW}1. Aplicando Secret...${NC}"
kubectl apply -f "${TEMP_DIR}/secret.yaml"

echo -e "${YELLOW}2. Aplicando ConfigMap...${NC}"
kubectl apply -f "${TEMP_DIR}/configmap.yaml"

echo -e "${YELLOW}3. Aplicando PVC do MySQL...${NC}"
kubectl apply -f "${TEMP_DIR}/mysql-pvc.yaml"

# Verificar se o PVC foi criado corretamente
echo -e "${YELLOW}Verificando se o PVC foi criado corretamente...${NC}"
kubectl get pvc mysql-pvc
pvc_status=$(kubectl get pvc mysql-pvc -o jsonpath='{.status.phase}')
if [ "$pvc_status" != "Bound" ] && [ "$pvc_status" != "Pending" ]; then
    echo -e "${RED}PVC não está em estado Bound ou Pending. Isto pode causar problemas.${NC}"
    echo -e "${YELLOW}Estado atual: $pvc_status${NC}"
    echo -e "${YELLOW}Continuando mesmo assim...${NC}"
fi

echo -e "${YELLOW}4. Aplicando Deployment do MySQL...${NC}"
kubectl apply -f "${TEMP_DIR}/mysql-deployment.yaml"

echo -e "${YELLOW}5. Aplicando Service do MySQL...${NC}"
kubectl apply -f "${TEMP_DIR}/mysql-service.yaml"

# Aguardar o MySQL estar pronto de forma mais robusta
echo -e "${YELLOW}Aguardando o MySQL ficar pronto (pode levar algum tempo)...${NC}"
MYSQL_READY=false
RETRY_COUNT=0
MAX_RETRIES=30

# Opção para pular a verificação
echo -e "${YELLOW}Deseja pular a verificação do MySQL e continuar? (s/n) ${NC}"
read -r skip_mysql_check
if [[ "$skip_mysql_check" =~ ^([sS])$ ]]; then
    echo -e "${YELLOW}Pulando verificação do MySQL...${NC}"
    MYSQL_READY=true
fi

while [ "$MYSQL_READY" = false ] && [ $RETRY_COUNT -lt $MAX_RETRIES ]; do
    RETRY_COUNT=$((RETRY_COUNT+1))

    # Verificar se o pod está rodando
    MYSQL_POD=$(kubectl get pods -l app=mysql -o jsonpath='{.items[0].metadata.name}' 2>/dev/null)

    if [ -z "$MYSQL_POD" ]; then
        echo -e "${YELLOW}Aguardando criação do pod MySQL... ($RETRY_COUNT/$MAX_RETRIES)${NC}"
        sleep 5
        continue
    fi

    POD_STATUS=$(kubectl get pod $MYSQL_POD -o jsonpath='{.status.phase}')

    if [ "$POD_STATUS" != "Running" ]; then
        echo -e "${YELLOW}Pod MySQL está em estado $POD_STATUS. Aguardando... ($RETRY_COUNT/$MAX_RETRIES)${NC}"
        sleep 5
        continue
    fi

    # Verificar se o MySQL está respondendo dentro do pod
    if kubectl exec $MYSQL_POD -- mysqladmin ping -h localhost -u root -prootpassword --silent &> /dev/null; then
        echo -e "${GREEN}MySQL está pronto e respondendo!${NC}"
        MYSQL_READY=true
    else
        # Tentar uma abordagem alternativa para verificar se o MySQL está pronto
        if kubectl exec $MYSQL_POD -- mysql -u root -prootpassword -e "SELECT 1;" &> /dev/null; then
            echo -e "${GREEN}MySQL está pronto e respondendo!${NC}"
            MYSQL_READY=true
        else
            echo -e "${YELLOW}MySQL ainda não está respondendo. Aguardando... ($RETRY_COUNT/$MAX_RETRIES)${NC}"

            # Para casos em que o MySQL está rodando, mas não responde ao ping
            if [ $RETRY_COUNT -gt 15 ]; then
                echo -e "${YELLOW}Muitas tentativas, verificando logs e continuando...${NC}"
                kubectl logs $MYSQL_POD | tail -n 20

                # Se o log mostrar que o MySQL está pronto para conexões, continuar
                if kubectl logs $MYSQL_POD | grep -q "ready for connections"; then
                    echo -e "${GREEN}Log indica que MySQL está pronto para conexões. Continuando...${NC}"
                    MYSQL_READY=true
                else
                    echo -e "${YELLOW}Tentando mais algumas vezes...${NC}"
                fi
            fi

            # Verificar logs para diagnóstico
            if [ $((RETRY_COUNT % 5)) -eq 0 ]; then
                echo -e "${YELLOW}Verificando logs do MySQL:${NC}"
                kubectl logs $MYSQL_POD --tail=20
            fi

            sleep 10
        fi
    fi
done

if [ "$MYSQL_READY" = false ]; then
    echo -e "${RED}Timeout ao aguardar o MySQL ficar pronto. Continuando mesmo assim, mas a aplicação pode falhar.${NC}"
else
    echo -e "${GREEN}Continuando com a implantação...${NC}"
fi

# Verificar se o banco de dados existe e criá-lo se necessário
echo -e "${YELLOW}Garantindo que o banco de dados fastfood_db exista...${NC}"
kubectl exec $MYSQL_POD -- mysql -u root -prootpassword -e "CREATE DATABASE IF NOT EXISTS fastfood_db;"
if [ $? -eq 0 ]; then
    echo -e "${GREEN}Banco de dados fastfood_db está disponível.${NC}"
else
    echo -e "${RED}Não foi possível criar o banco de dados. A aplicação pode falhar.${NC}"
fi

echo -e "${YELLOW}6. Aplicando Deployment da API...${NC}"
kubectl apply -f "${TEMP_DIR}/uaifood-deployment.yaml"

echo -e "${YELLOW}7. Aplicando Service da API...${NC}"
kubectl apply -f "${TEMP_DIR}/api-service.yaml"

echo -e "${YELLOW}8. Aplicando HPA...${NC}"
kubectl apply -f "${TEMP_DIR}/hpa.yaml"

# Limpar diretório temporário
rm -rf "${TEMP_DIR}"

# Verificar o status dos recursos
echo -e "${YELLOW}Verificando status dos recursos...${NC}"
kubectl get pods
kubectl get svc
kubectl get hpa

# Monitorar o status da aplicação
echo -e "${YELLOW}Monitorando o status da aplicação por 30 segundos...${NC}"
echo -e "${YELLOW}(Pressione Ctrl+C para interromper o monitoramento)${NC}"
MONITOR_COUNT=0
while [ $MONITOR_COUNT -lt 6 ]; do
    MONITOR_COUNT=$((MONITOR_COUNT+1))
    kubectl get pods
    sleep 5
done

# Obter URL do serviço
echo -e "${BLUE}===========================================${NC}"
echo -e "${GREEN}Implantação concluída!${NC}"
echo -e "${BLUE}Você pode acessar a aplicação através do URL:${NC}"
minikube service fastfood-api-service --url

echo -e "${BLUE}Comandos úteis para diagnóstico:${NC}"
echo -e "  ${YELLOW}kubectl get pods${NC} - Lista todos os pods"
echo -e "  ${YELLOW}kubectl get svc${NC} - Lista todos os serviços"
echo -e "  ${YELLOW}kubectl logs <nome-do-pod>${NC} - Exibe logs de um pod específico"
echo -e "  ${YELLOW}kubectl describe pod <nome-do-pod>${NC} - Exibe detalhes de um pod"
echo -e "  ${YELLOW}minikube dashboard${NC} - Abre o dashboard do Kubernetes"

echo -e "${BLUE}Se a aplicação mostrar CrashLoopBackOff, verifique os logs:${NC}"
echo -e "  ${YELLOW}kubectl logs -l app=fastfood-api${NC}"
echo -e "  ${YELLOW}kubectl logs -l app=mysql${NC}"

echo -e "${BLUE}===========================================${NC}"