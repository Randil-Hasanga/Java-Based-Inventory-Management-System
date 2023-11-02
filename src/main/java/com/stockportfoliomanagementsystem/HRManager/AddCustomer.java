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

public class AddCustomer implements Initializable{

    private Connection conn = MySqlCon.MysqlMethod();
    private  String cusID;
    private String cusName;
    private String cusAddress;
    private String cusContact;
    private int count;

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
        txtCusID.setEditable(false);
    }
    @FXML
    void onAddButton(MouseEvent event) {
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
    }
    @FXML
    void onBackButton(MouseEvent event) {

    }

    @FXML
    void onManageSuppliers(MouseEvent event) {

    }

    @FXML
    void onStockButton(MouseEvent event) {

    }


}
