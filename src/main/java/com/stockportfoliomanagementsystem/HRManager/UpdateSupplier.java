package com.stockportfoliomanagementsystem.HRManager;

import com.stockportfoliomanagementsystem.MainController;
import com.stockportfoliomanagementsystem.MySqlCon;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

public class UpdateSupplier implements Initializable {
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
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;

    private String supName;
    private String supAddress;
    private String supContact;

    private static String supID = ManageSuppliers.getSelectedSupplier();


    @FXML
    void onManageCustomer(MouseEvent event) {
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
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String sql = "SELECT * FROM supplier WHERE S_ID = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,supID);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                supName = rs.getString("S_Name");
                supAddress = rs.getString("S_Location");
                supContact = rs.getString("S_Contact");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        txtSupID.setText(supID);
        txtSupName.setText(supName);
        txtSupAddress.setText(supAddress);
        txtSupContact.setText(supContact);

        txtSupID.setEditable(false);
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
    void onUpdateButton(MouseEvent event) {
        supName = txtSupName.getText();
        supAddress = txtSupAddress.getText();
        supContact = txtSupContact.getText();

        if((supName.isEmpty())||(supAddress.isEmpty())||(supContact.isEmpty())){
            MainController.fillAllTheFieldsAlert();
        }else {
            if(MainController.isPhoneNumberValid(supContact)) {
                System.out.println("phone number valid");
                String sql = "UPDATE supplier SET S_Name = ?, S_Location = ?, S_Contact = ? WHERE S_ID = ?";
                try {
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, supName);
                    pstmt.setString(2, supAddress);
                    pstmt.setString(3, supContact);
                    pstmt.setString(4, supID);
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    System.out.println("Error: " + e.getMessage());
                }
                System.out.println("customer table updated");
                lblSuccess.setText("Customer table updated !");
            }else{
                MainController.invalidPhoneNumberAlert();
            }
        }
    }
}
