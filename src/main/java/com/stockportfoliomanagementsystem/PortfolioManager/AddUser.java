package com.stockportfoliomanagementsystem.PortfolioManager;

import com.stockportfoliomanagementsystem.MySqlCon;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
import java.sql.SQLException;
import java.util.ResourceBundle;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dropPosition.setItems(FXCollections.observableArrayList("Portfolio Manager", "Accounting Manager", "HR Manager", "Stock keeper"));
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
        userId = txtUserID.getText();
        userName = txtUserName.getText();
        pwd = txtPwd.getText();
        Fname = txtFname.getText();
        Lname = txtLname.getText();
        NIC = txtNIC.getText();
        contact = txtContact.getText();

        if((userId.isEmpty())||(userName.isEmpty())||(pwd.isEmpty())||(Fname.isEmpty())||(Lname.isEmpty())||(NIC.isEmpty())||(contact.isEmpty())||(position.isEmpty())){
            lblWarning.setText("Please Fill All The Fields");
        }else{
            String sql = "INSERT INTO users (User_id, Username, Password, FName, Lname, NIC, Position, Contact, Pic)\n" +
                    "VALUES (?,?,?,?,?,?,?,?,?)";

            try {
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, userId);
                pstmt.setString(2, userName);
                pstmt.setString(3, pwd);
                pstmt.setString(4, Fname);
                pstmt.setString(5, Lname);
                pstmt.setString(6, NIC);
                pstmt.setString(7, position);
                pstmt.setString(8, contact);
                pstmt.setBinaryStream(9, fis, (int)selectedFile.length());

                pstmt.executeUpdate();
                System.out.println("Successfully updated");

                ManageUsersCtrl mg = new ManageUsersCtrl();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @FXML
    void onBackButton(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/PortfolioManager/ManageUsers.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setHeight(700);
        stage.setWidth(1210);
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
