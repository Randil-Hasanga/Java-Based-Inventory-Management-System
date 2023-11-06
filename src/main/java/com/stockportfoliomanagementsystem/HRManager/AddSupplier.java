package com.stockportfoliomanagementsystem.HRManager;

import com.stockportfoliomanagementsystem.MainController;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddSupplier implements Initializable {

    Connection conn = MySqlCon.MysqlMethod();
    @FXML
    private ImageView imageView;
    @FXML
    private Label lblSuccess;
    @FXML
    private Label txtName;
    @FXML
    private TextField txtSupAddress;
    @FXML
    private TextField txtSupContact;
    @FXML
    private TextField txtSupID;
    @FXML
    private TextField txtSupName;

    private String max;
    private int numericId;
    private  String supID;
    private String supName;
    private String supAddress;
    private String supContact;

    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    void onReports(MouseEvent event) {
        //To complete -----------------------------------------------------------------
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String sql2 = "SELECT MAX(S_ID) FROM supplier";
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
            txtSupID.setText("S001");
        }else if(numericId < 9) {
            txtSupID.setText("S00" + (numericId + 1));
        }else if(numericId < 99){
            txtSupID.setText("S0" + (numericId + 1));
        }else{
            txtSupID.setText("S" + (numericId + 1));
        }
        txtSupID.setEditable(false);
    }
    @FXML
    void onAddButton(MouseEvent event) {
        supName = txtSupName.getText();
        supAddress = txtSupAddress.getText();
        supContact = txtSupContact.getText();
        supID = txtSupID.getText();

        if((supName.isEmpty())||(supAddress.isEmpty()||(supContact.isEmpty()||supID.isEmpty()))){
            MainController.fillAllTheFieldsAlert();
        }else{
            if(MainController.isPhoneNumberValid(supContact)) {
                String sql = "INSERT INTO supplier (S_ID, S_Name, S_Location, S_Contact) VALUES (?, ?, ?, ?)";

                try {
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, String.valueOf(numericId + 1));
                    pstmt.setString(2, supName);
                    pstmt.setString(3, supAddress);
                    pstmt.setString(4, supContact);
                    pstmt.executeUpdate();

                } catch (SQLException e) {
                    System.out.println("Error : " + e.getMessage());
                }

                lblSuccess.setText("Customer Added Successfully");
            }else{
                MainController.invalidPhoneNumberAlert();
            }
        }
    }
    @FXML
    void onBackButton(MouseEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/HRManager/ManageSuppliers.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setHeight(700);
            stage.setWidth(1210);
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (NullPointerException e) {
        } catch (IOException e) {
        }
    }
    @FXML
    void onStockButton(MouseEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/HRManager/viewStock.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setHeight(700);
            stage.setWidth(1210);
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
        }
    }

    @FXML
    void onManageCustomers(MouseEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/HRManager/ManageCustomers.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setHeight(700);
            stage.setWidth(1210);
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {

        }
    }

}
