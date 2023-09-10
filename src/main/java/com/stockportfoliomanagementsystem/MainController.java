package com.stockportfoliomanagementsystem;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainController {

    Connection conn = MySqlCon.MysqlMethod();
    private String DBUsername;
    private String DBPwd;
    private String username;
    private String password;

    @FXML
    private JFXButton btnClear;

    @FXML
    private JFXButton btnLogin;

    @FXML
    private JFXPasswordField txtPwd;

    @FXML
    private JFXTextField txtUserName;

    @FXML
    private Label lblWarning;

    @FXML
    public void initialize(){
        if(btnClear.isFocused()){
            btnClear.setStyle("-fx-background-color: #03c62a;");
        }
    }

    @FXML
    void clearText(ActionEvent event) {

    }

    @FXML
    public void onLoginButton(MouseEvent event) throws IOException {

        username = txtUserName.getText();
        password = txtPwd.getText();

        if((username.isEmpty())&&(password.isEmpty())){
            lblWarning.setText("Please Fill All The Fields");
        }else{


            String sql = "SELECT Password FROM Users WHERE Username = ?";

            try(PreparedStatement pstmt = conn.prepareStatement(sql)){
                pstmt.setString(1,username);
                ResultSet rs = pstmt.executeQuery();

                while(rs.next()){
                    DBPwd = rs.getString("Password");
                }

                if((DBPwd!=null)&&(DBPwd.equals(password))){
                    lblWarning.setText("Password correct");
                }else{
                    lblWarning.setText("Incorrect Username or Password");
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void changeColorLoginEnter(MouseEvent event) throws IOException{
        btnLogin.setStyle("-fx-background-color: #03c62a;");
        btnLogin.setTextFill(Color.WHITE);
    }

    public void changeColorLoginExit(MouseEvent event) throws IOException{
        btnLogin.setStyle("-fx-background-color: #aee8a5;");
        btnLogin.setTextFill(Color.BLACK);
    }

    public void changeColorClearEnter(MouseEvent event) throws IOException{
        btnClear.setStyle("-fx-background-color: #03c62a;");
        btnClear.setTextFill(Color.WHITE);
    }

    public void changeColorClearExit(MouseEvent event) throws IOException{
        btnClear.setStyle("-fx-background-color: #aee8a5;");
        btnClear.setTextFill(Color.BLACK);
    }
}