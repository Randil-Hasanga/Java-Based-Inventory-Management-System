package com.stockportfoliomanagementsystem.PortfolioManager;

import com.stockportfoliomanagementsystem.MainController;
import com.stockportfoliomanagementsystem.MySqlCon;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EditProfile implements Initializable{

    Connection conn = MySqlCon.MysqlMethod();
    MainController mc= new MainController();
    @FXML
    private ComboBox<String> dropPosition;

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

    File selectedFile;
    FileChooser fc;
    FileInputStream fis;

    @FXML
    private Window window;

    String position;
    String userId;
    String userName = mc.getUsername();
    String pwd = mc.getPwd();
    String NIC;
    String Lname;
    String Fname;
    String contact;
    InputStream is;

    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Label lblWarning;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dropPosition.setItems(FXCollections.observableArrayList("Portfolio Manager", "Accounting Manager", "HR Manager", "Stock keeper"));

        String sql = "SELECT * FROM users WHERE Username = ? AND Password = ?";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userName);
            pstmt.setString(2, pwd);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                userId = rs.getString("User_id");
                userName = rs.getString("Username");
                pwd = rs.getString("Password");
                Fname = rs.getString("Fname");
                Lname = rs.getString("Lname");
                NIC = rs.getString("NIC");
                position = rs.getString("Position");
                contact = rs.getString("Contact");
                is = rs.getBinaryStream("Pic");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        txtUserID.setText(userId);
        txtUserName.setText(userName);
        txtPwd.setText(pwd);
        txtFname.setText(Fname);
        txtLname.setText(Lname);
        txtNIC.setText(NIC);
        dropPosition.setValue(position);
        txtContact.setText(contact);
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
    void onUpdateButton(MouseEvent event) {
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
            String sql = "UPDATE users SET User_id = ?, Username = ?, Password = ?, FName = ?, Lname = ?, NIC = ?, Position = ?, Contact = ?, Pic = ? WHERE Username = ? AND Password = ?";

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
                pstmt.setBinaryStream(9, fis);
                pstmt.setString(10, mc.getUsername());
                pstmt.setString(11, mc.getPwd());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void onClearButton(MouseEvent event) {
        txtUserName.setText("");
        txtPwd.setText("");
        txtContact.setText("");
        txtFname.setText("");
        txtLname.setText("");
        txtNIC.setText("");
    }

}
