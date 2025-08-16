#!/bin/bash

# Deploy script with options
BUILD_IMAGES=true
DEPLOY_STACK=true

# Parse command line arguments
while [[ $# -gt 0 ]]; do
  case $1 in
    --no-build)
      BUILD_IMAGES=false
      shift
      ;;
    --build-only)
      DEPLOY_STACK=false
      shift
      ;;
    --help)
      echo "Usage: $0 [options]"
      echo "Options:"
      echo "  --no-build    Deploy without building images"
      echo "  --build-only  Only build images, don't deploy"
      echo "  --help        Show this help message"
      exit 0
      ;;
    *)
      echo "Unknown option $1"
      exit 1
      ;;
  esac
done

# Build images if requested
if [ "$BUILD_IMAGES" = true ]; then
  echo "Building Docker images..."
  ./scripts/build-images.sh
  if [ $? -ne 0 ]; then
    echo "Image build failed!"
    exit 1
  fi
fi

# Deploy the stack if requested
if [ "$DEPLOY_STACK" = true ]; then
  echo "Deploying stack to Docker Swarm..."

  # Load environment variables if .env file exists
  if [ -f .env ]; then
    echo "Loading environment variables from .env file..."
    export $(cat .env | grep -v '^#' | xargs)
  fi

  # Deploy the stack
  docker stack deploy -c docker-stack.yml library-stack

  if [ $? -eq 0 ]; then
    echo "Stack deployed successfully!"
    echo "Services are starting up..."
    echo ""
    echo "Checking service status..."
    docker service ls
    echo ""
    echo "To check service logs, use:"
    echo "  docker service logs library-stack_[service-name]"
    echo ""
    echo "To check database status, use:"
    echo "  ./scripts/check-db.sh"
  else
    echo "Stack deployment failed!"
    exit 1
  fi
fi