#!/bin/bash

# Database status checker script
echo "=== Database Status Checker ==="
echo ""

# Load environment variables
if [ -f .env ]; then
    export $(cat .env | grep -v '^#' | xargs)
fi

# Default values
DB_USERNAME=${DB_USERNAME:-library_user}
DB_PASSWORD=${DB_PASSWORD:-library_password123}
POSTGRES_DB=${POSTGRES_DB:-library_db}

# Function to execute SQL commands
execute_sql() {
    local database=$1
    local sql_command=$2
    docker exec -i $(docker ps -q -f name=library-stack_postgres-db) psql -U "$DB_USERNAME" -d "$database" -c "$sql_command"
}

# Function to check if database is accessible
check_db_connection() {
    echo "Checking PostgreSQL connection..."
    if docker exec $(docker ps -q -f name=library-stack_postgres-db) pg_isready -U "$DB_USERNAME" > /dev/null 2>&1; then
        echo "✅ PostgreSQL is running and accessible"
        return 0
    else
        echo "❌ PostgreSQL is not accessible"
        return 1
    fi
}

# Function to list all databases
list_databases() {
    echo ""
    echo "=== Available Databases ==="
    execute_sql "$POSTGRES_DB" "\l"
}

# Function to check tables in each database
check_tables() {
    local databases=("user_db" "book_db" "loan_db" "stats_db")

    for db in "${databases[@]}"; do
        echo ""
        echo "=== Tables in $db ==="
        if execute_sql "$db" "\dt" 2>/dev/null; then
            echo "✅ $db is accessible"

            # Count rows in each table
            echo ""
            echo "Row counts in $db:"
            execute_sql "$db" "
                SELECT
                    schemaname,
                    tablename,
                    n_tup_ins - n_tup_del as row_count
                FROM pg_stat_user_tables
                ORDER BY tablename;
            " 2>/dev/null || echo "No tables found or no statistics available"
        else
            echo "❌ Cannot access $db or no tables exist yet"
        fi
    done
}

# Function to show service status
show_service_status() {
    echo ""
    echo "=== Service Status ==="
    docker service ls | grep library-stack

    echo ""
    echo "=== Recent Service Logs ==="
    services=("user-service" "book-service" "loan-service" "stats-service")

    for service in "${services[@]}"; do
        echo ""
        echo "--- Last 5 lines from library-stack_$service ---"
        docker service logs --tail 5 library-stack_$service 2>/dev/null || echo "Service not found or not running"
    done
}

# Function to show database logs
show_db_logs() {
    echo ""
    echo "=== PostgreSQL Logs (last 10 lines) ==="
    docker service logs --tail 10 library-stack_postgres-db 2>/dev/null || echo "Database service not found"
}

# Main execution
if check_db_connection; then
    list_databases
    check_tables
else
    echo ""
    echo "Database is not accessible. Checking service status..."
    show_service_status
    show_db_logs
    echo ""
    echo "Try running: docker service ls"
    echo "To restart database: docker service update --force library-stack_postgres-db"
fi

# Interactive mode
echo ""
echo "=== Interactive Commands ==="
echo "1. Connect to PostgreSQL: docker exec -it \$(docker ps -q -f name=library-stack_postgres-db) psql -U $DB_USERNAME -d $POSTGRES_DB"
echo "2. View service logs: docker service logs library-stack_[service-name]"
echo "3. Scale service: docker service scale library-stack_[service-name]=X"
echo "4. Update service: docker service update --force library-stack_[service-name]"

# Service status check
show_service_status