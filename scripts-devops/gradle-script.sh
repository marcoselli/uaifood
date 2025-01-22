#!/bin/bash

# Gradle Build Helper Script
# Usage: ./gradle-helper.sh [command]

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check if Gradle wrapper exists
check_gradle() {
    if [ ! -f "./gradlew" ]; then
        echo -e "${RED}Error: Gradle wrapper not found!${NC}"
        echo "Would you like to create it? (y/n)"
        read -r response
        if [ "$response" = "y" ]; then
            gradle wrapper
        else
            exit 1
        fi
    fi
}

# Function to execute Gradle command with error handling
execute_gradle() {
    echo -e "${YELLOW}Executing: ${NC}./gradlew $1"
    ./gradlew "$1"
    if [ $? -ne 0 ]; then
        echo -e "${RED}Command failed!${NC}"
        exit 1
    fi
}

# Clean and build project
build_project() {
    echo -e "${GREEN}Building project...${NC}"
    execute_gradle "clean"
    execute_gradle "build"
}

# Run tests
run_tests() {
    echo -e "${GREEN}Running tests...${NC}"
    execute_gradle "test"
}

# Run Spring Boot application
run_app() {
    echo -e "${GREEN}Starting application...${NC}"
    execute_gradle "bootRun"
}

# Check dependencies
check_deps() {
    echo -e "${GREEN}Checking dependencies...${NC}"
    execute_gradle "dependencies"
}

# Create Docker image
create_docker() {
    echo -e "${GREEN}Creating Docker image...${NC}"
    execute_gradle "bootBuildImage"
}

# Show help menu
show_help() {
    echo -e "${GREEN}Gradle Helper Script${NC}"
    echo "Available commands:"
    echo "  build     - Clean and build project"
    echo "  test      - Run tests"
    echo "  run       - Run Spring Boot application"
    echo "  deps      - Show project dependencies"
    echo "  docker    - Create Docker image"
    echo "  help      - Show this help message"
}

# Main script logic
main() {
    check_gradle

    case "$1" in
        "build")
            build_project
            ;;
        "test")
            run_tests
            ;;
        "run")
            run_app
            ;;
        "deps")
            check_deps
            ;;
        "docker")
            create_docker
            ;;
        "help"|"")
            show_help
            ;;
        *)
            echo -e "${RED}Unknown command: $1${NC}"
            show_help
            exit 1
            ;;
    esac
}

# Execute main function with all arguments
main "$@"