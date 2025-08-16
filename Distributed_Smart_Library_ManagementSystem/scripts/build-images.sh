#!/bin/bash

# Build all Docker images
echo "Building Docker images..."

# Build Nginx
docker build -f Dockerfile.nginx -t library-nginx:latest .

# Build User Service
docker build -f ./User_Service/Smart_Library_Management_System/Dockerfile -t library-user-service:latest ./User_Service/Smart_Library_Management_System/

# Build Book Service
docker build -f ./Book_Service/Smart_Library_Management_System/Dockerfile -t library-book-service:latest ./Book_Service/Smart_Library_Management_System/

# Build Loan Service
docker build -f ./Loan_Service/Smart_Library_Management_System/Dockerfile -t library-loan-service:latest ./Loan_Service/Smart_Library_Management_System/

# Build Stats Service
docker build -f ./Stats_Service/Smart_Library_Management_System/Dockerfile -t library-stats-service:latest ./Stats_Service/Smart_Library_Management_System/

echo "All images built successfully!"