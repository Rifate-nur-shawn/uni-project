 🏥 e-Dispensary - Complete Project Documentation

## 📋 Table of Contents
1. [Project Overview](#project-overview)
2. [Architecture & Technology Stack](#architecture--technology-stack)
3. [Database Schema](#database-schema)
4. [Application Components](#application-components)
5. [Data Flow & Networking](#data-flow--networking)
6. [Real-Time Features](#real-time-features)
7. [Function Reference](#function-reference)
8. [Setup Instructions](#setup-instructions)

---

## 🎯 Project Overview

**e-Dispensary** is a comprehensive pharmacy management system built with JavaFX and MySQL. It provides functionality for:
- Customer browsing and purchasing medicines
- Admin/Pharmacist inventory management
- Real-time chat communication between customers and admins
- Shopping cart and checkout system
- Dashboard with analytics and reporting

---

## 🏗️ Architecture & Technology Stack

### Frontend
- **JavaFX 21.0.6** - UI Framework
- **FXML** - UI Layout Definition
- **CSS** - Styling

### Backend
- **Java 21** - Programming Language
- **JDBC** - Database Connectivity
- **Socket Programming** - Real-time Chat

### Database
- **MySQL 8.0** - Relational Database
- **Port**: 3307 (Customized)
- **Database Name**: `edispensary`

### Build Tool
- **Maven** - Dependency Management & Build

---

## 🗄️ Database Schema

### Tables

#### 1. **user**
Stores user authentication and role information.
```sql
- user_id (INT, PRIMARY KEY, AUTO_INCREMENT)
- username (VARCHAR(100), UNIQUE)
- password (VARCHAR(255))
- email (VARCHAR(150))
- role (VARCHAR(50)) - Values: 'admin', 'pharmacist', 'customer'
- created_at (TIMESTAMP)
```

#### 2. **medicine**
Stores medicine/product catalog.
```sql
- id (INT, PRIMARY KEY, AUTO_INCREMENT)
- medicine_id (INT, UNIQUE)
- productName (VARCHAR(200))
- brand (VARCHAR(150))
- type (VARCHAR(100)) - Category/Type
- status (VARCHAR(50)) - 'Available' or 'Not Available'
- price (DECIMAL(10,2))
- image (VARCHAR(255)) - Image path
- date (DATE)
- created_at (TIMESTAMP)
```

#### 3. **cart**
Stores shopping cart items for users.
```sql
- cart_id (INT, PRIMARY KEY, AUTO_INCREMENT)
- user_id (VARCHAR(100))
- medicine_id (INT)
- quantity (INT)
- date_added (TIMESTAMP)
```

#### 4. **customer**
Stores purchase transaction line items.
```sql
- id (INT, PRIMARY KEY, AUTO_INCREMENT)
- customer_id (INT)
- type (VARCHAR(100))
- medicine_id (INT)
- brand (VARCHAR(150))
- productName (VARCHAR(200))
- quantity (INT)
- price (DECIMAL(10,2))
- date (DATE)
```

#### 5. **customer_info**
Stores purchase summaries/invoices.
```sql
- id (INT, PRIMARY KEY, AUTO_INCREMENT)
- customer_id (INT)
- total (DECIMAL(10,2))
- date (DATE)
- created_at (TIMESTAMP)
```

---

## 🧩 Application Components

### 1. Core Application Files

#### **HelloApplication.java**
- **Purpose**: Main entry point of the application
- **Functions**:
  - `start(Stage stage)` - Initializes the JavaFX application
  - `main(String[] args)` - Launches the application
- **Features**:
  - Sets initial window size (1400x800)
  - Loads `home.fxml` as starting scene
  - Implements window dragging functionality
  - Sets window as non-resizable

---

### 2. Database & Data Management

#### **database.java**
- **Purpose**: Central database connection manager
- **Function**: `connectDb()` - Returns MySQL connection
- **Configuration**:
  - Host: `localhost:3307`
  - Database: `edispensary`
  - Credentials: `root` / `shawn12`
- **Thread Safety**: Each controller creates its own connection

#### **getData.java**
- **Purpose**: Global state management (session data)
- **Properties**:
  - `username` - Currently logged-in user
  - `path` - Image path for uploads
  - `isAdmin` - User role flag

---

### 3. Data Models (POJOs)

#### **medicineData.java**
- **Purpose**: Medicine entity representation
- **Properties**: medicineId, brand, productName, type, status, price, date, image
- **Used By**: Dashboard, Products listing, Cart operations

#### **customerData.java**
- **Purpose**: Purchase transaction representation
- **Properties**: customerId, type, medicineId, brand, productName, quantity, price, date
- **Used By**: Purchase history, Dashboard analytics

#### **CartItem.java**
- **Purpose**: Shopping cart item with JavaFX properties
- **Properties**: cartId, username, medicineId, productName, brand, price, quantity, total
- **Features**: Uses JavaFX Property classes for UI binding
- **Calculated Field**: `total` = price × quantity

---

### 4. Controllers (MVC Pattern)

#### **HelloController.java** (Login/Signup)
**Functions:**
- `initialize()` - Sets up login form
- `handleLogin()` - Authenticates user against database
- `handleSignup()` - Creates new user account
- `showLoginForm()` - Displays login view
- `showSignupForm()` - Displays signup view
- `handleClose()` - Closes application

**Database Operations:**
- **READ**: Validates credentials from `user` table
- **WRITE**: Inserts new user records

**Navigation:** 
- Success → Routes to `home.fxml` or `dashboard.fxml` based on role

---

#### **HomeController.java** (Landing Page)
**Functions:**
- `initialize()` - Loads navbar and sample medicines
- `loadNavbar()` - Embeds navigation bar
- `loadLatestProducts()` - Displays featured medicines (6 items)
- `createMedicineCard()` - Generates product card UI
- `setupBannerImage()` - Configures banner display

**Data Source:**
- Uses hardcoded sample data (can be replaced with DB query)
- Displays: Amoxicillin, Omega-3, Lisinopril, Metformin, Vitamin D3, Atorvastatin

**Navigation:** Through navbar to Products, About, Cart, Dashboard

---

#### **ProductsController.java** (Medicine Catalog)
**Functions:**
- `initialize()` - Sets up product grid and filters
- `initializeProductData()` - Loads all products
- `displayProducts()` - Renders products in grid layout
- `handleSearch()` - Filters products by search term
- `handleSort()` - Sorts by price/name
- `handleProductClick()` - Navigates to product details
- `createProductCard()` - Generates individual product UI

**Features:**
- Search functionality
- Sort by: Name, Price (Low-High, High-Low)
- Grid layout with responsive design
- Click to view details

**Database Operations:**
- **READ**: Queries `medicine` table for available products

---

#### **ProductDetailsController.java**
**Functions:**
- `initialize()` - Sets up product detail view
- `loadProductDetails()` - Displays product info
- `handleAddToCart()` - Adds item to shopping cart
- `handleQuantityChange()` - Updates quantity selection
- `showAddToCartMessage()` - Displays success feedback

**Database Operations:**
- **READ**: Fetches product details from `medicine`
- **WRITE**: Inserts into `cart` table

**UI Elements:**
- Product image, name, brand, category
- Price and availability status
- Quantity spinner (1-10)
- Description and usage instructions

---

#### **CartController.java** (Shopping Cart)
**Functions:**
- `initialize()` - Loads cart items for current user
- `loadCartItems()` - Queries cart from database
- `updateTotalAmount()` - Calculates cart total
- `handleRemoveItem()` - Removes item from cart
- `handleCheckout()` - Processes purchase
- `processCheckout()` - Transfers cart to orders
- `showErrorAlert()` / `showInfoAlert()` - User feedback

**Database Operations:**
- **READ**: JOIN between `cart` and `medicine` tables
```sql
SELECT c.*, m.product_name, m.brand, m.price 
FROM cart c 
JOIN medicine m ON c.medicine_id = m.medicine_id 
WHERE c.user_id = ?
```
- **WRITE**: Insert into `customer` and `customer_info`
- **DELETE**: Clear cart after checkout

**Transaction Flow:**
1. User reviews cart
2. Clicks checkout
3. System creates order records
4. Cart is cleared
5. Confirmation shown

---

#### **dashboardController.java** (Admin Panel)
**Functions:**

**Medicine Management:**
- `addMedicinesAdd()` - Adds new medicine to inventory
- `addMedicinesUpdate()` - Updates medicine details
- `addMedicinesDelete()` - Removes medicine
- `addMedicinesImportImage()` - Uploads product image
- `addMedicinesClear()` - Resets form fields
- `addMedicinesSearch()` - Searches inventory
- `addMedicinesSelect()` - Selects medicine for editing

**Purchase Management:**
- `purchaseAdd()` - Adds item to purchase list
- `purchasePay()` - Completes transaction
- `purchaseDisplayCart()` - Shows current purchase items
- `purchaseCalculateTotal()` - Computes total amount

**Dashboard Analytics:**
- `dashboardDisplayAvailableMedicines()` - Counts available stock
- `dashboardDisplayTotalCustomers()` - Shows customer count
- `dashboardDisplayTotalIncome()` - Calculates revenue
- `dashboardIncomeChart()` - Generates income graph

**Database Operations:**
- **CREATE**: New medicines, purchases
- **READ**: All tables for statistics
- **UPDATE**: Medicine details, prices, status
- **DELETE**: Remove medicines

**Real-time Updates:**
- TableView auto-refreshes after operations
- Chart updates with new transaction data

---

#### **NavbarController.java** (Navigation)
**Functions:**
- `initialize()` - Sets up navigation based on user role
- `navigateToHome()` - Loads home page
- `navigateToProducts()` - Loads products catalog
- `navigateToAbout()` - Loads about page
- `navigateToCart()` - Loads shopping cart
- `navigateToDashboard()` - Loads admin panel
- `navigateToChat()` - Opens chat window
- `handleLogin()` - Shows login form
- `handleLogout()` - Logs out user
- `updateNavbarBasedOnLoginStatus()` - Shows/hides buttons by role

**Role-Based Display:**
- **Guest**: Home, Products, About, Login, Signup
- **Customer**: + Cart, Chat, Logout
- **Admin/Pharmacist**: + Dashboard

---

#### **AboutController.java**
**Functions:**
- `initialize()` - Loads navbar
- Displays company information
- Shows contact details
- Presents team information

---

### 5. Networking & Real-Time Features

#### **ChatServerManager.java** (Server-Side)
**Purpose**: Manages chat server for admin-customer communication

**Functions:**
- `getInstance()` - Singleton pattern for single server instance
- `startServer()` - Initializes ServerSocket on port 8888
- `stopServer()` - Gracefully shuts down server
- `handleClientConnection()` - Manages individual client sockets
- `isRunning()` - Returns server status
- `getClientSockets()` - Returns list of connected clients

**Networking Details:**
- **Protocol**: TCP/IP Sockets
- **Port**: 8888
- **Architecture**: Multi-threaded server
- **Connection Type**: Persistent connections
- **Thread Model**: One thread per client

**Lifecycle:**
1. Server starts on application launch (if admin)
2. Listens for incoming connections
3. Accepts client socket
4. Spawns handler thread
5. Maintains connection list
6. Broadcasts/routes messages

---

#### **AdminChatController.java** (Admin Chat Interface)
**Purpose**: Admin-side chat interface for customer support

**Functions:**
- `initialize()` - Sets up chat UI
- `initializeWithClient(Socket)` - Connects to specific customer
- `startMessageReceiver()` - Listens for incoming messages
- `processMessage()` - Parses and displays messages
- `sendMessage()` - Sends message to customer
- `addUserMessage()` - Displays customer message
- `addAdminMessage()` - Displays admin message
- `addSystemMessage()` - Shows system notifications
- `setOnUserIdentified()` - Callback when user connects

**Message Protocol:**
- `USER:<username>` - User identification
- `MSG:<message>` - Regular chat message
- Format allows for protocol extension

**Real-Time Features:**
- Messages appear instantly
- Connection status indicators
- User identification display
- Timestamp on messages
- JavaFX Platform.runLater() for UI thread safety

---

#### **DataReceiver.java** (Interface)
**Purpose**: Contract for controllers that receive data during scene switching

**Method:**
- `receiveData(Object data)` - Receives data from previous scene

**Used By:** Controllers that need data from navigation
- Example: ProductDetailsController receives product info

---

#### **SceneSwitcher.java** (Navigation Utility)
**Purpose**: Handles scene transitions with data passing

**Functions:**
- `switchScene()` - Loads new FXML and passes data
- `switchSceneWithData()` - Overload with data parameter

**Features:**
- Validates FXML resources exist
- Passes data to DataReceiver controllers
- Handles scene sizing based on page type
- Error handling with user feedback

**Scene Sizes:**
- Login/Signup: 1000x700
- Main pages: 1400x800

---

## 🔄 Data Flow & Networking

### 1. User Authentication Flow
```
User Input → HelloController.handleLogin()
    ↓
database.connectDb()
    ↓
Query: SELECT * FROM user WHERE username=? AND password=?
    ↓
[If Found] → Store in getData.username
    ↓
[If Admin] → Navigate to dashboard.fxml
[If Customer] → Navigate to home.fxml
```

### 2. Product Browsing Flow
```
HomeController/ProductsController
    ↓
database.connectDb()
    ↓
Query: SELECT * FROM medicine WHERE status='Available'
    ↓
ObservableList<medicineData>
    ↓
TableView/GridPane Display
    ↓
[User Click] → SceneSwitcher.switchScene()
    ↓
ProductDetailsController.receiveData()
```

### 3. Shopping Cart Flow
```
ProductDetailsController.handleAddToCart()
    ↓
database.connectDb()
    ↓
Query: INSERT INTO cart (user_id, medicine_id, quantity)
    ↓
CartController.loadCartItems()
    ↓
Query: SELECT cart.*, medicine.* FROM cart JOIN medicine
    ↓
ObservableList<CartItem>
    ↓
TableView Display with Total Calculation
```

### 4. Checkout Flow
```
CartController.handleCheckout()
    ↓
Transaction BEGIN
    ↓
INSERT INTO customer (for each cart item)
    ↓
INSERT INTO customer_info (order summary)
    ↓
DELETE FROM cart WHERE user_id=?
    ↓
Transaction COMMIT
    ↓
Update Dashboard Statistics
```

### 5. Real-Time Chat Flow (Networking)

**Server-Side (Admin):**
```
ChatServerManager.startServer()
    ↓
ServerSocket.accept() on port 8888
    ↓
New Socket created per client
    ↓
AdminChatController.initializeWithClient(socket)
    ↓
BufferedReader reads incoming messages
    ↓
Platform.runLater() updates UI thread
    ↓
Display message in chat box
```

**Message Flow:**
```
Customer Client
    ↓ (TCP Socket Connection)
Server Socket (Port 8888)
    ↓
AdminChatController receives
    ↓
Parse message protocol:
  - USER:<name> → Identify customer
  - MSG:<text> → Display message
    ↓
Admin responds
    ↓ (TCP Socket)
Customer receives
```

**Thread Model:**
```
Main Thread (JavaFX UI)
    ↓
Server Thread (Listener)
    ↓
Client Handler Thread 1
Client Handler Thread 2
Client Handler Thread 3...
    ↓
Platform.runLater() → UI Thread
```

---

## ⚡ Real-Time Features

### 1. **Chat System (Socket-Based)**
- **Technology**: Java Socket Programming (TCP/IP)
- **Port**: 8888
- **Threading**: Multi-threaded server
- **UI Updates**: JavaFX Platform.runLater()

**How It Works:**
1. Admin starts chat server on dashboard load
2. Customer connects via chat button
3. Socket connection established
4. Messages sent through OutputStreams
5. Messages received via InputStreams
6. UI updated on JavaFX Application Thread

### 2. **Database Real-Time Updates**
- **Cart**: Refreshes on add/remove
- **Dashboard Statistics**: Recalculates on transaction
- **Inventory**: Updates immediately after admin changes
- **Purchase History**: Appends new orders instantly

**Implementation:**
- ObservableList auto-updates TableViews
- Manual refresh calls after DB operations
- Transaction listeners (implicit through CRUD operations)

### 3. **UI Responsiveness**
- **Scene Transitions**: Smooth FXML loading
- **Data Binding**: JavaFX Properties (SimpleStringProperty, etc.)
- **Event Handling**: ActionEvent listeners
- **Animations**: FadeTransition for messages

---

## 📚 Function Reference

### Database Functions (database.java)
| Function | Purpose | Returns |
|----------|---------|---------|
| `connectDb()` | Creates MySQL connection | Connection object |

### Authentication (HelloController.java)
| Function | Purpose | Database Operation |
|----------|---------|-------------------|
| `handleLogin()` | User authentication | SELECT from user |
| `handleSignup()` | New user registration | INSERT into user |

### Product Management (dashboardController.java)
| Function | Purpose | Database Operation |
|----------|---------|-------------------|
| `addMedicinesAdd()` | Add new medicine | INSERT into medicine |
| `addMedicinesUpdate()` | Update medicine | UPDATE medicine |
| `addMedicinesDelete()` | Remove medicine | DELETE from medicine |
| `addMedicinesShowListData()` | Load all medicines | SELECT * from medicine |

### Cart Operations (CartController.java)
| Function | Purpose | Database Operation |
|----------|---------|-------------------|
| `loadCartItems()` | Fetch cart items | SELECT cart JOIN medicine |
| `handleRemoveItem()` | Remove from cart | DELETE from cart |
| `processCheckout()` | Complete purchase | INSERT customer, DELETE cart |

### Chat Operations (ChatServerManager.java)
| Function | Purpose | Network Operation |
|----------|---------|------------------|
| `startServer()` | Start chat server | ServerSocket.bind(8888) |
| `handleClientConnection()` | Manage client | Socket I/O streams |
| `stopServer()` | Close server | Close all sockets |

### Navigation (SceneSwitcher.java)
| Function | Purpose | Action |
|----------|---------|--------|
| `switchScene()` | Change view | Load FXML, set Scene |
| Controllers implementing `DataReceiver` | Receive data | Populate UI with passed data |

---

## 🚀 Setup Instructions

### Prerequisites
- Java JDK 21
- Maven 3.8+
- MySQL 8.0
- IDE (IntelliJ IDEA recommended)

### Database Setup
1. Start MySQL on port 3307
2. Run the setup script:
```bash
mysql -u root -p -P 3307 < src/main/resources/database/complete_setup.sql
```
3. Verify tables created:
```sql
USE edispensary;
SHOW TABLES;
```

### Application Setup
1. Clone the repository
2. Open in IDE as Maven project
3. Update database credentials in `database.java` if needed
4. Build the project:
```bash
mvn clean install
```
5. Run the application:
```bash
mvn javafx:run
```

### Default Users
| Username | Password | Role |
|----------|----------|------|
| admin | admin123 | admin |
| pharmacist | pharma123 | pharmacist |
| user | user123 | customer |

---

## 📊 System Architecture Diagram

```
┌─────────────────────────────────────────────────────────┐
│                    JavaFX UI Layer                       │
├─────────────────────────────────────────────────────────┤
│  Home │ Products │ Cart │ Dashboard │ Chat │ About     │
└────┬──────────┬───────┬──────────┬───────┬─────────────┘
     │          │       │          │       │
     └──────────┴───────┴──────────┴───────┘
                     │
          ┌──────────┴───────────┐
          │   Controller Layer    │
          │  (Business Logic)     │
          └──────────┬───────────┘
                     │
          ┌──────────┴───────────┐
          │                      │
    ┌─────┴─────┐       ┌───────┴────────┐
    │  Database │       │  Chat Server   │
    │   (MySQL) │       │  (Port 8888)   │
    └───────────┘       └────────────────┘
         │                      │
    [JDBC Driver]          [Socket API]
         │                      │
    localhost:3307          TCP/IP
```

---

## 🔐 Security Considerations

⚠️ **Current Implementation** (Development):
- Plain text passwords (not hashed)
- No SQL injection protection
- No authentication tokens
- No HTTPS for future web deployment

✅ **Recommended for Production**:
- Use BCrypt for password hashing
- Implement PreparedStatements (already used)
- Add session management
- Use SSL/TLS for socket connections
- Input validation and sanitization

---

## 📝 License
This project is for educational purposes.

## 👥 Contributors
- Developer: iamshawn

---

**Version**: 1.0  
**Last Updated**: October 2025  
**Status**: Active Development

