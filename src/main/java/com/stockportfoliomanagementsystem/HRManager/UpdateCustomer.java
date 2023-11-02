package com.stockportfoliomanagementsystem.HRManager;

import com.stockportfoliomanagementsystem.MySqlCon;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateCustomer implements Initializable {
    private Connection conn = MySqlCon.MysqlMethod();
    private String cusName;
    private String cusAddress;
    private String cusContact;

    @FXML
    private ImageView imageView;
    @FXML
    private Label lblSuccess;
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

    private String cusID = ManageCustomers.getSelectedCustomer();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        String sql = "SELECT * FROM customer WHERE C_ID = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,cusID);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                cusName = rs.getString("C_Name");
                cusAddress = rs.getString("C_Location");
                cusContact = rs.getString("C_Contact");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        txtCusID.setText(cusID);
        txtCusName.setText(cusName);
        txtCusAddress.setText(cusAddress);
        txtCusContact.setText(cusContact);

        txtCusID.setEditable(false);
    }


    @FXML
    void onManageSuppliers(MouseEvent event) {

    }

    @FXML
    void onStockButton(MouseEvent event) {

    }

    @FXML
    void onUpdateButton(MouseEvent event) {

        cusName = txtCusName.getText();
        cusAddress = txtCusAddress.getText();
        cusContact = txtCusContact.getText();

        String sql = "UPDATE customer SET C_Name = ?, C_Location = ?, C_Contact = ? WHERE C_ID = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,cusName);
            pstmt.setString(2,cusAddress);
            pstmt.setString(3,cusContact);
            pstmt.setString(4,cusID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("customer table updated");
        lblSuccess.setText("Customer table updated !");
    }

    @FXML
    void onBackButton(MouseEvent event) {

    }


}
