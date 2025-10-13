package com.iamshawn.uniproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class dashboardController implements Initializable {

    @FXML
    private Button addMed_btn;

    @FXML
    private Button addMedicines_addBtn;

    @FXML
    private TextField addMedicines_brand;

    @FXML
    private Button addMedicines_clearBtn;

    @FXML
    private TableColumn<medicineData, String> addMedicines_col_brand;

    @FXML
    private TableColumn<medicineData, String> addMedicines_col_date;

    @FXML
    private TableColumn<medicineData, String> addMedicines_col_medicineID;

    @FXML
    private TableColumn<medicineData, String> addMedicines_col_price;

    @FXML
    private TableColumn<medicineData, String> addMedicines_col_productName;

    @FXML
    private TableColumn<medicineData, String> addMedicines_col_status;

    @FXML
    private TableColumn<medicineData, String> addMedicines_col_type;

    @FXML
    private Button addMedicines_deleteBtn;

    @FXML
    private AnchorPane addMedicines_form;

    @FXML
    private ImageView addMedicines_imageView;

    @FXML
    private Button addMedicines_importBtn;

    @FXML
    private TextField addMedicines_medicineID;

    @FXML
    private TextField addMedicines_price;

    @FXML
    private TextField addMedicines_productName;

    @FXML
    private TextField addMedicines_search;

    @FXML
    private ComboBox<String> addMedicines_status;

    @FXML
    private ComboBox<String> addMedicines_type;

    @FXML
    private Button addMedicines_updateBtn;

    @FXML
    private Button close;

    @FXML
    private AnchorPane dashboard_form;

    @FXML
    private Label dashboard_availableMed;

    @FXML
    private Button dashboard_btn;

    @FXML
    private AreaChart<?, ?> dashboard_chart;

    @FXML
    private AnchorPane dashboard;

    @FXML
    private Label dashboard_totalCustomers;

    @FXML
    private Label dashboard_totalIncome;

    @FXML
    private Button logout;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button minimize;

    @FXML
    private Button purchase_addBtn;

    @FXML
    private TextField purchase_amount;

    @FXML
    private Label purchase_balance;

    @FXML
    private ComboBox<String> purchase_brand;

    @FXML
    private Button purchase_btn;

    @FXML
    private TableColumn<customerData, String> purchase_col_brand;

    @FXML
    private TableColumn<customerData, String> purchase_col_medicineId;

    @FXML
    private TableColumn<customerData, String> purchase_col_price;

    @FXML
    private TableColumn<customerData, String> purchase_col_productName;

    @FXML
    private TableColumn<customerData, String> purchase_col_qty;

    @FXML
    private TableColumn<customerData, String> purchase_col_type;

    @FXML
    private AnchorPane purchase_form;

    @FXML
    private ComboBox<String> purchase_medicineID;

    @FXML
    private Button purchase_payBtn;

    @FXML
    private ComboBox<String> purchase_productName;

    @FXML
    private TableView<medicineData> addMedicines_tableView;

    @FXML
    private TableView<customerData> purchase_tableView;

    @FXML
    private Label purchase_total;

    @FXML
    private ComboBox<String> purchase_type;


    @FXML
    private Spinner<Integer> purchase_quantity;

    @FXML
    private Label username;

    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;
    private Image image;


    public void homeChart(){
        dashboard_chart.getData().clear();

        String sql = "SELECT date, SUM(total) FROM customer_info"
                + " GROUP BY date ORDER BY TIMESTAMP(date) ASC LIMIT 9";

        Connection localConnect = null;
        PreparedStatement localPrepare = null;
        ResultSet localResult = null;

        try{
            localConnect = database.connectDb();
            XYChart.Series chart = new XYChart.Series();

            localPrepare = localConnect.prepareStatement(sql);
            localResult = localPrepare.executeQuery();

            while(localResult.next()){
                chart.getData().add(new XYChart.Data(localResult.getString(1), localResult.getInt(2)));
            }

            dashboard_chart.getData().add(chart);

        }catch(Exception e){
            e.printStackTrace();
        } finally {
            try {
                if (localResult != null) localResult.close();
                if (localPrepare != null) localPrepare.close();
                if (localConnect != null) localConnect.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void homeAM(){
        String sql = "SELECT COUNT(id) AS total FROM medicine WHERE status='Available'";
        Connection localConnect = null;
        PreparedStatement localPrepare = null;
        ResultSet localResult = null;
        int countAM = 0;
        try {
            localConnect = database.connectDb();
            localPrepare = localConnect.prepareStatement(sql);
            localResult = localPrepare.executeQuery();

            if (localResult.next()) {
                countAM = localResult.getInt("total");
            }

            dashboard_availableMed.setText(String.valueOf(countAM));

        } catch(Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Exception Caught");
            alert.setHeaderText(null);
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();
        } finally {
            try {
                if (localResult != null) localResult.close();
                if (localPrepare != null) localPrepare.close();
                if (localConnect != null) localConnect.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void homeTI(){
        String sql = "SELECT SUM(total) FROM customer_info";

        Connection localConnect = null;
        PreparedStatement localPrepare = null;
        ResultSet localResult = null;
        double totalDisplay = 0;
        try{
            localConnect = database.connectDb();
            localPrepare = localConnect.prepareStatement(sql);
            localResult = localPrepare.executeQuery();

            while(localResult.next()){
                totalDisplay = localResult.getDouble("SUM(total)");
            }

            dashboard_totalIncome.setText( String.valueOf(totalDisplay));

        }catch(Exception e){
            e.printStackTrace();
        } finally {
            try {
                if (localResult != null) localResult.close();
                if (localPrepare != null) localPrepare.close();
                if (localConnect != null) localConnect.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void homeTC(){

        String sql = "SELECT COUNT(id) FROM customer_info";

        Connection localConnect = null;
        PreparedStatement localPrepare = null;
        ResultSet localResult = null;
        int countTC = 0;

        try{
            localConnect = database.connectDb();
            localPrepare = localConnect.prepareStatement(sql);
            localResult = localPrepare.executeQuery();

            while(localResult.next()){
                countTC = localResult.getInt("COUNT(id)");
            }

            dashboard_totalCustomers.setText(String.valueOf(countTC));

        }catch(Exception e){
            e.printStackTrace();
        } finally {
            try {
                if (localResult != null) localResult.close();
                if (localPrepare != null) localPrepare.close();
                if (localConnect != null) localConnect.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void addMedicinesAdd() {
        String sql = "INSERT INTO medicine (medicine_id, brand, productName, type, status, price, image, date) "
                + "VALUES(?,?,?,?,?,?,?,?)";

        Connection localConnect = null;
        PreparedStatement localPrepare = null;
        Statement localStatement = null;
        ResultSet localResult = null;

        try {
            localConnect = database.connectDb();
            Alert alert;

            if (addMedicines_medicineID.getText().isEmpty()
                    || addMedicines_brand.getText().isEmpty()
                    || addMedicines_productName.getText().isEmpty()
                    || addMedicines_type.getSelectionModel().getSelectedItem() == null
                    || addMedicines_status.getSelectionModel().getSelectedItem() == null
                    || addMedicines_price.getText().isEmpty()
                    || getData.path == null || getData.path == "") {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                String checkData = "SELECT medicine_id FROM medicine WHERE medicine_id = '"
                        + addMedicines_medicineID.getText() + "'";

                localStatement = localConnect.createStatement();
                localResult = localStatement.executeQuery(checkData);

                if (localResult.next()) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Medicine ID: " + addMedicines_medicineID.getText() + " was already exist!");
                    alert.showAndWait();
                } else {
                    localPrepare = localConnect.prepareStatement(sql);
                    localPrepare.setString(1, addMedicines_medicineID.getText());
                    localPrepare.setString(2, addMedicines_brand.getText());
                    localPrepare.setString(3, addMedicines_productName.getText());
                    localPrepare.setString(4, (String) addMedicines_type.getSelectionModel().getSelectedItem());
                    localPrepare.setString(5, (String) addMedicines_status.getSelectionModel().getSelectedItem());
                    localPrepare.setString(6, addMedicines_price.getText());
                    String uri = getData.path;
                    uri = uri.replace("\\", "\\\\");

                    localPrepare.setString(7, uri);

                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                    localPrepare.setString(8, String.valueOf(sqlDate));
                    localPrepare.executeUpdate();
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added!");
                    alert.showAndWait();
                    addMedicineShowListData();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Exception Caught");
            alert.setHeaderText(null);
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();
        } finally {
            try {
                if (localResult != null) localResult.close();
                if (localStatement != null) localStatement.close();
                if (localPrepare != null) localPrepare.close();
                if (localConnect != null) localConnect.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void addMedicineUpdate(){

        String uri = getData.path;
        uri = uri.replace("\\", "\\\\");

        String sql = "UPDATE medicine SET brand = '"
                +addMedicines_brand.getText()+"', productName = '"
                +addMedicines_productName.getText()+"', type = '"
                +addMedicines_type.getSelectionModel().getSelectedItem()+"', status = '"
                +addMedicines_status.getSelectionModel().getSelectedItem()+"', price = '"
                +addMedicines_price.getText()+"', image = '"+uri+"' WHERE medicine_id = '"
                +addMedicines_medicineID.getText()+"'";

        Connection localConnect = null;
        Statement localStatement = null;

        try{
            localConnect = database.connectDb();
            Alert alert;

            if(addMedicines_medicineID.getText().isEmpty()
                    || addMedicines_brand.getText().isEmpty()
                    || addMedicines_productName.getText().isEmpty()
                    || addMedicines_type.getSelectionModel().getSelectedItem() == null
                    || addMedicines_status.getSelectionModel().getSelectedItem() == null
                    || addMedicines_price.getText().isEmpty()
                    || getData.path == null || getData.path == ""){
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            }else{
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE Medicine ID:" + addMedicines_medicineID.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if(option.get().equals(ButtonType.OK)){
                    localStatement = localConnect.createStatement();
                    localStatement.executeUpdate(sql);

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Updated!");
                    alert.showAndWait();

                    addMedicineShowListData();
                    addMedicineReset();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            try {
                if (localStatement != null) localStatement.close();
                if (localConnect != null) localConnect.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addMedicineDelete(){

        String sql = "DELETE FROM medicine WHERE medicine_id = '"+addMedicines_medicineID.getText()+"'";

        Connection localConnect = null;
        Statement localStatement = null;

        try{
            localConnect = database.connectDb();
            Alert alert;

            if(addMedicines_medicineID.getText().isEmpty()
                    || addMedicines_brand.getText().isEmpty()
                    || addMedicines_productName.getText().isEmpty()
                    || addMedicines_type.getSelectionModel().getSelectedItem() == null
                    || addMedicines_status.getSelectionModel().getSelectedItem() == null
                    || addMedicines_price.getText().isEmpty()
                    || getData.path == null || getData.path == ""){
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            }else{
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to DELETE Medicine ID:" + addMedicines_medicineID.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if(option.get().equals(ButtonType.OK)){
                    localStatement = localConnect.createStatement();
                    localStatement.executeUpdate(sql);

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted!");
                    alert.showAndWait();

                    addMedicineShowListData();
                    addMedicineReset();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            try {
                if (localStatement != null) localStatement.close();
                if (localConnect != null) localConnect.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addMedicineReset(){
        addMedicines_medicineID.setText("");
        addMedicines_brand.setText("");
        addMedicines_productName.setText("");
        addMedicines_price.setText("");
        addMedicines_type.getSelectionModel().clearSelection();
        addMedicines_status.getSelectionModel().clearSelection();

        addMedicines_imageView.setImage(null);

        getData.path = "";

    }

    private String[] addMedicineListT = {"Hydrocodone", "Antibiotics", "Metformin", "Losartan", "Albuterol"};
    public void addMedicineListType() {
        List<String> listT = new ArrayList<>();

        for (String data : addMedicineListT) {
            listT.add(data);
        }
        ObservableList listData = FXCollections.observableArrayList(listT);
        addMedicines_type.setItems(listData);
    }

    private String [] addMedicineStatus ={"Available" , "Unavailable"};
    public void addMedicineListStatus(){
        List<String> listS = new ArrayList<>();

        for(String data: addMedicineStatus){
            listS.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listS);
        addMedicines_status.setItems(listData);
    }
    public void addMedicineImportImage(){
        FileChooser open = new FileChooser();
        open.setTitle("Import Image File");
        open.getExtensionFilters().add(new ExtensionFilter("Image File", "*jpg", "*png"));

        File file = open.showOpenDialog(main_form.getScene().getWindow());

        if(file != null) {
            image = new Image(file.toURI().toString(), 118, 147, false, true);

            addMedicines_imageView.setImage(image);

            getData.path = file.getAbsolutePath();
        }
    }
    public ObservableList<medicineData> addMedicinesListData() {
        String sql = "SELECT * FROM medicine";

        ObservableList<medicineData> listData = FXCollections.observableArrayList();

        Connection localConnect = null;
        PreparedStatement localPrepare = null;
        ResultSet localResult = null;

        try {
            localConnect = database.connectDb();
            localPrepare = localConnect.prepareStatement(sql);
            localResult = localPrepare.executeQuery();

            medicineData medData;

            while (localResult.next()) {
                medData = new medicineData(localResult.getInt("medicine_id"), localResult.getString("brand")
                        , localResult.getString("productName"), localResult.getString("type")
                        , localResult.getString("status"), localResult.getDouble("price")
                        , localResult.getDate("date"), localResult.getString("image"));

                listData.add(medData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (localResult != null) localResult.close();
                if (localPrepare != null) localPrepare.close();
                if (localConnect != null) localConnect.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return listData;

    }


    private ObservableList<medicineData> addMedicineList;
    public void addMedicineShowListData(){
        addMedicineList = addMedicinesListData();

        addMedicines_col_medicineID.setCellValueFactory(new PropertyValueFactory<>("medicineId"));
        addMedicines_col_brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        addMedicines_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        addMedicines_col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        addMedicines_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        addMedicines_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        addMedicines_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));

        addMedicines_tableView.setItems(addMedicineList);

    }

    public void addMedicineSearch(){

        FilteredList<medicineData> filter = new FilteredList<>(addMedicineList, e-> true);

        addMedicines_search.textProperty().addListener((Observable, oldValue, newValue) ->{

            filter.setPredicate(predicateMedicineData ->{

                if(newValue == null || newValue.isEmpty()){
                    return true;
                }

                String searchKey = newValue.toLowerCase();

                if(predicateMedicineData.getMedicineId().toString().contains(searchKey)){
                    return true;
                }else if(predicateMedicineData.getBrand().toLowerCase().contains(searchKey)){
                    return true;
                }else if(predicateMedicineData.getProductName().toLowerCase().contains(searchKey)){
                    return true;
                }else if(predicateMedicineData.getType().toLowerCase().contains(searchKey)){
                    return true;
                }else if(predicateMedicineData.getStatus().toLowerCase().contains(searchKey)){
                    return true;
                }else if(predicateMedicineData.getPrice().toString().contains(searchKey)){
                    return true;
                }else if(predicateMedicineData.getDate().toString().contains(searchKey)){
                    return true;
                }else return false;
            });
        });

        SortedList<medicineData> sortList = new SortedList<>(filter);

        sortList.comparatorProperty().bind(addMedicines_tableView.comparatorProperty());
        addMedicines_tableView.setItems(sortList);

    }



    public void addMedicineSelect(){
        medicineData medData = addMedicines_tableView.getSelectionModel().getSelectedItem();
        int num = addMedicines_tableView.getSelectionModel().getSelectedIndex();
        if((num - 1) < - 1){return;}

        addMedicines_medicineID.setText(String.valueOf(medData.getMedicineId()));
        addMedicines_brand.setText(medData.getBrand());
        addMedicines_productName.setText(medData.getProductName());
        addMedicines_price.setText(String.valueOf(medData.getPrice()));

        String uri = "file:" + medData.getImage();

        image = new Image(uri, 118, 147, false, true);
        addMedicines_imageView.setImage(image);

        getData.path = medData.getImage();

    }
    private double totalP;

    public void purchaseAdd(){
        purchaseCustomerId();

        String sql = "INSERT INTO customer (customer_id,type,medicine_id,brand,productName,quantity,price,date)"
                + " VALUES(?,?,?,?,?,?,?,?)";

        Connection localConnect = null;
        PreparedStatement localPrepare = null;
        Statement localStatement = null;
        ResultSet localResult = null;

        try{
            localConnect = database.connectDb();
            Alert alert;

            if(purchase_type.getSelectionModel().getSelectedItem() == null
                    || purchase_medicineID.getSelectionModel().getSelectedItem() == null
                    || purchase_brand.getSelectionModel().getSelectedItem() == null
                    || purchase_productName.getSelectionModel().getSelectedItem() == null){
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            }else{

                localPrepare = localConnect.prepareStatement(sql);
                localPrepare.setString(1, String.valueOf(customerId));
                localPrepare.setString(2, (String)purchase_type.getSelectionModel().getSelectedItem());
                localPrepare.setString(3, (String)purchase_medicineID.getSelectionModel().getSelectedItem());
                localPrepare.setString(4, (String)purchase_brand.getSelectionModel().getSelectedItem());
                localPrepare.setString(5, (String)purchase_productName.getSelectionModel().getSelectedItem());
                localPrepare.setString(6, String.valueOf(qty));

                String checkData = "SELECT price FROM medicine WHERE medicine_id = '"
                        +purchase_medicineID.getSelectionModel().getSelectedItem()+"'";

                localStatement = localConnect.createStatement();
                localResult = localStatement.executeQuery(checkData);
                double priceD = 0;
                if(localResult.next()){
                    priceD = localResult.getDouble("price");
                }

                totalP = (priceD * qty);

                localPrepare.setString(7, String.valueOf(totalP));

                Date date = new Date();
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                localPrepare.setString(8, String.valueOf(sqlDate));

                localPrepare.executeUpdate();

                purchaseShowListData();
                purchaseDisplayTotal();
            }
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            try {
                if (localResult != null) localResult.close();
                if (localStatement != null) localStatement.close();
                if (localPrepare != null) localPrepare.close();
                if (localConnect != null) localConnect.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private double totalPriceD;
    public void  purchaseDisplayTotal(){
        String sql = "SELECT SUM(price) FROM customer WHERE customer_id = '"+customerId+"'";

        Connection localConnect = null;
        PreparedStatement localPrepare = null;
        ResultSet localResult = null;

        try{
            localConnect = database.connectDb();
            localPrepare = localConnect.prepareStatement(sql);
            localResult = localPrepare.executeQuery();

            if(localResult.next()){
                totalPriceD = localResult.getDouble("SUM(price)");
            }
            purchase_total.setText(String.valueOf(totalPriceD));

        }catch(Exception e){
            e.printStackTrace();
        } finally {
            try {
                if (localResult != null) localResult.close();
                if (localPrepare != null) localPrepare.close();
                if (localConnect != null) localConnect.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private double balance;
    private double amount;
    public void purchaseAmount(){
        if(purchase_amount.getText().isEmpty() || totalPriceD == 0){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Invalid :3");
            alert.showAndWait();
        }else {
            amount = Double.parseDouble(purchase_amount.getText());
            if (totalPriceD > amount) {
                purchase_amount.setText("");
            } else {
                balance = (amount - totalPriceD);

                purchase_balance.setText(String.valueOf(balance));
            }
        }
    }

    public void purchasePay(){
        purchaseCustomerId();
        String sql = "INSERT INTO customer_info (customer_id, total, date) "
                + "VALUES(?,?,?)";

        Connection localConnect = null;
        PreparedStatement localPrepare = null;

        try{
            localConnect = database.connectDb();
            Alert alert;

            if(totalPriceD == 0){
                alert = new Alert(AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Something wrong :3");
                alert.showAndWait();
            }else{
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setHeaderText(null);
                alert.setContentText("Are you sure?");
                Optional<ButtonType> option = alert.showAndWait();

                if(option.get().equals(ButtonType.OK)){

                    localPrepare = localConnect.prepareStatement(sql);
                    localPrepare.setString(1, String.valueOf(customerId));
                    localPrepare.setString(2, String.valueOf(totalPriceD));

                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                    localPrepare.setString(3, String.valueOf(sqlDate));

                    localPrepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("Successful!");
                    alert.showAndWait();

                    purchase_amount.setText("");
                    purchase_balance.setText("");
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        } finally {
            try {
                if (localPrepare != null) localPrepare.close();
                if (localConnect != null) localConnect.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private SpinnerValueFactory<Integer> spinner;
    public void purchaseShowValue(){
        spinner=new SpinnerValueFactory.IntegerSpinnerValueFactory(0,20,0);
        purchase_quantity.setValueFactory(spinner);
    }
    private int qty;
    public void purchaseQuantity(){
        qty=purchase_quantity.getValue();
    }

    public ObservableList<customerData>purchaseListData(){
        purchaseCustomerId();
        String sql = "SELECT * FROM customer WHERE customer_id = '"+customerId+"'";
        ObservableList<customerData>listData =FXCollections.observableArrayList();

        Connection localConnect = null;
        PreparedStatement localPrepare = null;
        ResultSet localResult = null;

        try{
            localConnect = database.connectDb();
            customerData customerD;
            localPrepare = localConnect.prepareStatement(sql);
            localResult = localPrepare.executeQuery();

            while(localResult.next()){
                customerD = new customerData(localResult.getInt("customer_id")
                        , localResult.getString("type"), localResult.getInt("medicine_id")
                        , localResult.getString("brand"), localResult.getString("productName")
                        , localResult.getInt("quantity"), localResult.getDouble("price")
                        , localResult.getDate("date"));

                listData.add(customerD);
            }

        }catch(Exception e){
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Exception Caught");
            alert.setHeaderText(null);
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();
        } finally {
            try {
                if (localResult != null) localResult.close();
                if (localPrepare != null) localPrepare.close();
                if (localConnect != null) localConnect.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return listData;

    }
    private ObservableList<customerData> purchaseList;
    public void purchaseShowListData(){
        purchaseList = purchaseListData();

        purchase_col_medicineId.setCellValueFactory(new PropertyValueFactory<>("medicineId"));
        purchase_col_brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        purchase_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        purchase_col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        purchase_col_qty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        purchase_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));

        purchase_tableView.setItems(purchaseList);

    }

    private int customerId;
    public void purchaseCustomerId(){
        String sql = "SELECT customer_id FROM customer";

        Connection localConnect = null;
        PreparedStatement localPrepare = null;
        Statement localStatement = null;
        ResultSet localResult = null;

        try{
            localConnect = database.connectDb();
            localPrepare = localConnect.prepareStatement(sql);
            localResult = localPrepare.executeQuery();
            while(localResult.next()){
                customerId = localResult.getInt("customer_id");
            }
            int cID = 0;
            String checkData = "SELECT customer_id FROM customer_info";

            localStatement = localConnect.createStatement();
            localResult = localStatement.executeQuery(checkData);

            while(localResult.next()){
                cID = localResult.getInt("customer_id");
            }
            if(customerId == 0){
                customerId+=1;
            }else if(cID == customerId){
                customerId = cID+1;
            }

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Exception Caught");
            alert.setHeaderText(null);
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();
        } finally {
            try {
                if (localResult != null) localResult.close();
                if (localStatement != null) localStatement.close();
                if (localPrepare != null) localPrepare.close();
                if (localConnect != null) localConnect.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void purchaseType(){
        String sql = "SELECT type FROM medicine WHERE status = 'Available'";

        Connection localConnect = null;
        PreparedStatement localPrepare = null;
        ResultSet localResult = null;

        try{
            localConnect = database.connectDb();
            ObservableList listData = FXCollections.observableArrayList();

            localPrepare = localConnect.prepareStatement(sql);
            localResult = localPrepare.executeQuery();

            while(localResult.next()){
                listData.add(localResult.getString("type"));
            }
            purchase_type.setItems(listData);

            purchaseMedicineId();

        }catch(Exception e){
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Exception Caught");
            alert.setHeaderText(null);
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();
        } finally {
            try {
                if (localResult != null) localResult.close();
                if (localPrepare != null) localPrepare.close();
                if (localConnect != null) localConnect.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void purchaseMedicineId(){

        String sql = "SELECT * FROM medicine WHERE type = '"
                +purchase_type.getSelectionModel().getSelectedItem()+"'";

        Connection localConnect = null;
        PreparedStatement localPrepare = null;
        ResultSet localResult = null;

        try{
            localConnect = database.connectDb();
            ObservableList listData = FXCollections.observableArrayList();

            localPrepare = localConnect.prepareStatement(sql);
            localResult = localPrepare.executeQuery();

            while(localResult.next()){
                listData.add(localResult.getString("medicine_id"));
            }
            purchase_medicineID.setItems(listData);

            purchaseBrand();
        }catch(Exception e){
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Exception Caught");
            alert.setHeaderText(null);
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();
        } finally {
            try {
                if (localResult != null) localResult.close();
                if (localPrepare != null) localPrepare.close();
                if (localConnect != null) localConnect.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void purchaseBrand(){

        String sql = "SELECT * FROM medicine WHERE medicine_id = '"
                +purchase_medicineID.getSelectionModel().getSelectedItem()+"'";

        Connection localConnect = null;
        PreparedStatement localPrepare = null;
        ResultSet localResult = null;

        try{
            localConnect = database.connectDb();
            ObservableList listData = FXCollections.observableArrayList();

            localPrepare = localConnect.prepareStatement(sql);
            localResult = localPrepare.executeQuery();

            while(localResult.next()){
                listData.add(localResult.getString("brand"));
            }
            purchase_brand.setItems(listData);

            purchaseProductName();

        }catch(Exception e){
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Exception Caught");
            alert.setHeaderText(null);
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();
        } finally {
            try {
                if (localResult != null) localResult.close();
                if (localPrepare != null) localPrepare.close();
                if (localConnect != null) localConnect.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void purchaseProductName(){

        String sql = "SELECT * FROM medicine WHERE brand = '"
                +purchase_brand.getSelectionModel().getSelectedItem()+"'";

        Connection localConnect = null;
        PreparedStatement localPrepare = null;
        ResultSet localResult = null;

        try{
            localConnect = database.connectDb();
            ObservableList listData = FXCollections.observableArrayList();

            localPrepare = localConnect.prepareStatement(sql);
            localResult = localPrepare.executeQuery();

            while(localResult.next()){
                listData.add(localResult.getString("productName"));
            }
            purchase_productName.setItems(listData);
        }catch(Exception e){
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Exception Caught");
            alert.setHeaderText(null);
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();
        } finally {
            try {
                if (localResult != null) localResult.close();
                if (localPrepare != null) localPrepare.close();
                if (localConnect != null) localConnect.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }




    public void switchForm(ActionEvent event){
        if(event.getSource()==dashboard_btn){
            dashboard_form.setVisible(true);
            addMedicines_form.setVisible(false);
            purchase_form.setVisible(false);
            dashboard_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #41b170, #8a418c);");
            addMed_btn.setStyle("-fx-background-color:transparent");
            purchase_btn.setStyle("-fx-background-color:transparent");
            homeAM();
            homeTC();
            homeTI();
            homeChart();


        } else if (event.getSource()==addMed_btn){
            dashboard_form.setVisible(false);
            addMedicines_form.setVisible(true);
            purchase_form.setVisible(false);
            addMed_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #41b170, #8a418c);");
            dashboard_btn.setStyle("-fx-background-color:transparent");
            purchase_btn.setStyle("-fx-background-color:transparent");

            addMedicineShowListData();
            addMedicineListStatus();
            addMedicineListType();
            addMedicineSearch();


        }
        else if(event.getSource()==purchase_btn){
            dashboard_form.setVisible(false);
            addMedicines_form.setVisible(false);
            purchase_form.setVisible(true);
            purchase_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #41b170, #8a418c);");
            dashboard_btn.setStyle("-fx-background-color:transparent");
            addMed_btn.setStyle("-fx-background-color:transparent");

            purchaseType();
            purchaseMedicineId();
            purchaseBrand();
            purchaseProductName();
            purchaseShowValue();
            purchaseDisplayTotal();
        }
    }

    private double x=0;
    private double y=0;
    public void logout() {
        try {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.isPresent() && option.get().equals(ButtonType.OK)) {
                Stage currentStage = (Stage) logout.getScene().getWindow();
                currentStage.close();


                FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
                Parent root = loader.load();

                Stage loginStage = new Stage();
                loginStage.setScene(new Scene(root));
                loginStage.setTitle("Login");


                loginStage.initStyle(StageStyle.UTILITY);


                loginStage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public void minimize() {
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.setIconified(true);
    }

    public void close() {
        System.exit(0);
    }

    public void openChatSupport() {
        try {
            // Initialize the chat server manager if it's not running
            ChatServerManager chatManager = ChatServerManager.getInstance();
            if (!chatManager.isRunning()) {
                chatManager.startServer();
            }

            // Load the admin chat interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin_chat.fxml"));
            Parent root = loader.load();

            // Create new stage for chat window
            Stage chatStage = new Stage();
            chatStage.setTitle("Admin Chat Support");
            chatStage.setScene(new Scene(root));
            chatStage.setResizable(false);

            // Show the chat window
            chatStage.show();

            // Show confirmation to admin
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Chat Server");
            alert.setHeaderText("Chat Support Activated");
            alert.setContentText("Chat server is running. Chat window opened. Waiting for customers to connect...");
            alert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not start chat support");
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        homeChart();
        homeAM();
        homeTC();
        homeTI();
        addMedicineShowListData();
        addMedicineListType();
        addMedicineListStatus();

        purchaseType();
        purchaseMedicineId();
        purchaseBrand();
        purchaseProductName();
        purchaseShowValue();
        purchaseDisplayTotal();

    }
}

