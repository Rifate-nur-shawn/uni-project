# e-Dispensary - Pharmacy Management System

A JavaFX-based pharmacy management system with MySQL database integration, real-time chat functionality, and comprehensive inventory management.

![Java](https://img.shields.io/badge/Java-21-orange)
![JavaFX](https://img.shields.io/badge/JavaFX-21.0.6-blue)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![Maven](https://img.shields.io/badge/Maven-Build-red)

## 🚀 Quick Start

### Prerequisites
- Java JDK 21
- Maven 3.8+
- MySQL 8.0 (running on port 3307)

### Installation
```bash
# 1. Clone the repository
git clone <repository-url>

# 2. Set up database
mysql -u root -p -P 3307 < src/main/resources/database/complete_setup.sql

# 3. Build and run
mvn clean install
mvn javafx:run
```

### Default Login Credentials
| Username | Password | Role |
|----------|----------|------|
| admin | admin123 | Admin |
| pharmacist | pharma123 | Pharmacist |
| user | user123 | Customer |

## 📚 Documentation

📖 **[Complete Project Documentation](PROJECT_DOCUMENTATION.md)** - Detailed guide covering:
- Full architecture and technology stack
- Database schema with all tables
- Complete function reference for every class
- Data flow diagrams
- Networking and real-time features explanation
- Step-by-step setup instructions

## ✨ Key Features

### For Customers
- 🏠 Browse medicine catalog
- 🔍 Search and filter products
- 🛒 Shopping cart functionality
- 💳 Checkout and order placement
- 💬 Real-time chat with pharmacist

### For Admin/Pharmacist
- 📊 Dashboard with analytics
- 📦 Inventory management (CRUD operations)
- 👥 Customer order management
- 💰 Sales tracking and reports
- 💬 Real-time customer support chat

## 🏗️ Project Structure

```
uni-project/
├── src/main/
│   ├── java/com/iamshawn/uniproject/
│   │   ├── HelloApplication.java       # Main entry point
│   │   ├── database.java               # Database connection
│   │   ├── ChatServerManager.java      # Real-time chat server (Port 8888)
│   │   ├── Controllers/                # MVC Controllers
│   │   │   ├── HelloController.java    # Login/Signup
│   │   │   ├── HomeController.java     # Landing page
│   │   │   ├── ProductsController.java # Product catalog
│   │   │   ├── CartController.java     # Shopping cart
│   │   │   ├── dashboardController.java # Admin panel
│   │   │   ├── AdminChatController.java # Chat interface
│   │   │   └── NavbarController.java   # Navigation
│   │   └── Models/                     # Data models
│   │       ├── medicineData.java
│   │       ├── customerData.java
│   │       └── CartItem.java
│   └── resources/
│       ├── com/iamshawn/uniproject/*.fxml  # UI layouts
│       ├── database/complete_setup.sql      # Database setup
│       └── images/                          # Product images
└── pom.xml                             # Maven configuration
```

## 🔄 Real-Time Features Explained

### 1. **Chat System (TCP/IP Socket Programming)**
- **Server**: `ChatServerManager.java` runs on **port 8888**
- **Protocol**: Custom message protocol (USER:<name>, MSG:<text>)
- **Threading**: Multi-threaded server with one thread per client
- **UI Updates**: JavaFX `Platform.runLater()` for thread-safe UI updates

**How it works:**
1. Admin starts server when dashboard loads
2. Customer connects via chat button (creates Socket to localhost:8888)
3. Messages sent through `PrintWriter` (OutputStreams)
4. Messages received via `BufferedReader` (InputStreams)
5. UI instantly updates on both ends

### 2. **Database Real-Time Updates**
- **Cart Updates**: `ObservableList<CartItem>` auto-refreshes TableView
- **Inventory**: Admin changes immediately reflect in product listings
- **Dashboard Stats**: Recalculated after each transaction
- **Implementation**: JDBC with PreparedStatements + JavaFX Property binding

## 🗄️ Database Schema Overview

### Core Tables
- **user** - Authentication (username, password, role)
- **medicine** - Product catalog (id, name, brand, price, status)
- **cart** - Shopping cart (user_id, medicine_id, quantity)
- **customer** - Order line items
- **customer_info** - Order summaries/invoices

See [DATABASE_SETUP.md](DOCKER_MYSQL_SETUP.md) for detailed schema.

## 🔧 Technology Stack

| Layer | Technology |
|-------|-----------|
| **Frontend** | JavaFX 21.0.6, FXML, CSS |
| **Backend** | Java 21, JDBC |
| **Database** | MySQL 8.0 (Port 3307) |
| **Networking** | Java Socket API (TCP/IP) |
| **Build Tool** | Maven |
| **UI Framework** | Scene Builder compatible |

## 📊 Function Summary

### Data Flow
```
User Input → Controller → database.connectDb() → MySQL Query
                ↓
          ResultSet → Model (POJO) → ObservableList
                ↓
          TableView/UI Display
```

### Key Functions by Component

**Database Layer:**
- `database.connectDb()` - Creates MySQL connection

**Authentication:**
- `HelloController.handleLogin()` - Validates credentials (SELECT from user)
- `HelloController.handleSignup()` - Registers new user (INSERT into user)

**Product Management:**
- `ProductsController.displayProducts()` - Shows catalog (SELECT from medicine)
- `dashboardController.addMedicinesAdd()` - Adds inventory (INSERT into medicine)
- `dashboardController.addMedicinesUpdate()` - Updates product (UPDATE medicine)

**Cart Operations:**
- `CartController.loadCartItems()` - Loads cart (SELECT cart JOIN medicine)
- `CartController.processCheckout()` - Completes order (INSERT customer, DELETE cart)

**Real-Time Chat:**
- `ChatServerManager.startServer()` - Opens ServerSocket on port 8888
- `AdminChatController.sendMessage()` - Writes to Socket OutputStream
- `startMessageReceiver()` - Reads from Socket InputStream in separate thread

**Navigation:**
- `SceneSwitcher.switchScene()` - Changes views with data passing
- `DataReceiver.receiveData()` - Interface for receiving navigation data

## 🔐 Security Notes

⚠️ **Current Status** (Development Build):
- Passwords stored in plain text
- PreparedStatements used (prevents basic SQL injection)
- No session management

✅ **For Production**: Implement password hashing (BCrypt), HTTPS, JWT tokens

## 📞 Support

For detailed documentation on every function, data flow, and networking implementation, see:
👉 **[PROJECT_DOCUMENTATION.md](PROJECT_DOCUMENTATION.md)**

---

**Version**: 1.0  
**Last Updated**: October 2025  
**Developer**: iamshawn
