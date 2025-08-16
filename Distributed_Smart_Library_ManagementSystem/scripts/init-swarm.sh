#!/bin/bash

# Initialize Docker Swarm
echo "Initializing Docker Swarm..."

# Initialize swarm (this machine becomes manager)
docker swarm init

# Create overlay network
docker network create --driver overlay --attachable library-network

echo "Docker Swarm initialized successfully!"
echo "To add worker nodes, run the join command shown above on other machines."