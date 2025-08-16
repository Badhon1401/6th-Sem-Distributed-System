-- Create databases for each service (optional - or use one database)
CREATE DATABASE IF NOT EXISTS library_db;
CREATE DATABASE IF NOT EXISTS user_db;
CREATE DATABASE IF NOT EXISTS book_db;
CREATE DATABASE IF NOT EXISTS loan_db;
CREATE DATABASE IF NOT EXISTS stats_db;

-- Set permissions
GRANT ALL PRIVILEGES ON DATABASE library_db TO library_user;
GRANT ALL PRIVILEGES ON DATABASE user_db TO library_user;
GRANT ALL PRIVILEGES ON DATABASE book_db TO library_user;
GRANT ALL PRIVILEGES ON DATABASE loan_db TO library_user;
GRANT ALL PRIVILEGES ON DATABASE stats_db TO library_user;
