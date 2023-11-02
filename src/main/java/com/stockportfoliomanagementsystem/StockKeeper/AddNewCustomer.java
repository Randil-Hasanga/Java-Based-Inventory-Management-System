package com.stockportfoliomanagementsystem.StockKeeper;

import com.stockportfoliomanagementsystem.MySqlCon;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddNewCustomer implements Initializable {

    Connection conn = MySqlCon.MysqlMethod();
    @FXML
    private ImageView imageView;

    @FXML
    private TextField txtCusAddress;

    @FXML
    private TextField txtCusContact;

    @FXML
    private TextField txtCusID;

    @FXML
    private TextField txtCusName;

    @FXML
    private Label txtName;
    @FXML
    private Label lblSuccess;
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;

    private int count;
    private static String cusID;
    private String cusName;
    private String cusAddress;
    private String cusContact;

    protected static String customerType;

    public static String getCusIDNew(){
        return cusID;
    }

    private void setCustomerType(String existing) {
        this.customerType = existing;
    }
    public static String getCustomerType() {
        return customerType;
    }

    @FXML
    void onAddButton(MouseEvent event) throws IOException {
        cusName = txtCusName.getText();
        cusAddress = txtCusAddress.getText();
        cusContact = txtCusContact.getText();
        cusID = txtCusID.getText();

        String sql = "INSERT INTO customer (C_ID, C_Name, C_Location, C_Contact) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, String.valueOf(count+1));
            pstmt.setString(2, cusName);
            pstmt.setString(3, cusAddress);
            pstmt.setString(4, cusContact);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        lblSuccess.setText("Customer Added Successfully");
        setCustomerType("New");
        SelectExistingCustomer.customerType = null;


        root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/StockKeeper/SellExisting.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setHeight(700);
        stage.setWidth(1210);
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);

//        stage.setOnCloseRequest(event2 -> {
//            event2.consume(); // Consume the event to prevent the window from closing immediately
//
//            // Show a confirmation dialog
//            Alert alert = new Alert(Alert.AlertType.WARNING);
//            alert.setTitle("Warning");
//            alert.setHeaderText("Close Warning");
//            alert.setContentText("You cannot close the program while a ongoing transaction is in progress");
//
//            // Add OK and Cancel buttons to the dialog
//            ButtonType buttonTypeOK = new ButtonType("OK");
//            alert.getButtonTypes().setAll(buttonTypeOK);
//
//            // Show the dialog and handle the user's response
//        });

        stage.show();
    }



    @FXML
    void onCustomersButton(MouseEvent event) {

    }

    @FXML
    void onReportsButton(MouseEvent event) {

    }

    @FXML
    void onSupplierButton(MouseEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String sql = "SELECT COUNT(*) FROM customer";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(count == 0){
            txtCusID.setText("C_001");
        }else if(count < 9) {
            txtCusID.setText("C_00" + (count + 1));
        }else if(count < 99){
            txtCusID.setText("C_0" + (count + 1));
        }else{
            txtCusID.setText("C_" + (count + 1));
        }
    }
}
