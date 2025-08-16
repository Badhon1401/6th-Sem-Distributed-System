#!/bin/bash

echo "=== Quick Service Health Check ==="
echo ""

# Service status
echo "🔍 Service Status:"
docker service ls | grep library-stack

echo ""
echo "🔍 Service Health Details:"
services=("user-service" "book-service" "loan-service" "stats-service")

for service in "${services[@]}"; do
    echo ""
    echo "--- $service ---"

    # Get running task count
    running=$(docker service ps library-stack_$service --filter "desired-state=running" --format "table {{.Name}}\t{{.CurrentState}}" | grep -c "Running")
    total=$(docker service inspect library-stack_$service --format "{{.Spec.Mode.Replicated.Replicas}}")

    echo "Status: $running/$total replicas running"

    # Get recent logs (last 3 lines)
    echo "Recent logs:"
    docker service logs --tail 3 library-stack_$service 2>/dev/null | tail -3
done

echo ""
echo "🔍 Database Status:"
if docker exec $(docker ps -q -f name=library-stack_postgres-db) pg_isready -U library_user > /dev/null 2>&1; then
    echo "✅ Database is accessible"
else
    echo "❌ Database is not accessible"
fi

echo ""
echo "🔍 Network Connectivity Test:"
# Test if services can reach database
for service in "${services[@]}"; do
    container_id=$(docker ps -q -f name=library-stack_$service | head -1)
    if [ ! -z "$container_id" ]; then
        if docker exec $container_id ping -c 1 postgres-db > /dev/null 2>&1; then
            echo "✅ $service can reach database"
        else
            echo "❌ $service cannot reach database"
        fi
    else
        echo "⚠️  $service container not found"
    fi
done

echo ""
echo "🔍 Quick Commands:"
echo "Check specific service: docker service logs library-stack_[service-name]"
echo "Scale service: docker service scale library-stack_[service-name]=2"
echo "Force restart: docker service update --force library-stack_[service-name]"