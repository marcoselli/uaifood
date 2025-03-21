#!/bin/bash

# Enable strict error handling
set -euo pipefail
trap 'handle_error $? $LINENO' ERR

# Configuration variables
IMAGE_NAME="uaifood"
IMAGE_TAG="1.0.1"
DOCKERFILE_PATH="./Dockerfile"
GRADLE_VERSION="8.10.2-jdk21-alpine"
JAVA_VERSION="21"

# Color definitions
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# Utility functions
print_status() {
    echo -e "${YELLOW}[$(date +'%Y-%m-%d %H:%M:%S')] $1${NC}"
}

print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

handle_error() {
    local exit_code=$1
    local line_number=$2
    print_error "Erro durante a execução do script (linha $line_number, código $exit_code)"
    exit "$exit_code"
}

validate_environment() {
    print_status "Validando ambiente de desenvolvimento..."

    # Verificar Docker
    if ! command -v docker &> /dev/null; then
        print_error "Docker não está instalado"
        exit 1
    fi

    # Verificar se o Docker daemon está rodando
    if ! docker info &> /dev/null; then
        print_error "Docker daemon não está em execução"
        exit 1
    fi

    # Verificar Dockerfile
    if [ ! -f "$DOCKERFILE_PATH" ]; then
        print_error "Dockerfile não encontrado em: $DOCKERFILE_PATH"
        exit 1
    fi

    # Verificar arquivo build.gradle.kts.kts
    if [ ! -f "build.gradle.kts" ]; then
        print_error "build.gradle.kts não encontrado"
        exit 1
    fi

    # Verificar diretório src
    if [ ! -d "src" ]; then
        print_error "Diretório src não encontrado"
        exit 1
    fi

    print_success "Ambiente validado com sucesso"
}

verify_docker_images() {
    print_status "Verificando imagens base necessárias..."

    # Verificar imagem do Gradle
    if ! docker images | grep -q "gradle.*$GRADLE_VERSION"; then
        print_status "Baixando imagem do Gradle..."
        docker pull "gradle:$GRADLE_VERSION"
    fi

    # Verificar imagem do OpenJDK
    if ! docker images | grep -q "openjdk.*$JAVA_VERSION"; then
        print_status "Baixando imagem do OpenJDK..."
        docker pull "openjdk:$JAVA_VERSION-slim"
    fi

    print_success "Imagens base verificadas"
}

build_image() {
    local full_image_name="$IMAGE_NAME:$IMAGE_TAG"
    print_status "Iniciando build da imagem: $full_image_name"

    # Registrar tempo inicial
    # shellcheck disable=SC2155
    local start_time=$(date +%s)

    # Executar build com cache
    if docker build \
        --build-arg GRADLE_VERSION=$GRADLE_VERSION \
        --build-arg JAVA_VERSION=$JAVA_VERSION \
        -t "$full_image_name" \
        -f "$DOCKERFILE_PATH" \
        --progress=plain \
        .; then

        # shellcheck disable=SC2155
        local end_time=$(date +%s)
        local duration=$((end_time - start_time))

        print_success "Build concluído em $duration segundos"

        # Exibir informações da imagem
        echo -e "\n${BLUE}Informações da imagem:${NC}"
        # shellcheck disable=SC2155
        local image_size=$(docker image inspect "$full_image_name" --format='{{.Size}}')
        echo "Tamanho: $(numfmt --to=iec-i --suffix=B "$image_size")"

        print_success "Imagem criada com sucesso: $full_image_name"

        echo -e "\n${BLUE}Comandos úteis:${NC}"
        echo "Executar a imagem: docker run -p 8080:8080 $full_image_name"
        echo "Visualizar logs: docker logs -f \$(docker ps -q -f ancestor=$full_image_name)"
        echo "Parar container: docker stop \$(docker ps -q -f ancestor=$full_image_name)"
    else
        print_error "Falha no build da imagem"
        exit 1
    fi
}

cleanup() {
    print_status "Realizando limpeza..."
    docker system prune -f
    print_success "Limpeza concluída"
}

main() {
    print_status "Iniciando processo de build da aplicação Spring Boot"

    validate_environment
    verify_docker_images
    build_image
    cleanup

    print_success "Processo concluído com sucesso!"
}

# Execução do script
main