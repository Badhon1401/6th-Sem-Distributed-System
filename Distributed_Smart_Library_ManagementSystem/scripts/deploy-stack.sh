#!/bin/bash

# Deploy the stack to Docker Swarm
echo "Deploying stack to Docker Swarm..."

# Build images first
./scripts/build-images.sh

# Deploy the stack
docker stack deploy -c docker-stack.yml library-stack

echo "Stack deployed successfully!"
echo "Services are starting up..."

# Show status
docker service ls