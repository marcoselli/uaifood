# Guia de Implantação Kubernetes para Aplicação Fast Food

Este guia fornece instruções detalhadas para implantar a aplicação Fast Food em um cluster Kubernetes utilizando Minikube. O processo de implantação inclui a configuração de todos os componentes necessários, como bancos de dados, serviços e configurações de autoescalamento.

## Pré-requisitos

Antes de iniciar a implantação, certifique-se de ter as seguintes ferramentas instaladas em seu sistema:

- Docker Desktop
- Minikube
- kubectl (Ferramenta de linha de comando do Kubernetes)

Você pode verificar a instalação dessas ferramentas usando os seguintes comandos:
```bash
docker --version
minikube version
kubectl version
```

## Estrutura do Projeto

A configuração de implantação segue esta estrutura de diretórios:

```
k8s/
├── configmap/
│   ├── configmap.yaml
│   └── secret.yaml
├── autoscaling/
│   └── hpa.yaml
└── base/
    ├── deployments/
    │   ├── mysql-deployment.yaml
    │   └── uaifood-deployment.yaml
    ├── services/
    │   ├── mysql-service.yaml
    │   └── api-service.yaml
    └── storage/
        └── mysql-pvc.yaml
```

## Processo de Implantação

### 1. Configuração do Ambiente

Primeiro, inicie o Minikube com recursos apropriados:

```bash
minikube start \
  --driver=docker \
  --kubernetes-version=v1.29.0 \
  --memory=4096 \
  --cpus=2 \
  --extra-config=apiserver.enable-admission-plugins="LimitRanger,NamespaceExists,NamespaceLifecycle,ResourceQuota,ServiceAccount,DefaultStorageClass,MutatingAdmissionWebhook" \
  --wait=all
```

Habilite o servidor de métricas para o Escalonamento Horizontal de Pods:
```bash
minikube addons enable metrics-server
```

### 2. Criação do Namespace

Crie um namespace dedicado para a aplicação:
```bash
kubectl create namespace fastfood
```

### 3. Implantação dos Componentes

Implante os componentes na seguinte ordem para garantir a resolução adequada de dependências:

a. Configuração de Armazenamento:
```bash
kubectl apply -f k8s/base/storage/mysql-pvc.yaml -n fastfood
```

b. Configurações e Secrets:
```bash
kubectl apply -f k8s/configmap/ -n fastfood
```

c. Implantação de Services:
```bash
kubectl apply -f k8s/base/services/ -n fastfood
```

d. Deployments da Aplicação:
```bash
# Implante o MySQL primeiro
kubectl apply -f k8s/base/deployments/mysql-deployment.yaml -n fastfood

# Aguarde o MySQL estar pronto, depois implante a aplicação
kubectl apply -f k8s/base/deployments/uaifood-deployment.yaml -n fastfood
```

e. Configuração de Autoscaling:
```bash
kubectl apply -f k8s/autoscaling/hpa.yaml -n fastfood
```

### 4. Passos de Verificação

Monitore o status da implantação usando estes comandos:

Verificar status dos pods:
```bash
kubectl get pods -n fastfood
```

Verificar serviços:
```bash
kubectl get services -n fastfood
```

Verificar Escalonador Horizontal de Pods:
```bash
kubectl get hpa -n fastfood
```

Verificar volumes persistentes:
```bash
kubectl get pvc -n fastfood
```

### 5. Acessando a Aplicação

Obtenha a URL da aplicação:
```bash
minikube service fastfood-api-service -n fastfood --url
```

## Solução de Problemas

Se encontrar problemas durante a implantação, use estes comandos para depuração:

Visualizar informações detalhadas dos pods:
```bash
kubectl describe pods -n fastfood
```

Verificar logs dos pods:
```bash
kubectl logs -f <nome-do-pod> -n fastfood
```

Visualizar todos os recursos no namespace:
```bash
kubectl get all -n fastfood
```

## Limpeza

Para remover todos os recursos implantados:
```bash
kubectl delete namespace fastfood
```

## Configurar ambiente Docker do Minikube
```bash
eval $(minikube docker-env)
```

## Carregar a imagem no Minikube
```bash
minikube image load uaifood:2.0.0
```

## Verificar se a imagem está disponível no Minikube
```bash
minikube ssh 'docker images | grep uaifood'
```

## Para remover a imagem antiga se necessário
```bash
minikube ssh 'docker rmi uaifood:2.0.0'
```

### Monitoramento de Recursos

Monitore as métricas da aplicação:
```bash
kubectl top pods -n fastfood
kubectl top nodes
```

### Escalonamento

A aplicação escala automaticamente com base na utilização da CPU (configurado no HPA). Você pode escalar manualmente se necessário:
```bash
kubectl scale deployment fastfood-api --replicas=<número> -n fastfood
```

## Informações Adicionais

A aplicação utiliza MySQL como banco de dados backend. O sistema inclui:

- Escalonamento Horizontal de Pods configurado para escalar entre 3 e 6 réplicas
- Armazenamento persistente configurado para o banco de dados MySQL
- Verificações de saúde implementadas tanto para a aplicação quanto para o banco de dados
- Implantação com recursos e limites adequadamente configurados

## Suporte

Se encontrar problemas ou precisar de assistência, por favor:

1. Verifique os logs e eventos dos pods
2. Confirme se todas as configurações foram aplicadas corretamente
3. Certifique-se de que todos os recursos necessários estão disponíveis no cluster

Para problemas persistentes, colete as seguintes informações antes de solicitar suporte:

- Saída do comando `kubectl get all -n fastfood`
- Logs relevantes dos pods
- Quaisquer mensagens de erro ou comportamentos inesperados

## Desenvolvimento

Para contribuir com o projeto ou realizar modificações:

1. Certifique-se de entender a estrutura de diretórios do Kubernetes
2. Faça alterações em um ambiente de desenvolvimento antes de aplicar em produção
3. Teste todas as alterações localmente usando Minikube
4. Documente todas as alterações realizadas