package com.stockportfoliomanagementsystem;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainController implements Initializable {

    Connection conn = MySqlCon.MysqlMethod();
    @FXML
    private AnchorPane aPane;
    @FXML
    private Label lblIMS;

    @FXML
    private Pane loginPane;
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

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    public static boolean isEmailValid(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private static final String PHONE_NUMBER_REGEX = "^[0-9]{10}$";
    public static boolean isPhoneNumberValid(String phoneNumber) {
        Pattern pattern = Pattern.compile(PHONE_NUMBER_REGEX);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    private static final String NIC_REGEX_V = "^[0-9]{9}[vV]$";
    private static final String NIC_REGEX = "^[0-9]{12}$";

    public static boolean isNICValid(String nic) {
        Pattern pattern = Pattern.compile(NIC_REGEX);
        Pattern pattern1 = Pattern.compile(NIC_REGEX_V);
        Matcher matcher = pattern.matcher(nic);
        Matcher matcher1 = pattern1.matcher(nic);
        return matcher.matches()||matcher1.matches();
    }
    public static boolean isPasswordValid(String password) {

        String regexPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,}$";
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static void fillAllTheFieldsAlert(){
        Alert confirmationDialog = new Alert(Alert.AlertType.WARNING);
        confirmationDialog.setTitle("Warning !");
        confirmationDialog.setContentText("Please Fill All The Fields");

        ButtonType okButton = new ButtonType("OK");

        confirmationDialog.showAndWait().ifPresent(response -> {
            if (response == okButton) {
                System.out.println("OK button clicked.");
            }
        });
    }

    public static void invalidNICAlert() {
        Alert confirmationDialog = new Alert(Alert.AlertType.WARNING);
        confirmationDialog.setTitle("Warning !");
        confirmationDialog.setContentText("Invalid NIC number");
        confirmationDialog.setContentText("Please Enter a Valid NIC Number\n" +
                "Ex: 123456789v or 123456789123");

        ButtonType okButton = new ButtonType("OK");

        confirmationDialog.showAndWait().ifPresent(response -> {
            if (response == okButton) {
                System.out.println("OK button clicked.");
            }
        });
    }
    public static void invalidPhoneNumberAlert(){
        Alert confirmationDialog = new Alert(Alert.AlertType.WARNING);
        confirmationDialog.setTitle("Warning !");
        confirmationDialog.setHeaderText("Invalid Phone Number");
        confirmationDialog.setContentText("Please Enter a Valid Phone Number\n" +
                "Ex: 0771234567");

        ButtonType okButton = new ButtonType("OK");

        confirmationDialog.showAndWait().ifPresent(response -> {
            if (response == okButton) {
                System.out.println("OK button clicked.");
            }
        });
    }

    public static void invalidEmailAlert(){
        Alert confirmationDialog = new Alert(Alert.AlertType.WARNING);
        confirmationDialog.setTitle("Warning !");
        confirmationDialog.setHeaderText("Invalid Email Address");
        confirmationDialog.setContentText("Please Enter a Valid Email Address\n" +
                "Ex: john@gmail.com");

        ButtonType okButton = new ButtonType("OK");

        confirmationDialog.showAndWait().ifPresent(response -> {
            if (response == okButton) {
                System.out.println("OK button clicked.");
            }
        });
    }

    public static void invalidPasswordAlert(){
        Alert confirmationDialog = new Alert(Alert.AlertType.WARNING);
        confirmationDialog.setTitle("Warning !");
        confirmationDialog.setHeaderText("Password Not Valid");
        confirmationDialog.setContentText("Minimum length of 8 characters.\n" +
                "At least one uppercase letter.\n" +
                "At least one lowercase letter.\n" +
                "At least one digit.\n" +
                "At least one special character ( @, #, $, etc.)");

        ButtonType okButton = new ButtonType("OK");

        confirmationDialog.showAndWait().ifPresent(response -> {
            if (response == okButton) {
                System.out.println("OK button clicked");
                confirmationDialog.close();
            }
        });
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

        if((username.isEmpty())||(password.isEmpty())){
            fillAllTheFieldsAlert();
        }else{
            if (isEmailValid(username)) {
                String sql = "SELECT Password, Position, Fname, Lname FROM Users WHERE Username = ?";

                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, username);
                    ResultSet rs = pstmt.executeQuery();

                    while (rs.next()) {
                        DBPwd = rs.getString("Password");
                        position = rs.getString("Position");
                        Fname = rs.getString("Fname");
                        Lname = rs.getString("Lname");
                    }
                    System.out.println(Fname + " " + Lname);
                    MainController mainController = new MainController();
                    mainController.setFname(Fname);
                    mainController.setLname(Lname);

                    if ((DBPwd != null) && (DBPwd.equals(password))) {
                        lblWarning.setText("Password correct");
                        mainController.setPwd(DBPwd);
                        mainController.setUsername(username);

                        if (position.equals("Portfolio Manager")) {
                            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/PortfolioManager/PortfolioManagerDashboard.fxml"));
                            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            stage.setHeight(700);
                            stage.setWidth(1210);
                            scene = new Scene(root);
                            stage.setScene(scene);
                            stage.setResizable(false);
                            stage.show();
                        } else if (position.equals("HR Manager")) {
                            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/HRManager/HRManagerDashboard.fxml"));
                            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            stage.setHeight(700);
                            stage.setWidth(1210);
                            scene = new Scene(root);
                            stage.setScene(scene);
                            stage.setResizable(false);
                            stage.show();
                        } else if (position.equals("Accounting Manager")) {
                            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/AccountingManager/AccountingManagerDashboard.fxml"));
                            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            stage.setHeight(700);
                            stage.setWidth(1210);
                            scene = new Scene(root);
                            stage.setScene(scene);
                            stage.setResizable(false);
                            stage.show();
                        } else if (position.equals("Stock keeper")) {
                            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/StockKeeper/StockKeeperDashboard.fxml"));
                            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            stage.setHeight(700);
                            stage.setWidth(1210);
                            scene = new Scene(root);
                            stage.setScene(scene);
                            stage.setResizable(false);
                            stage.show();
                        }
                    } else {
                        Alert confirmationDialog = new Alert(Alert.AlertType.WARNING);
                        confirmationDialog.setTitle("Warning !");
                        confirmationDialog.setHeaderText("Username or Password incorrect");

                        ButtonType okButton = new ButtonType("OK");

                        confirmationDialog.showAndWait().ifPresent(response -> {
                            if (response == okButton) {
                                System.out.println("OK button clicked.");
                            }
                        });
                    }

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }else {
                invalidEmailAlert();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        animations();
    }

    private void animations(){
        FadeTransition fd = new FadeTransition(Duration.seconds(1),loginPane);
        fd.setFromValue(0.0);
        fd.setToValue(1.0);
        fd.play();

        FadeTransition fd2 = new FadeTransition(Duration.seconds(1),lblIMS);
        fd2.setFromValue(0.0);
        fd2.setToValue(1.0);
        fd2.play();

        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1), loginPane);
        translateTransition.setFromY(-100);
        translateTransition.setToY(0);
        translateTransition.play();

        TranslateTransition translateTransition2 = new TranslateTransition(Duration.seconds(1), lblIMS);
        translateTransition2.setFromY(-100);
        translateTransition2.setToY(0);
        translateTransition2.play();
    }
}