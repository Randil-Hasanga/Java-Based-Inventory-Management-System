package com.stockportfoliomanagementsystem.StockKeeper;

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

    private static String cusID;
    private String cusName;
    private String cusAddress;
    private String cusContact;
    private String max;
    private int numericId;

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

        if((cusName.isEmpty()||cusAddress.isEmpty()||cusContact.isEmpty())) {
            MainController.fillAllTheFieldsAlert();
        }else{
            if(MainController.isPhoneNumberValid(cusContact)){
                String sql = "INSERT INTO customer (C_ID, C_Name, C_Location, C_Contact) VALUES (?, ?, ?, ?)";

                try {
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, String.valueOf(numericId+1));
                    pstmt.setString(2, cusName);
                    pstmt.setString(3, cusAddress);
                    pstmt.setString(4, cusContact);
                    pstmt.executeUpdate();

                } catch (SQLException e) {
                    System.out.println("Error: " + e.getMessage());
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
                stage.show();
            }else{
                MainController.invalidPhoneNumberAlert();
            }
        }
    }



    @FXML
    void onBackButton(MouseEvent event) {
try {
            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/StockKeeper/SelectCustomerType.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setHeight(700);
            stage.setWidth(1210);
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e) {
        }
    }

    @FXML
    void onBuyProduct(MouseEvent event) throws IOException {
        try {
            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/StockKeeper/SelectSupplierType.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setHeight(700);
            stage.setWidth(1210);
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            throw new IOException(e);
        } catch (NullPointerException e) {
        }
    }

    @FXML
    void onCustomersButton(MouseEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/StockKeeper/viewCustomers.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setHeight(700);
            stage.setWidth(1210);
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
        } catch (NullPointerException e) {
        }
    }



    @FXML
    void onSupplierButton(MouseEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/StockKeeper/viewSuppliers.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setHeight(700);
            stage.setWidth(1210);
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
        } catch (NullPointerException e) {
        }
    }

    @FXML
    void onUpdateProducts(MouseEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/StockKeeper/ManageProducts.fxml"));
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
}
