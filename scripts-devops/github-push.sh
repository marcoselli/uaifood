#!/bin/bash

# Configurações
GITHUB_USERNAME="almeidadevops042@gmail.com"
GITHUB_TOKEN="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"
REPO_NAME="git@github.com:marcoselli/uaifood.git"

# Configura a URL do repositório com o token
git remote set-url origin "https://$GITHUB_USERNAME:$GITHUB_TOKEN@github.com/$GITHUB_USERNAME/$REPO_NAME.git"

# Adiciona todas as alterações
git add .

# Solicita mensagem do commit
echo "Digite a mensagem do commit:"
read commit_message

# Faz o commit
git commit -m "$commit_message"

# Faz o push
git push origin feature/eduardo-arch-k8s

# Remove o token das configurações
git remote set-url origin "https://github.com/$GITHUB_USERNAME/$REPO_NAME.git"