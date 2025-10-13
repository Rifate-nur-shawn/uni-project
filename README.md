# e-Dispensary - Pharmacy Management System

A comprehensive JavaFX-based pharmacy management system with real-time customer support chat functionality.

## 📋 Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Architecture](#architecture)
- [Database Setup](#database-setup)
- [How to Run](#how-to-run)
- [Project Structure](#project-structure)
- [Key Components](#key-components)
- [Real-time Chat System](#real-time-chat-system)
- [Admin Dashboard](#admin-dashboard)
- [Networking](#networking)

---

## 🎯 Overview

e-Dispensary is a full-stack pharmacy management system that allows:
- **Customers** to browse medicines, add items to cart, and chat with admin support
- **Administrators** to manage inventory, process orders, view analytics, and provide real-time customer support

Built with **JavaFX** for the UI and **MySQL** for data persistence, featuring a custom socket-based real-time chat system.

---

## ✨ Features

### Customer Features
- 🏠 **Home Page**: Browse available medicines
- 🛒 **Shopping Cart**: Add/remove items, calculate totals
- 💬 **Live Chat Support**: Real-time communication with admin
- 📦 **Product Details**: View medicine information
- 🔍 **Search & Filter**: Find medicines by name, brand, or type

### Admin Features
- 📊 **Dashboard Analytics**: 
  - Total customers count
  - Total income visualization
  - Available medicines count
  - Revenue charts
- 💊 **Medicine Management**:
  - Add new medicines
  - Update medicine details (price, brand, status, type)
  - Delete medicines
  - Upload medicine images
  - Search and filter medicines
- 💰 **Purchase Processing**:
  - Process customer orders
  - Calculate totals with quantities
  - Generate receipts
- 💬 **Customer Support Chat**:
  - Real-time chat with customers
  - See connected customers
  - Handle multiple chat sessions
- 👥 **User Management**: View and manage customer accounts

---

## 🏗️ Architecture

### Technology Stack
- **Frontend**: JavaFX 17+ with FXML
- **Backend**: Java 11+
- **Database**: MySQL 8.x
- **Networking**: Java Socket Programming (TCP/IP)
- **Build Tool**: Maven

### Design Patterns Used
1. **Singleton Pattern**: `ChatServerManager` - ensures only one server instance
2. **MVC Pattern**: Separation of FXML views, controllers, and data models
3. **Observer Pattern**: Real-time chat updates using callbacks
4. **DAO Pattern**: `database.java` handles all database operations

---

## 🗄️ Database Setup

### Prerequisites
- MySQL Server 8.x or higher
- MySQL Connector/J 8.3.0 (included in `lib/` folder)

### Setup Steps

1. **Create Database**:
```bash
mysql -u root -p
```

2. **Run Setup Script**:
```sql
source src/main/resources/database/complete_setup.sql
```

Or manually:
```sql
CREATE DATABASE pharmacy_db;
USE pharmacy_db;
```

3. **Configure Connection**:
Update `database.java` with your MySQL credentials:
```java
private static final String URL = "jdbc:mysql://localhost:3306/pharmacy_db";
private static final String USER = "root";
private static final String PASSWORD = "your_password";
```

---

## 🚀 How to Run

### Method 1: Using Maven
```bash
# Clean and compile
mvn clean compile

# Run the application
mvn javafx:run
```

### Method 2: Using IDE (IntelliJ IDEA / Eclipse)
1. Import project as Maven project
2. Let Maven download dependencies
3. Run `HelloApplication.java` main method

### Method 3: Using Maven Wrapper (Linux/Mac)
```bash
./mvnw clean javafx:run
```

### Method 4: Using Maven Wrapper (Windows)
```cmd
mvnw.cmd clean javafx:run
```

---

## 📁 Project Structure

```
uni-project/
├── src/
│   ├── main/
│   │   ├── java/com/iamshawn/uniproject/
│   │   │   ├── HelloApplication.java          # Main entry point
│   │   │   ├── HelloController.java           # Login controller
│   │   │   ├── HomeController.java            # Customer home page
│   │   │   ├── dashboardController.java       # Admin dashboard
│   │   │   ├── CartController.java            # Shopping cart
│   │   │   ├── ChatController.java            # Customer chat UI
│   │   │   ├── AdminChatController.java       # Admin chat UI
│   │   │   ├── ChatServerManager.java         # Chat server (Singleton)
│   │   │   ├── ProductsController.java        # Product listing
│   │   │   ├── ProductDetailsController.java  # Product details
│   │   │   ├── database.java                  # Database connections
│   │   │   ├── getData.java                   # Global data store
│   │   │   ├── medicineData.java              # Medicine model
│   │   │   ├── customerData.java              # Customer model
│   │   │   ├── CartItem.java                  # Cart item model
│   │   │   ├── NavbarController.java          # Navigation bar
│   │   │   ├── SceneSwitcher.java             # Scene navigation utility
│   │   │   └── DataReceiver.java              # Data transfer objects
│   │   └── resources/com/iamshawn/uniproject/
│   │       ├── *.fxml                         # UI layout files
│   │       ├── database/
│   │       │   └── complete_setup.sql         # Database schema
│   │       └── images/                        # UI assets
├── lib/
│   └── mysql-connector-j-8.3.0.jar           # MySQL driver
├── pom.xml                                    # Maven configuration
└── README.md                                  # This file
```

---

## 🔑 Key Components

### 1. **HelloApplication.java**
- **Purpose**: Main entry point of the application
- **Function**: Initializes JavaFX stage and loads home.fxml
- **Features**: 
  - Makes window draggable
  - Sets application title and dimensions (1400x800)

### 2. **HelloController.java**
- **Purpose**: Handles user login/registration
- **Functions**:
  - `loginAccount()`: Authenticates users
  - `registerAccount()`: Creates new user accounts
  - Validates credentials against database
  - Sets user session data in `getData.username` and `getData.isAdmin`

### 3. **HomeController.java**
- **Purpose**: Customer home page with product browsing
- **Functions**:
  - `loadMedicines()`: Fetches available medicines from database
  - `displayMedicines()`: Shows medicines in grid view
  - Navigation to cart, products, chat
- **Features**: Dynamic product cards with images, prices, and "Add to Cart" buttons

### 4. **dashboardController.java** (Admin Dashboard)
This is the core admin interface with multiple sections:

#### Dashboard Analytics Section
- `homeChart()`: Displays income chart using AreaChart
- `homeAM()`: Shows total available medicines count
- `homeTI()`: Calculates and displays total income
- `homeTC()`: Shows total customers count
- **Real-time updates**: Data refreshes when page loads

#### Medicine Management Section
- `addMedicinesAdd()`: Adds new medicine to inventory
  - Validates: medicine ID, name, brand, type, status, price
  - Uploads image to database
- `addMedicineUpdate()`: Updates existing medicine details
- `addMedicineDelete()`: Removes medicine from inventory
- `addMedicineReset()`: Clears form fields
- `addMedicineImportImage()`: Opens file chooser for medicine images
- `addMedicineShowListData()`: Displays all medicines in TableView
- `addMedicineSearch()`: Filters medicines based on search input
- `addMedicineSelect()`: Populates form when medicine is selected from table

#### Purchase Processing Section
- `purchaseAdd()`: Adds medicine to purchase list
- `purchaseDisplayTotal()`: Calculates total amount
- `purchaseAmount()`: Handles amount input
- `purchasePay()`: Processes payment and saves to database
- `purchaseShowValue()`: Displays purchase items in table
- `purchaseQuantity()`: Updates quantity for selected item

### 5. **CartController.java**
- **Purpose**: Manages shopping cart
- **Functions**:
  - `loadCartItems()`: Loads items from session/database
  - `calculateTotal()`: Sums up cart item prices
  - `handleRemoveItem()`: Removes selected item
  - `handleCheckout()`: Processes order

### 6. **ProductsController.java**
- **Purpose**: Displays all available products
- **Functions**:
  - `loadProducts()`: Fetches products from database
  - `filterByType()`: Filters products by medicine type
  - `searchProducts()`: Searches by name/brand

### 7. **ProductDetailsController.java**
- **Purpose**: Shows detailed product information
- **Functions**:
  - `loadProductDetails()`: Displays selected product
  - `addToCart()`: Adds product to cart with quantity

---

## 💬 Real-time Chat System

The chat system uses **Java Socket Programming** for real-time TCP/IP communication.

### Architecture

```
Customer (ChatController) <--TCP/IP--> ChatServerManager <---> Admin (AdminChatController)
                                            |
                                      Port: 8888
                                      Singleton Server
```

### Components

#### 1. **ChatServerManager.java** (Server)
- **Pattern**: Singleton
- **Port**: 8888
- **Functions**:
  - `getInstance()`: Returns singleton instance
  - `startServer()`: Starts TCP server socket
  - `stopServer()`: Closes all connections
  - `setOnNewClientCallback()`: Registers callback for new connections
  - Inner class `ClientHandler`: Manages individual client connections

**How it works**:
1. Starts ServerSocket on port 8888
2. Accepts incoming client connections
3. Creates a `ClientHandler` thread for each client
4. Routes messages between customer and admin
5. Maintains map of connected clients: `Map<String, ClientHandler>`

#### 2. **ChatController.java** (Customer Client)
- **Purpose**: Customer-side chat interface
- **Functions**:
  - `connectToServer()`: Establishes socket connection to localhost:8888
  - `startMessageReceiver()`: Listens for incoming messages in background thread
  - `sendMessage()`: Sends messages with "MSG:" prefix
  - `addUserMessage()`: Displays sent messages (right-aligned, green)
  - `addAdminMessage()`: Displays received messages (left-aligned, purple)
  - `addSystemMessage()`: Shows connection status (centered, gray)

**Connection Flow**:
1. Initialize → Load navbar
2. Auto-connect to server on localhost:8888
3. Send "USER:{username}" for identification
4. Start background thread to receive messages
5. Display connection status in UI

**Message Protocol**:
- `USER:username` - Identifies the customer
- `MSG:message` - Regular chat message
- `ADMIN:message` - Message from admin (received)

#### 3. **AdminChatController.java** (Admin Server Interface)
- **Purpose**: Admin-side chat management
- **Functions**:
  - `initialize()`: Starts ChatServerManager if not running
  - `initializeWithClient()`: Sets up connection with specific customer
  - `startMessageReceiver()`: Listens for customer messages
  - `sendMessage()`: Sends replies with "ADMIN:" prefix
  - `processMessage()`: Handles incoming messages
  - `addUserMessage()`: Displays customer messages (left-aligned, blue)
  - `addAdminMessage()`: Displays admin messages (right-aligned, green)

**Server Lifecycle**:
1. Admin opens chat interface → Server starts automatically
2. Server listens on port 8888
3. Customer connects → Callback triggered
4. `initializeWithClient()` called with socket and username
5. Two-way communication established
6. On disconnect → Server continues running for next customer

### Real-time Message Flow

```
Customer Types Message:
1. User enters text → clicks Send
2. ChatController.sendMessage() called
3. Sends "MSG:Hello" to server via socket
4. Message appears in customer's chat (green, right)

Server Receives:
5. ChatServerManager.ClientHandler receives "MSG:Hello"
6. Identifies sender by username
7. Logs message

Admin Receives:
8. AdminChatController.processMessage() triggered
9. Parses "MSG:Hello" → extracts "Hello"
10. addUserMessage("Hello") → displays in admin chat (blue, left)

Admin Replies:
11. Admin types response → clicks Send
12. AdminChatController.sendMessage() called
13. Sends "ADMIN:Hi there!" to customer socket
14. Message appears in admin's chat (green, right)

Customer Receives:
15. ChatController.startMessageReceiver() receives "ADMIN:Hi there!"
16. Parses and extracts "Hi there!"
17. addAdminMessage("Hi there!") → displays (purple, left)
```

### Threading Model
- **Main Thread**: UI rendering (JavaFX Application Thread)
- **Server Thread**: Accepts connections (daemon thread)
- **ClientHandler Threads**: One per customer (daemon threads)
- **Receiver Threads**: One per chat window to listen for messages
- **Platform.runLater()**: Updates UI from background threads safely

---

## 🎛️ Admin Dashboard

The admin dashboard (`dashboard.fxml` + `dashboardController.java`) is the central control panel.

### Layout Structure

```
┌─────────────────────────────────────┐
│  Header Bar (Logo, Close, Minimize) │
├────────┬────────────────────────────┤
│  Side  │  Main Content Area         │
│  Nav   │                            │
│  Bar   │  ┌──────────────────────┐  │
│        │  │  Selected Section    │  │
│ • Home │  │  (Home/Medicine/     │  │
│ • Meds │  │   Purchase/Chat)     │  │
│ • Buy  │  └──────────────────────┘  │
│ • Chat │                            │
│ • Users│                            │
│ • Exit │                            │
└────────┴────────────────────────────┘
```

### Sections & Their Functions

#### 1. **Home Section** (Analytics Dashboard)
Shows 4 key metrics:
- **Total Customers**: Counts registered users
- **Total Income**: Sums all completed purchases
- **Available Medicines**: Counts medicines with status="Available"
- **Income Chart**: 7-day revenue visualization using AreaChart

**Data Flow**:
```
Initialize → homeDisplayTC() → Query: SELECT COUNT(*) FROM customers
          → homeDisplayTI() → Query: SELECT SUM(total) FROM receipts
          → homeDisplayAM() → Query: SELECT COUNT(*) WHERE status='Available'
          → homeChart()     → Query: SELECT date, SUM(total) GROUP BY date
```

#### 2. **Medicines Section** (Inventory Management)
**UI Elements**:
- Form fields: Medicine ID, Name, Brand, Type, Status, Price
- Image preview with import button
- TableView showing all medicines
- Search bar with live filtering
- Action buttons: Add, Update, Delete, Clear

**Operations**:
- **Add Medicine**:
  1. Fill form fields
  2. Import image (optional)
  3. Click Add → Validates inputs
  4. Inserts into `medicines` table
  5. Refreshes table view
  
- **Update Medicine**:
  1. Select medicine from table
  2. Form auto-fills with data
  3. Modify fields
  4. Click Update → Updates database
  
- **Delete Medicine**:
  1. Select medicine from table
  2. Click Delete → Confirmation dialog
  3. Removes from database
  
- **Search**:
  - Live filtering using FilteredList and SortedList
  - Searches across ID, name, brand fields

#### 3. **Purchase Section** (Order Processing)
**UI Elements**:
- Dropdown: Select medicine
- Spinner: Quantity selection
- Table: Current purchase items
- Total display
- Amount input field
- Balance calculation
- Pay button

**Purchase Flow**:
```
1. Select medicine → Loads available medicines
2. Choose quantity → Validates stock
3. Click Add → Adds to purchase list
4. Repeat for multiple items
5. Total auto-calculates
6. Enter amount paid
7. Balance = Amount - Total
8. Click Pay → Saves to receipts table
9. Updates inventory (reduces stock)
10. Clears purchase list
```

#### 4. **Chat Section**
Loads `admin_chat.fxml` which shows:
- Connected user name
- Chat history (scrollable)
- Message input field
- Send button
- Server status indicator

See [Real-time Chat System](#real-time-chat-system) for details.

#### 5. **Users Section**
Displays customer accounts in TableView:
- Customer ID
- Username
- Email
- Registration date
- Status (Active/Inactive)

---

## 🌐 Networking

### Socket Communication Details

#### Server (ChatServerManager)
```java
ServerSocket serverSocket = new ServerSocket(8888);  // Bind to port 8888
while (running) {
    Socket clientSocket = serverSocket.accept();     // Block until connection
    ClientHandler handler = new ClientHandler(clientSocket);
    handler.start();                                 // Handle in new thread
}
```

#### Client (ChatController)
```java
Socket socket = new Socket("localhost", 8888);       // Connect to server
PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

// Send message
out.println("MSG:Hello");

// Receive messages
String message = in.readLine();
```

### Protocol Specification

| Prefix | Direction | Purpose | Example |
|--------|-----------|---------|---------|
| `USER:` | Client → Server | Identify customer | `USER:john_doe` |
| `MSG:` | Client → Server | Send message | `MSG:I need help` |
| `ADMIN:` | Server → Client | Admin reply | `ADMIN:How can I help?` |

### Error Handling
- **Connection Failed**: Shows "Server not available" message
- **Disconnection**: Automatically detected, status updated
- **Server Down**: Client shows connection error, can retry
- **Socket Timeout**: Not implemented (could add `socket.setSoTimeout()`)

### Thread Safety
- **Concurrent HashMap**: Stores connected clients safely
- **Platform.runLater()**: All UI updates from background threads
- **Synchronized**: Not needed due to single-threaded client handling
- **Daemon Threads**: All network threads are daemons (app can close cleanly)

---

## 🔐 Security Considerations

**Current Implementation** (Educational):
- Passwords stored in plain text
- No input sanitization
- No SQL injection prevention
- No HTTPS/SSL for chat

**Production Recommendations**:
1. Hash passwords (BCrypt/Argon2)
2. Use PreparedStatements for all queries
3. Implement SSL/TLS for socket communication
4. Add authentication tokens
5. Rate limiting for chat
6. Input validation and sanitization

---

## 📊 Database Schema

### Tables

#### `users`
```sql
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    is_admin BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### `medicines`
```sql
CREATE TABLE medicines (
    medicine_id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    brand VARCHAR(100),
    type VARCHAR(50),
    status VARCHAR(20),
    price DECIMAL(10,2),
    image BLOB,
    date_added DATE
);
```

#### `receipts`
```sql
CREATE TABLE receipts (
    receipt_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT,
    medicine_id VARCHAR(50),
    quantity INT,
    total DECIMAL(10,2),
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES users(id),
    FOREIGN KEY (medicine_id) REFERENCES medicines(medicine_id)
);
```

---

## 🐛 Troubleshooting

### Chat Not Connecting
1. Check if admin has opened chat interface (server must be running)
2. Verify port 8888 is not blocked by firewall
3. Check console for server startup messages

### Database Connection Failed
1. Verify MySQL service is running
2. Check credentials in `database.java`
3. Ensure database exists and tables are created

### Images Not Displaying
1. Check image path in FXML files
2. Verify images exist in `src/main/resources/images/`
3. Rebuild project: `mvn clean compile`

---

## 📝 Future Enhancements

- [ ] Multi-admin chat support (broadcast messages)
- [ ] Chat history persistence in database
- [ ] File sharing in chat (prescriptions, images)
- [ ] Push notifications for new messages
- [ ] Mobile app integration via REST API
- [ ] Prescription verification system
- [ ] Inventory low-stock alerts
- [ ] Automated reports generation
- [ ] Payment gateway integration
- [ ] Order tracking system

---

## 👨‍💻 Development

### Building
```bash
mvn clean package
```

### Testing
```bash
mvn test
```

### Creating Executable JAR
```bash
mvn clean package
java -jar target/uni-project-1.0-SNAPSHOT.jar
```

---

## 📄 License

This is an educational project. Use at your own discretion.

---

## 🤝 Contributing

This is a university project. For issues or suggestions, please open an issue on the repository.

---

## 📧 Contact

For questions about this project, please refer to the project documentation or open an issue.

---

**Built with ❤️ using JavaFX and MySQL**

