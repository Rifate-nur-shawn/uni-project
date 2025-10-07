# e-Dispensary - Pharmacy Management System

A JavaFX-based pharmacy management system with database integration for managing medicines, customers, and orders.

## 📋 What Has Been Set Up

### 1. **Database Connection**
- MySQL JDBC connector has been added to `pom.xml`
- Database connection class (`database.java`) is ready to connect to MySQL
- Default credentials: `root` / `shawn12` for database `edispensary`

### 2. **Project Structure**
```
uni-project/
├── src/main/
│   ├── java/com/iamshawn/uniproject/
│   │   ├── HelloApplication.java          # Main application entry point
│   │   ├── database.java                  # Database connection handler
│   │   ├── CartController.java            # Shopping cart functionality (NEW)
│   │   ├── CartItem.java                  # Cart item data model
│   │   ├── medicineData.java              # Medicine data model (NEW)
│   │   ├── customerData.java              # Customer data model
│   │   ├── ChatServerManager.java         # Chat server manager (NEW)
│   │   ├── HomeController.java            # Home page controller
│   │   ├── ProductsController.java        # Products page controller
│   │   ├── NavbarController.java          # Navigation bar controller
│   │   ├── AboutController.java           # About page controller
│   │   ├── ProductDetailsController.java  # Product details controller
│   │   ├── dashboardController.java       # Admin dashboard controller
│   │   └── AdminChatController.java       # Admin chat controller
│   │
│   └── resources/com/iamshawn/uniproject/
│       ├── *.fxml                         # All FXML view files
│       ├── cart.fxml                      # Shopping cart page (NEW)
│       └── database/
│           ├── setup_database.sql         # Database setup script (NEW)
│           └── cart_table.sql             # Cart table schema
│
├── pom.xml                                # Maven configuration with MySQL dependency
├── DATABASE_SETUP.md                      # Database setup guide (NEW)
└── README.md                             # This file
```

### 3. **New/Updated Files**
- ✅ **CartController.java** - Fully implemented with cart management features
- ✅ **cart.fxml** - Shopping cart UI
- ✅ **medicineData.java** - Data model for medicines
- ✅ **ChatServerManager.java** - Chat server functionality
- ✅ **setup_database.sql** - Complete database setup script
- ✅ **DATABASE_SETUP.md** - Step-by-step database setup guide

## 🚀 How to Run the Project

### Step 1: Setup MySQL Database

1. **Start MySQL Server**
   ```bash
   sudo systemctl start mysql
   ```

2. **Login to MySQL**
   ```bash
   mysql -u root -p
   ```

3. **Run the Setup Script**
   ```sql
   source /home/shawn/Documents/My-Project/uni-project/src/main/resources/database/setup_database.sql
   ```
   
   Or manually:
   ```bash
   mysql -u root -p < src/main/resources/database/setup_database.sql
   ```

4. **Verify Database**
   ```sql
   USE edispensary;
   SHOW TABLES;
   SELECT * FROM medicine;
   ```

### Step 2: Configure Database Connection (if needed)

If you need to change database credentials, edit `src/main/java/com/iamshawn/uniproject/database.java`:

```java
Connection connect = DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/edispensary",
    "your_username",    // Change this
    "your_password"     // Change this
);
```

### Step 3: Build and Run the Project

```bash
# Navigate to project directory
cd /home/shawn/Documents/My-Project/uni-project

# Clean and compile
mvn clean compile

# Run the application
mvn javafx:run
```

## 📊 Database Schema

### Tables Created:
1. **user** - User authentication
2. **medicine** - Medicine/product catalog
3. **cart** - Shopping cart items
4. **customer** - Order/purchase history
5. **customer_info** - Customer transaction summary

### Sample Data Included:
- 10 sample medicines
- 2 test users:
  - **Admin**: username: `admin`, password: `admin123`
  - **Guest**: username: `guest`, password: `guest123`

## 🎯 Features

### User Features:
- Browse medicines/products
- Search and filter products
- Add items to cart
- Checkout and place orders
- View product details

### Admin Features:
- Dashboard with analytics
- Add/Update/Delete medicines
- View customer orders
- Manage inventory
- Chat support system

## 🔧 Project Configuration

### Dependencies Added:
- JavaFX Controls, FXML, Graphics, Base
- MySQL Connector/J 8.2.0
- JUnit 5 (for testing)

### Java Version: 21
### Maven Build Tool

## 📝 Important Notes

1. **Database Connection**: Ensure MySQL is running before starting the application
2. **Port 3306**: MySQL default port must be available
3. **First Run**: Make sure to run the database setup script first
4. **Images**: Product images are stored in `src/main/resources/images/`

## 🐛 Troubleshooting

### Connection Failed
- Check if MySQL service is running: `sudo systemctl status mysql`
- Verify credentials in `database.java`
- Ensure database `edispensary` exists

### Compilation Errors
- Run `mvn clean install` to refresh dependencies
- Check Java version: `java -version` (should be 21)

### Application Won't Start
- Verify JavaFX installation
- Check console output for detailed error messages
- Ensure all FXML files are in correct locations

## 📚 Next Steps

1. **Test the application**:
   ```bash
   mvn javafx:run
   ```

2. **Login with test account**:
   - Username: `guest`
   - Password: `guest123`

3. **Explore features**:
   - Browse products
   - Add items to cart
   - Test checkout process

4. **Admin Dashboard** (use admin account):
   - Username: `admin`
   - Password: `admin123`

## 🎨 Customization

### Change Database Credentials
Edit `database.java` line 11-13

### Add More Products
Insert data into `medicine` table or use admin dashboard

### Modify UI
Edit corresponding `.fxml` files in resources folder

## ✅ Project Status

- ✅ Project compiles successfully
- ✅ All controllers implemented
- ✅ Database connection configured
- ✅ FXML files properly linked
- ✅ Cart functionality complete
- ✅ Sample data included
- ✅ Ready to run!

## 🤝 Support

For database setup details, see `DATABASE_SETUP.md`

---

**Author**: Shawn  
**Project**: e-Dispensary Pharmacy Management System  
**Version**: 1.0-SNAPSHOT

