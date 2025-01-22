#!/bin/bash

# Gradle Commands Utility Script
# Use: ./gradle-commands.sh [command]

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Function to display usage
display_usage() {
    echo -e "${YELLOW}Gradle Commands Utility${NC}"
    echo "Usage: $0 [command]"
    echo ""
    echo "Available commands:"
    echo "  clean       - Clean the project"
    echo "  build       - Build the entire project"
    echo "  test        - Run all tests"
    echo "  assemble    - Compile the source code"
    echo "  dependencies- Show project dependencies"
    echo "  run         - Run the application"
    echo "  publish     - Publish the project"
    echo "  info        - Show Gradle project info"
    echo "  help        - Show this help message"
}

# Execute Gradle command
execute_command() {
    case "$1" in
        clean)
            echo -e "${GREEN}Cleaning project...${NC}"
            ./gradlew clean
            ;;
        build)
            echo -e "${GREEN}Building project...${NC}"
            ./gradlew build
            ;;
        test)
            echo -e "${GREEN}Running tests...${NC}"
            ./gradlew test
            ;;
        assemble)
            echo -e "${GREEN}Assembling project...${NC}"
            ./gradlew assemble
            ;;
        dependencies)
            echo -e "${GREEN}Showing project dependencies...${NC}"
            ./gradlew dependencies
            ;;
        run)
            echo -e "${GREEN}Running application...${NC}"
            ./gradlew run
            ;;
        publish)
            echo -e "${GREEN}Publishing project...${NC}"
            ./gradlew publish
            ;;
        info)
            echo -e "${GREEN}Showing Gradle project info...${NC}"
            ./gradlew properties
            ;;
        help)
            display_usage
            ;;
        *)
            echo -e "${RED}Invalid command!${NC}"
            display_usage
            exit 1
            ;;
    esac
}

# Check if command is provided
if [ $# -eq 0 ]; then
    display_usage
    exit 1
fi

# Execute the specified command
execute_command "$1"

exit 0