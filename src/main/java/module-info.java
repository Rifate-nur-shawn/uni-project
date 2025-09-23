module com.iamshawn.uniproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.iamshawn.uniproject to javafx.fxml;
    exports com.iamshawn.uniproject;
}