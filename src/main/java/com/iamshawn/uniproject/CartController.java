package com.iamshawn.uniproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.ResourceBundle;

public class CartController implements Initializable {

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private TableView<CartItem> cartTableView;

    @FXML
    private TableColumn<CartItem, String> colProductName;

    @FXML
    private TableColumn<CartItem, String> colBrand;

    @FXML
    private TableColumn<CartItem, Double> colPrice;

    @FXML
    private TableColumn<CartItem, Integer> colQuantity;

    @FXML
    private TableColumn<CartItem, Double> colTotal;

    @FXML
    private Label totalAmountLabel;

    @FXML
    private Button checkoutButton;

    @FXML
    private Button removeButton;

    private ObservableList<CartItem> cartItems = FXCollections.observableArrayList();
    private String currentUsername = "guest"; // This should be set from login session

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Load navbar
        loadNavbar();

        // Setup table columns
        setupTableColumns();

        // Load cart items from database
        loadCartItems();

        // Update total amount
        updateTotalAmount();
    }

    private void loadNavbar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("navbar.fxml"));
            Parent navbar = loader.load();
            mainBorderPane.setTop(navbar);
        } catch (IOException e) {
            System.err.println("Error loading navbar: " + e.getMessage());
        }
    }

    private void setupTableColumns() {
        colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        cartTableView.setItems(cartItems);
    }

    private void loadCartItems() {
        cartItems.clear();

        String sql = "SELECT c.cart_id, c.user_id, c.medicine_id, c.quantity, " +
                     "m.product_name, m.brand, m.price, m.image " +
                     "FROM cart c " +
                     "JOIN medicine m ON c.medicine_id = m.medicine_id " +
                     "WHERE c.user_id = ?";

        try (Connection connect = database.connectDb();
             PreparedStatement prepare = connect.prepareStatement(sql)) {

            prepare.setString(1, currentUsername);
            ResultSet result = prepare.executeQuery();

            while (result.next()) {
                CartItem item = new CartItem(
                    result.getInt("cart_id"),
                    result.getString("user_id"),
                    result.getInt("medicine_id"),
                    result.getString("product_name"),
                    result.getString("brand"),
                    result.getDouble("price"),
                    result.getInt("quantity"),
                    result.getString("image")
                );
                cartItems.add(item);
            }

        } catch (Exception e) {
            showErrorAlert("Database Error", "Failed to load cart items: " + e.getMessage());
        }
    }

    private void updateTotalAmount() {
        double total = 0.0;
        for (CartItem item : cartItems) {
            total += item.getTotal();
        }
        totalAmountLabel.setText(String.format("$%.2f", total));
    }

    @FXML
    private void handleRemoveItem(ActionEvent event) {
        CartItem selectedItem = cartTableView.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            showWarningAlert("No Selection", "Please select an item to remove.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Remove Item");
        confirmAlert.setHeaderText("Remove from Cart");
        confirmAlert.setContentText("Are you sure you want to remove this item from cart?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            removeItemFromDatabase(selectedItem.getCartId());
            cartItems.remove(selectedItem);
            updateTotalAmount();
            showInfoAlert("Success", "Item removed from cart.");
        }
    }

    private void removeItemFromDatabase(int cartId) {
        String sql = "DELETE FROM cart WHERE cart_id = ?";

        try (Connection connect = database.connectDb();
             PreparedStatement prepare = connect.prepareStatement(sql)) {

            prepare.setInt(1, cartId);
            prepare.executeUpdate();

        } catch (Exception e) {
            showErrorAlert("Database Error", "Failed to remove item: " + e.getMessage());
        }
    }

    @FXML
    private void handleCheckout(ActionEvent event) {
        if (cartItems.isEmpty()) {
            showWarningAlert("Empty Cart", "Your cart is empty. Add items before checkout.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Checkout");
        confirmAlert.setHeaderText("Proceed to Checkout");
        confirmAlert.setContentText("Total Amount: " + totalAmountLabel.getText() + "\n\nProceed with purchase?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            processCheckout();
        }
    }

    private void processCheckout() {
        // Process order and clear cart
        String insertOrderSql = "INSERT INTO customer (type, medicine_id, brand, productName, quantity, price, date) " +
                               "VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
        String deleteCartSql = "DELETE FROM cart WHERE user_id = ?";

        try (Connection connect = database.connectDb()) {
            connect.setAutoCommit(false);

            try (PreparedStatement prepare = connect.prepareStatement(insertOrderSql)) {
                for (CartItem item : cartItems) {
                    prepare.setString(1, "Purchase");
                    prepare.setInt(2, item.getMedicineId());
                    prepare.setString(3, item.getBrand());
                    prepare.setString(4, item.getProductName());
                    prepare.setInt(5, item.getQuantity());
                    prepare.setDouble(6, item.getPrice());
                    prepare.addBatch();
                }
                prepare.executeBatch();
            }

            try (PreparedStatement prepare = connect.prepareStatement(deleteCartSql)) {
                prepare.setString(1, currentUsername);
                prepare.executeUpdate();
            }

            connect.commit();
            cartItems.clear();
            updateTotalAmount();

            showInfoAlert("Success", "Order placed successfully! Thank you for your purchase.");

        } catch (Exception e) {
            showErrorAlert("Checkout Error", "Failed to process checkout: " + e.getMessage());
        }
    }

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showWarningAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setCurrentUsername(String username) {
        this.currentUsername = username;
        loadCartItems();
        updateTotalAmount();
    }
}
