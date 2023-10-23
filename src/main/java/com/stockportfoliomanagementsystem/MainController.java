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
    private String DBUsername;
    private String DBPwd;
    private String position;
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
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    public void initialize(){
        if(btnClear.isFocused()){
            btnClear.setStyle("-fx-background-color: #03c62a;");
        }
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
            String sql = "SELECT Password, Position FROM Users WHERE Username = ?";

            try(PreparedStatement pstmt = conn.prepareStatement(sql)){
                pstmt.setString(1,username);
                ResultSet rs = pstmt.executeQuery();

                while(rs.next()){
                    DBPwd = rs.getString("Password");
                    position = rs.getString("Position");
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
<<<<<<< Updated upstream
        if(position.equals("Manager")){
            root = FXMLLoader.load(getClass().getResource("PortfolioManagerDashboard.fxml"));
=======
        if(position.equals("Portfolio Manager")){
            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/PortfolioManager/PortfolioManagerDashboard.fxml"));
>>>>>>> Stashed changes
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setHeight(700);
            stage.setWidth(1210);
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
<<<<<<< Updated upstream
=======
        }else if(position.equals("HR Manager")) {
            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/HRManager/HRManagerDashboard.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setHeight(700);
            stage.setWidth(1210);
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }else if(position.equals("Accounting Manager")) {
            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/AccountingManager/AccountingManagerDashboard.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setHeight(700);
            stage.setWidth(1210);
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }else if(position.equals("Stock keeper")) {
            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/StockKeeper/StockKeeperDashboard.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setHeight(700);
            stage.setWidth(1210);
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
>>>>>>> Stashed changes
        }
    }




}