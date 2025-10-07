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
    id INT AUTO_INCREMENT PRIMARY KEY,
    medicine_id INT UNIQUE NOT NULL,
    productName VARCHAR(200) NOT NULL,
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
    date_added TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ====================================================
-- Table: customer
-- Stores purchase line items
-- ====================================================
CREATE TABLE IF NOT EXISTS customer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    type VARCHAR(100),
    medicine_id INT,
    brand VARCHAR(150),
    productName VARCHAR(200),
    quantity INT,
    price DECIMAL(10, 2),
    date DATE
);

-- ====================================================
-- Table: customer_info
-- Stores purchase summaries/invoices
-- ====================================================
CREATE TABLE IF NOT EXISTS customer_info (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    total DECIMAL(10, 2) NOT NULL,
    date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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
INSERT INTO medicine (medicine_id, productName, brand, type, status, price, image, date) VALUES
(1, 'Amoxicillin 500mg', 'Generic Pharma', 'Antibiotics', 'Available', 18.99, '/images/m.png', CURDATE()),
(2, 'Paracetamol 500mg', 'Tylenol', 'Antibiotics', 'Available', 5.99, '/images/addmed.png', CURDATE()),
(3, 'Ibuprofen 400mg', 'Advil', 'Hydrocodone', 'Available', 8.50, '/images/purchaseMedi.png', CURDATE()),
(4, 'Omega-3 Fish Oil', 'Nature Made', 'Metformin', 'Available', 24.99, '/images/addmed.png', CURDATE()),
(5, 'Vitamin D3 1000 IU', 'Nature Made', 'Losartan', 'Available', 15.99, '/images/doctor.png', CURDATE()),
(6, 'Lisinopril 10mg', 'Zestril', 'Losartan', 'Available', 12.50, '/images/purchaseMedi.png', CURDATE()),
(7, 'Metformin 500mg', 'Glucophage', 'Metformin', 'Available', 22.99, '/images/m.png', CURDATE()),
(8, 'Aspirin 81mg', 'Bayer', 'Albuterol', 'Available', 6.99, '/images/stetho.png', CURDATE()),
(9, 'Omeprazole 20mg', 'Prilosec', 'Hydrocodone', 'Available', 19.99, '/images/okk.png', CURDATE()),
(10, 'Cetirizine 10mg', 'Zyrtec', 'Albuterol', 'Available', 11.50, '/images/addmed.png', CURDATE()),
(11, 'Multivitamin', 'HealthCare Plus', 'Metformin', 'Available', 29.99, '/images/m.png', CURDATE())
ON DUPLICATE KEY UPDATE productName=productName;

-- ====================================================
-- Display Success Message
-- ====================================================
SELECT 'Database setup completed successfully!' AS Status;
SELECT COUNT(*) AS 'Total Users' FROM user;
SELECT COUNT(*) AS 'Total Medicines' FROM medicine;
