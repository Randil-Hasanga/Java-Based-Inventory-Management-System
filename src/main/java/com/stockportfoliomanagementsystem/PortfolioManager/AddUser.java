package com.stockportfoliomanagementsystem.PortfolioManager;

import com.stockportfoliomanagementsystem.MainController;
import com.stockportfoliomanagementsystem.MySqlCon;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.stockportfoliomanagementsystem.MainController.*;

public class AddUser implements Initializable {
    Connection conn = MySqlCon.MysqlMethod();
    @FXML
    private ComboBox<String> dropPosition;
    String userId;
    String userName;
    String pwd;
    String NIC;
    String Lname;
    String Fname;
    String position;
    String contact;
    File selectedFile;
    FileChooser fc;
    FileInputStream fis;
    @FXML
    private TextField txtContact;
    @FXML
    private TextField txtFname;
    @FXML
    private TextField txtLname;
    @FXML
    private TextField txtNIC;
    @FXML
    private PasswordField txtPwd;
    @FXML
    private TextField txtUserID;
    @FXML
    private TextField txtUserName;
    @FXML
    private Window window;
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Label lblWarning;
    private String max;
    private int numericId;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dropPosition.setItems(FXCollections.observableArrayList("Portfolio Manager", "Accounting Manager", "HR Manager", "Stock keeper"));

        String sql2 = "SELECT MAX(User_Id) FROM users";
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
            txtUserID.setText("U001");
        }else if(numericId < 9) {
            txtUserID.setText("U00" + (numericId + 1));
        }else if(numericId < 99){
            txtUserID.setText("U0" + (numericId + 1));
        }else{
            txtUserID.setText("U_" + (numericId + 1));
        }
        txtUserID.setEditable(false);
    }
    @FXML
    void onPositionSelection(ActionEvent event) {
        position = dropPosition.getValue();
    }
    @FXML
    void onChooseBtn(ActionEvent event) {
        fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png"));
        selectedFile = fc.showOpenDialog(window);

        if (selectedFile != null) {
            // Insert the selected photo into the database
            try {
               fis = new FileInputStream(selectedFile);

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @FXML
    void onInsertButton(MouseEvent event) {



        userName = txtUserName.getText();
        pwd = txtPwd.getText();
        Fname = txtFname.getText();
        Lname = txtLname.getText();
        NIC = txtNIC.getText();
        contact = txtContact.getText();
        position = dropPosition.getValue();

        if((userName.isEmpty())||(pwd.isEmpty())||(Fname.isEmpty())||(Lname.isEmpty())||(NIC.isEmpty())||(contact.isEmpty())||(position.isEmpty())||(selectedFile == null)){
            MainController.fillAllTheFieldsAlert();
        }else{
            if(isEmailValid(userName)){
                if (isPasswordValid(pwd)) {
                    if(isPhoneNumberValid(contact)) {
                        if(isNICValid(NIC)){
                            System.out.println("Password is valid.");

                            String sql = "INSERT INTO users (User_id, Username, Password, FName, Lname, NIC, Position, Contact, Pic)\n" +
                                    "VALUES (?,?,?,?,?,?,?,?,?)";

                            try {
                                PreparedStatement pstmt = conn.prepareStatement(sql);
                                pstmt.setString(1, String.valueOf(numericId + 1));
                                pstmt.setString(2, userName);
                                pstmt.setString(3, pwd);
                                pstmt.setString(4, Fname);
                                pstmt.setString(5, Lname);
                                pstmt.setString(6, NIC);
                                pstmt.setString(7, position);
                                pstmt.setString(8, contact);
                                pstmt.setBinaryStream(9, fis, (int) selectedFile.length());

                                pstmt.executeUpdate();
                                lblWarning.setText("Successfully updated");

                                ManageUsersCtrl mg = new ManageUsersCtrl();
                            } catch (SQLException e) {
                                System.out.println("Error: " + e.getMessage());
                            }

                            txtUserName.clear();
                            txtPwd.clear();
                            txtFname.clear();
                            txtLname.clear();
                            txtNIC.clear();
                            txtContact.clear();
                            dropPosition.setValue(null);
                        }else{
                            MainController.invalidNICAlert();
                        }
                    }else{
                        MainController.invalidPhoneNumberAlert();
                    }
                } else {
                    MainController.invalidPasswordAlert();
                }
            }else{
                MainController.invalidEmailAlert();
            }
        }

    }

}
