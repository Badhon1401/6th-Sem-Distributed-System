#!/bin/bash

# Cleanup Docker Swarm resources
echo "Cleaning up Docker Swarm resources..."

# Remove stack
docker stack rm library-stack

# Wait for services to be removed
echo "Waiting for services to be removed..."
sleep 10

# Remove unused networks
docker network prune -f

# Remove unused volumes (optional - be careful!)
# docker volume prune -f

echo "Cleanup completed!"