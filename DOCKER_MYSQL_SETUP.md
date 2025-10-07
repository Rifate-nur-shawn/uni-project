# Docker MySQL Setup Documentation

**Date:** October 7, 2025  
**Database:** edispensary  
**Container Name:** mysql-db  
**Volume Name:** mysql-data

---

## Overview

This document describes the Docker-based MySQL setup for the e-Dispensary uni-project application, including the database schema import, configuration, and **persistent data storage using Docker volumes**.

---

## What Was Done

### 1. Docker Volume Creation

A Docker volume was created to ensure **data persistence** across container restarts and removals:

```bash
docker volume create mysql-data
```

**Why Use Volumes?**
- ✅ Data persists even if the container is removed
- ✅ Easy to backup and migrate data
- ✅ Better performance than bind mounts
- ✅ Data survives container recreation

### 2. Docker Container Setup with Persistent Volume

A MySQL 8.x container was created and started with the following configuration:

```bash
docker run -d \
  --name mysql-db \
  -e MYSQL_ROOT_PASSWORD=shawn12 \
  -e MYSQL_DATABASE=edispensary \
  -p 3307:3306 \
  -v mysql-data:/var/lib/mysql \
  mysql:latest
```

**Configuration Details:**
- **Container Name:** `mysql-db`
- **MySQL Root Password:** `shawn12`
- **Default Database:** `edispensary`
- **Port Mapping:** Host port `3307` → Container port `3306`
  - *Note: Port 3307 was used because port 3306 was already occupied by another MySQL instance (likely XAMPP)*
- **Volume Mount:** `mysql-data:/var/lib/mysql` - **All database data is stored here permanently**

### 3. Database Schema Import

The complete database schema was imported from:
```
/home/shawn/Documents/My-Project/uni-project/src/main/resources/database/complete_setup.sql
```

**Import Process:**
1. Wait for MySQL to initialize (15 seconds)
2. Execute the SQL script directly:
   ```bash
   docker exec -i mysql-db mysql -uroot -pshawn12 edispensary < /home/shawn/Documents/My-Project/uni-project/src/main/resources/database/complete_setup.sql
   ```

### 4. Database Structure Created

The following tables were created:

#### **user** Table
Stores user authentication and role information.
- `user_id` (INT, AUTO_INCREMENT, PRIMARY KEY)
- `username` (VARCHAR(100), UNIQUE)
- `password` (VARCHAR(255))
- `email` (VARCHAR(150))
- `role` (VARCHAR(50), DEFAULT 'customer')
- `created_at` (TIMESTAMP)

#### **medicine** Table
Stores product/medicine catalog information.
- `medicine_id` (INT, AUTO_INCREMENT, PRIMARY KEY)
- `product_name` (VARCHAR(200))
- `brand` (VARCHAR(150))
- `type` (VARCHAR(100))
- `status` (VARCHAR(50), DEFAULT 'Available')
- `price` (DECIMAL(10, 2))
- `image` (VARCHAR(255))
- `date` (DATE)
- `created_at` (TIMESTAMP)

#### **cart** Table
Stores shopping cart items for users.
- `cart_id` (INT, AUTO_INCREMENT, PRIMARY KEY)
- `user_id` (VARCHAR(100)) - References username
- `medicine_id` (INT, FOREIGN KEY → medicine.medicine_id)
- `quantity` (INT, DEFAULT 1)
- `date_added` (TIMESTAMP)

#### **customer** Table
Stores purchase history and orders.
- `customer_id` (INT, AUTO_INCREMENT, PRIMARY KEY)
- `customer_username` (VARCHAR(100))
- `type` (VARCHAR(100))
- `medicine_id` (INT, FOREIGN KEY → medicine.medicine_id)
- `brand` (VARCHAR(150))
- `productName` (VARCHAR(200))
- `quantity` (INT)
- `price` (DECIMAL(10, 2))
- `date` (TIMESTAMP)

### 5. Sample Data Imported

**Default Users Created:**
| Username | Password | Email | Role |
|----------|----------|-------|------|
| admin | admin123 | admin@edispensary.com | admin |
| pharmacist | pharma123 | pharmacist@edispensary.com | pharmacist |
| user | user123 | user@edispensary.com | customer |

**Sample Medicines:** 10 medicines were added to the catalog including:
- Amoxicillin 500mg (Antibiotic) - $18.99
- Paracetamol 500mg (Pain Relief) - $5.99
- Ibuprofen 400mg (Pain Relief) - $8.50
- Omega-3 Fish Oil (Supplement) - $24.99
- Vitamin D3 1000 IU (Supplement) - $15.99
- And 5 more medicines...

---

## How to Access the Database

### Option 1: MySQL Command Line (Interactive Terminal)
```bash
docker exec -it mysql-db mysql -uroot -pshawn12 edispensary
```

### Option 2: Execute SQL Commands
```bash
docker exec -i mysql-db mysql -uroot -pshawn12 edispensary -e "SELECT * FROM user;"
```

### Option 3: From Host Machine (if MySQL client installed)
```bash
mysql -h 127.0.0.1 -P 3307 -uroot -pshawn12 edispensary
```

---

## Docker Container Management

### View Running Containers
```bash
docker ps
```

### Stop the Container
```bash
docker stop mysql-db
```

### Start the Container
```bash
docker start mysql-db
```

### View Container Logs
```bash
docker logs mysql-db
```

### Remove the Container
```bash
docker stop mysql-db
docker rm mysql-db
```

---

## Docker Volume Management

### Understanding Data Persistence

**With Docker Volume (Current Setup):**
- ✅ Stop container → Data persists
- ✅ Remove container → Data persists
- ✅ Recreate container → Data persists
- ⚠️ Remove volume → Data is deleted permanently

**Without Docker Volume (Not Recommended):**
- ❌ Remove container → All data is lost forever

### Volume Operations

#### List All Volumes
```bash
docker volume ls
```

#### Inspect Volume Details
```bash
docker volume inspect mysql-data
```

#### Check Volume Location
```bash
docker volume inspect mysql-data --format '{{ .Mountpoint }}'
```

#### Remove Volume (⚠️ Deletes All Data)
```bash
# First remove container
docker stop mysql-db
docker rm mysql-db

# Then remove volume
docker volume rm mysql-data
```

### Recreating Container with Volume

If you need to recreate the container, **always include the volume mount** to preserve data:

```bash
docker stop mysql-db
docker rm mysql-db

# Recreate with SAME volume - data is preserved!
docker run -d \
  --name mysql-db \
  -e MYSQL_ROOT_PASSWORD=shawn12 \
  -e MYSQL_DATABASE=edispensary \
  -p 3307:3306 \
  -v mysql-data:/var/lib/mysql \
  mysql:latest
```

---

## Backup and Migration

### Full Database Backup
```bash
# SQL dump (recommended)
docker exec mysql-db mysqldump -uroot -pshawn12 edispensary > backup_$(date +%Y%m%d).sql

# Backup entire volume (advanced)
docker run --rm -v mysql-data:/data -v $(pwd):/backup ubuntu tar czf /backup/mysql-volume-backup.tar.gz -C /data .
```

### Restore from Backup
```bash
# From SQL dump
docker exec -i mysql-db mysql -uroot -pshawn12 edispensary < backup_20251007.sql

# From volume backup
docker run --rm -v mysql-data:/data -v $(pwd):/backup ubuntu tar xzf /backup/mysql-volume-backup.tar.gz -C /data
```

---

## Application Configuration

To connect your Java application to this Docker MySQL instance, update your database connection settings:

**Current Settings (from database.java):**
```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/edispensary";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "shawn12";
```

**Update to:**
```java
private static final String DB_URL = "jdbc:mysql://localhost:3307/edispensary";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "shawn12";
```

**Note:** Change the port from `3306` to `3307` to connect to the Docker container.

---

## Troubleshooting

### Port Already in Use
If you get "address already in use" error:
1. Check what's using port 3306: `sudo lsof -i :3306`
2. Stop XAMPP/other MySQL: `sudo /opt/lampp/lampp stop`
3. Or use a different port mapping (e.g., `-p 3308:3306`)

### Container Won't Start
```bash
docker logs mysql-db
```

### Check Container is Using Volume
```bash
docker inspect mysql-db | grep -A 10 Mounts
```

### Fresh Start (Delete Everything)
```bash
docker stop mysql-db
docker rm mysql-db
docker volume rm mysql-data

# Recreate from scratch
docker volume create mysql-data
docker run -d --name mysql-db -e MYSQL_ROOT_PASSWORD=shawn12 -e MYSQL_DATABASE=edispensary -p 3307:3306 -v mysql-data:/var/lib/mysql mysql:latest

# Re-import database
sleep 15
docker exec -i mysql-db mysql -uroot -pshawn12 edispensary < src/main/resources/database/complete_setup.sql
```

---

## Verification

The database setup was successful with:
- ✅ Docker volume `mysql-data` created
- ✅ Container running with persistent storage
- ✅ 3 users created
- ✅ 10 medicines in catalog
- ✅ All 4 tables created with proper relationships
- ✅ Foreign key constraints established
- ✅ Data persists across container restarts and removals

**Status Output:**
```
Database setup completed successfully!
Total Users: 3
Total Medicines: 10
```

**Verify Volume:**
```bash
$ docker volume ls
DRIVER    VOLUME NAME
local     mysql-data

$ docker inspect mysql-db | grep -A 5 Mounts
"Mounts": [
    {
        "Type": "volume",
        "Name": "mysql-data",
        "Source": "/var/lib/docker/volumes/mysql-data/_data",
        "Destination": "/var/lib/mysql"
    }
]
```

---

## Next Steps

1. ✅ **Volume created** - Data persistence enabled
2. ✅ **Java application updated** - Now connects to port 3307
3. **Test the connection** from your JavaFX application
4. **Regular backups** - Run mysqldump periodically
5. **Monitor logs** - `docker logs mysql-db` if issues arise

---

## Important Notes

1. **Always include the volume mount** when recreating containers:
   - `-v mysql-data:/var/lib/mysql`
   
2. **Your data is safe unless:**
   - You explicitly run `docker volume rm mysql-data`
   - You run `docker volume prune` (removes unused volumes)
   - You create a new container WITHOUT the volume mount

3. **Best Practices:**
   - Regular SQL backups: `mysqldump` daily/weekly
   - Test your backups occasionally
   - Document volume names and configurations
   - Use named volumes (like `mysql-data`) instead of anonymous volumes

---

## Additional Resources

- Docker MySQL Official Image: https://hub.docker.com/_/mysql
- Docker Volumes Documentation: https://docs.docker.com/storage/volumes/
- MySQL Documentation: https://dev.mysql.com/doc/
- Your complete setup SQL: `src/main/resources/database/complete_setup.sql`
- Volume Guide: `DOCKER_VOLUME_GUIDE.md` (detailed volume operations)

---

**Container ID:** f12099291f4f  
**MySQL Version:** latest (8.x)  
**Volume Name:** mysql-data  
**Status:** ✅ Running with persistent storage  
**Port:** 3307 (host) → 3306 (container)  
**Data Persistence:** ✅ Enabled via Docker volume
