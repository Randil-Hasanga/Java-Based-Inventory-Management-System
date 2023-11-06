package com.stockportfoliomanagementsystem.PortfolioManager;

import com.stockportfoliomanagementsystem.MainController;
import com.stockportfoliomanagementsystem.MySqlCon;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.ResourceBundle;

public class UpdateOtherUsers implements Initializable {

    Connection conn = MySqlCon.MysqlMethod();

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
    private Label lblSuccess;
    private String userName;
   // private String pwd;
    private String Fname;
    private String Lname;
    private String NIC;
    private String contact;
    private String userId = ManageUsersCtrl.getSelectedUser();

    @FXML
    void onUpdateButton(MouseEvent event) {

        userName = txtUserName.getText();
        //pwd = txtPwd.getText();
        Fname = txtFname.getText();
        Lname = txtLname.getText();
        NIC = txtNIC.getText();
        //position = dropPosition.getValue();
        contact = txtContact.getText();

        if((userId.isEmpty() || userName.isEmpty() || Fname.isEmpty() || Lname.isEmpty() || NIC.isEmpty() || contact.isEmpty())){
            MainController.fillAllTheFieldsAlert();
        }else {
            if(MainController.isPhoneNumberValid(contact)) {
                if(MainController.isNICValid(NIC)){
                    String sql = "UPDATE users SET Username = ?, Fname = ?, Lname = ?, NIC = ?, Contact = ? WHERE User_Id = ?";

                    try {
                        PreparedStatement pstmt = conn.prepareStatement(sql);
                        pstmt.setString(1, txtUserName.getText());
                        pstmt.setString(2, txtFname.getText());
                        pstmt.setString(3, txtLname.getText());
                        pstmt.setString(4, txtNIC.getText());
                        pstmt.setString(5, txtContact.getText());
                        pstmt.setString(6, userId);
                        pstmt.executeUpdate();
                    } catch (SQLException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    lblSuccess.setText("Successfully Updated");
                }else{
                    MainController.invalidNICAlert();
                }
            }else{
                MainController.invalidPhoneNumberAlert();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtUserName.setEditable(false);
        txtUserID.setEditable(false);

        String sql = "SELECT * FROM users WHERE User_Id = ?";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                userId = rs.getString("User_id");
                userName = rs.getString("Username");
                //pwd = rs.getString("Password");
                Fname = rs.getString("Fname");
                Lname = rs.getString("Lname");
                NIC = rs.getString("NIC");
                //position = rs.getString("Position");
                contact = rs.getString("Contact");

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        txtUserID.setText(userId);
        txtUserName.setText(userName);
        //txtPwd.setText(pwd);
        txtFname.setText(Fname);
        txtLname.setText(Lname);
        txtNIC.setText(NIC);
        //dropPosition.setValue(position);
        txtContact.setText(contact);
    }
}
