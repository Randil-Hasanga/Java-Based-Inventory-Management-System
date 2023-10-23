package com.stockportfoliomanagementsystem;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainController {

    Connection conn = MySqlCon.MysqlMethod();
    private static String DBUsername;
    private String DBPwd;
    private String position;
    private String username;
    private String password;
    private static String Fname;
    private static String Lname;

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
    private Stage stage;
    private Scene scene;
    private Parent root;
    private static String DBPw;

    public void setFname(String Fname){
        this.Fname = Fname;
    }
    public static String getFname(){
        return Fname;
    }
    public void setLname(String Lname){
        this.Lname = Lname;
    }
    public static String getLname(){
        return Lname;
    }

    public void setPwd(String pwd){
        this.DBPw = pwd;
    }
    public static String getPwd(){
        return DBPw;
    }
    public void setUsername(String username){
        this.DBUsername = username;
    }
    public static String getUsername(){
        return DBUsername;
    }


    @FXML
    void clearText(ActionEvent event) {
        txtUserName.setText("");
        txtPwd.setText("");
    }

    @FXML
    public void onLoginButton(MouseEvent event) throws IOException {

        username = txtUserName.getText();
        password = txtPwd.getText();

        if((username.isEmpty())&&(password.isEmpty())){
            lblWarning.setText("Please Fill All The Fields");
        }else{
            String sql = "SELECT Password, Position, Fname, Lname FROM Users WHERE Username = ?";

            try(PreparedStatement pstmt = conn.prepareStatement(sql)){
                pstmt.setString(1,username);
                ResultSet rs = pstmt.executeQuery();

                while(rs.next()){
                    DBPwd = rs.getString("Password");
                    position = rs.getString("Position");
                    Fname = rs.getString("Fname");
                    Lname = rs.getString("Lname");
                }
                System.out.println(Fname+" "+Lname);
                MainController mainController = new MainController();
                mainController.setFname(Fname);
                mainController.setLname(Lname);

                if((DBPwd!=null)&&(DBPwd.equals(password))){
                    lblWarning.setText("Password correct");
                    mainController.setPwd(DBPwd);
                    mainController.setUsername(username);
                }else{
                    lblWarning.setText("Incorrect Username or Password");
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        if(position.equals("Portfolio Manager")){
            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/PortfolioManager/PortfolioManagerDashboard.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setHeight(700);
            stage.setWidth(1210);
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }else if(position.equals("HR Manager")) {
            root = FXMLLoader.load(getClass().getResource("HRManagerDashboard.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setHeight(700);
            stage.setWidth(1210);
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }else if(position.equals("Accounting Manager")) {
            root = FXMLLoader.load(getClass().getResource("AccountingManagerDashboard.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setHeight(700);
            stage.setWidth(1210);
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }else if(position.equals("Stock keeper")) {
            root = FXMLLoader.load(getClass().getResource("StockKeeperDashboard.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setHeight(700);
            stage.setWidth(1210);
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }
    }
}