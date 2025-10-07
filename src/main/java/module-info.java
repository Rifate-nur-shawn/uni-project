module com.iamshawn.uniproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;


    opens com.iamshawn.uniproject to javafx.fxml;
    exports com.iamshawn.uniproject;
}