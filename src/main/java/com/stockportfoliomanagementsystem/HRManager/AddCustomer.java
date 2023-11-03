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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddCustomer implements Initializable{

    private Connection conn = MySqlCon.MysqlMethod();
    private  String cusID;
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

    private String max;
    private int numericId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        String sql2 = "SELECT MAX(C_ID) FROM customer";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql2);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                max = rs.getString(1);
                System.out.println("Last : "+max);
            }
            Pattern pattern = Pattern.compile("\\d+");

            // Use a Matcher to find the numeric part
            Matcher matcher = pattern.matcher(max);

            if (matcher.find()) {
                // Extract the numeric part as a string
                String numericPart = matcher.group();

                // Convert the numeric part to an integer if needed
                numericId = Integer.parseInt(numericPart);

                // Now you have the numeric ID as an integer
                System.out.println("Numeric ID: " + numericId);
            } else {
                System.out.println("No numeric part found in the C_ID value.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(numericId == 0){
            txtCusID.setText("C_001");
        }else if(numericId < 9) {
            txtCusID.setText("C_00" + (numericId + 1));
        }else if(numericId < 99){
            txtCusID.setText("C_0" + (numericId + 1));
        }else{
            txtCusID.setText("C_" + (numericId + 1));
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
            pstmt.setString(1, String.valueOf(numericId+1));
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
