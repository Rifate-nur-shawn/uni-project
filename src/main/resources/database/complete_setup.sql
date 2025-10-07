-- ====================================================
-- Complete Database Setup Script for e-Dispensary
-- ====================================================

-- Create database (if running directly in MySQL)
CREATE DATABASE IF NOT EXISTS edispensary;
USE edispensary;

-- ====================================================
-- Table: user
-- Stores user login credentials and roles
-- ====================================================
CREATE TABLE IF NOT EXISTS user (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(150),
    role VARCHAR(50) DEFAULT 'customer',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ====================================================
-- Table: medicine
-- Stores medicine/product information
-- ====================================================
CREATE TABLE IF NOT EXISTS medicine (
    medicine_id INT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(200) NOT NULL,
    brand VARCHAR(150) NOT NULL,
    type VARCHAR(100),
    status VARCHAR(50) DEFAULT 'Available',
    price DECIMAL(10, 2) NOT NULL,
    image VARCHAR(255),
    date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ====================================================
-- Table: cart
-- Stores shopping cart items
-- ====================================================
CREATE TABLE IF NOT EXISTS cart (
    cart_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(100) NOT NULL,
    medicine_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    date_added TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (medicine_id) REFERENCES medicine(medicine_id) ON DELETE CASCADE
);

-- ====================================================
-- Table: customer
-- Stores purchase history/orders
-- ====================================================
CREATE TABLE IF NOT EXISTS customer (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_username VARCHAR(100),
    type VARCHAR(100),
    medicine_id INT,
    brand VARCHAR(150),
    productName VARCHAR(200),
    quantity INT,
    price DECIMAL(10, 2),
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (medicine_id) REFERENCES medicine(medicine_id) ON DELETE SET NULL
);

-- ====================================================
-- Insert Default Admin User
-- ====================================================
INSERT INTO user (username, password, email, role) VALUES
('admin', 'admin123', 'admin@edispensary.com', 'admin'),
('pharmacist', 'pharma123', 'pharmacist@edispensary.com', 'pharmacist'),
('user', 'user123', 'user@edispensary.com', 'customer')
ON DUPLICATE KEY UPDATE username=username;

-- ====================================================
-- Insert Sample Medicine Data
-- ====================================================
INSERT INTO medicine (product_name, brand, type, status, price, image, date) VALUES
('Amoxicillin 500mg', 'Generic Pharma', 'Antibiotic', 'Available', 18.99, '/images/m.png', CURDATE()),
('Paracetamol 500mg', 'Tylenol', 'Pain Relief', 'Available', 5.99, '/images/addmed.png', CURDATE()),
('Ibuprofen 400mg', 'Advil', 'Pain Relief', 'Available', 8.50, '/images/purchaseMedi.png', CURDATE()),
('Omega-3 Fish Oil', 'Nature Made', 'Supplement', 'Available', 24.99, '/images/addmed.png', CURDATE()),
('Vitamin D3 1000 IU', 'Nature Made', 'Supplement', 'Available', 15.99, '/images/doctor.png', CURDATE()),
('Lisinopril 10mg', 'Zestril', 'Blood Pressure', 'Available', 12.50, '/images/purchaseMedi.png', CURDATE()),
('Metformin 500mg', 'Glucophage', 'Diabetes', 'Available', 22.99, '/images/m.png', CURDATE()),
('Aspirin 81mg', 'Bayer', 'Blood Thinner', 'Available', 6.99, '/images/stetho.png', CURDATE()),
('Omeprazole 20mg', 'Prilosec', 'Acid Reducer', 'Available', 19.99, '/images/okk.png', CURDATE()),
('Cetirizine 10mg', 'Zyrtec', 'Allergy', 'Available', 11.50, '/images/addmed.png', CURDATE())
ON DUPLICATE KEY UPDATE product_name=product_name;

-- ====================================================
-- Display Success Message
-- ====================================================
SELECT 'Database setup completed successfully!' AS Status;
SELECT COUNT(*) AS 'Total Users' FROM user;
SELECT COUNT(*) AS 'Total Medicines' FROM medicine;

