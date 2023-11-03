package com.stockportfoliomanagementsystem.PortfolioManager;

import com.stockportfoliomanagementsystem.MainController;
import com.stockportfoliomanagementsystem.MySqlCon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class PortfolioManagerController implements Initializable{

    Connection conn = MySqlCon.MysqlMethod();
    MainController mainController = new MainController();
    String Fname = mainController.getFname();
    String Lname = mainController.getLname();
    String username = mainController.getUsername();
    String password = mainController.getPwd();

    byte[] image = new byte[1024];
    @FXML
    private FontAwesomeIconView IconSignOut;
    @FXML
    private LineChart<?, ?> lineChart;
    @FXML
    private PieChart pieChart;
    @FXML
    private Label txtName;
    @FXML
    private SVGPath svgIco;
    @FXML
    private VBox legendContainer;
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private ImageView imageView;
    @FXML
    private Image image1;
    private double total;
    @FXML
    private Label lblAVG;


    @FXML
    void onReportButton(MouseEvent event) {
        //To Complete -------------------------------------------------------------
    }
    @FXML
    public void manageUsers(MouseEvent event){
        try {
            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/PortfolioManager/ManageUsers.fxml"));
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
    void onEditProfile(MouseEvent event) throws IOException {
        try {
            // Load the FXML file for the new window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/stockportfoliomanagementsystem/PortfolioManager/EditProfilePM.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage newStage = new Stage();

            // Set the FXML content as the scene for the new stage
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.setResizable(false);
            // Show the new stage
            newStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onStockButton(MouseEvent event){
        try {
            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/PortfolioManager/viewStock.fxml"));
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
    void onSupplierButton(MouseEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/PortfolioManager/viewSuppliers.fxml"));
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
    void onCustomerButton(MouseEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/PortfolioManager/viewCustomers.fxml"));
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

    private void showPicture(){
        String sql = "SELECT Pic FROM Users WHERE Username = ?";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                InputStream is = rs.getBinaryStream("Pic");

                if(is!=null) {
                    // Read the image data and save it to a file
                    OutputStream os = new FileOutputStream(new File("photo.jpg"));
                    byte[] content = new byte[1024];
                    int size = 0;

                    while ((size = is.read(content)) != -1) {
                        os.write(content, 0, size);
                    }
                    os.close();
                    is.close();

                    // Create a circular mask for the ImageView
                    Circle clip = new Circle(imageView.getFitWidth() / 2, imageView.getFitHeight() / 2, imageView.getFitWidth() / 2);
                    imageView.setClip(clip);

                    // Load the image and set it to the ImageView
                    imageView.setImage(new Image("file:photo.jpg"));
                    imageView.setPreserveRatio(true);

                    // Set the dimensions of the ImageView
                    imageView.setFitWidth(50);
                    imageView.setFitHeight(50);

                    // Set a border and make the image circular
                    imageView.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-background-color: white;");
                }else{
                    System.out.println("No image");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException f) {
            throw new RuntimeException(f);
        } catch (IOException g) {
            throw new RuntimeException(g);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        String totalSQL = "SELECT SUM(Total) AS TotalStock FROM stock";

        try {
            PreparedStatement pstmt = conn.prepareStatement(totalSQL);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                total = rs.getDouble("TotalStock");
            }
            System.out.println(total);
            lblAVG.setText("LKR "+total);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        showPicture();

        System.out.println(Fname+" PM "+Lname);
        txtName.setText(Fname+" "+Lname);

//pie chart
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Pens", 1200),
                        new PieChart.Data("Pencils", 1500),
                        new PieChart.Data("CR pg80 Single", 600),
                        new PieChart.Data("CR pg 80 Square", 400),
                        new PieChart.Data("CR pg 120 Single", 1000),
                        new PieChart.Data("CR pg 120 Square", 350),
                        new PieChart.Data("Pastel Large", 400)
                );

        pieChartData.forEach(data ->
                data.nameProperty().setValue(data.getName() + "\namount: " + data.getPieValue())
        );


        pieChart.getData().addAll(pieChartData);
        //pieChart.setLegendVisible(true);

        //Inventory
        ObservableList<LineChart.Data> lineChartData =
                FXCollections.observableArrayList(
                        new LineChart.Data("Jan", 35000),
                        new LineChart.Data("Feb", 22500),
                        new LineChart.Data("Mar", 40000),
                        new LineChart.Data("Apr", 44000),
                        new LineChart.Data("May", 11000),
                        new LineChart.Data("Jun", 38500),
                        new LineChart.Data("Jul", 40000),
                        new LineChart.Data("Aug", 12000),
                        new LineChart.Data("Sep", 15000),
                        new LineChart.Data("Oct", 29000),
                        new LineChart.Data("Nov", 41000),
                        new LineChart.Data("Dec", 26000)
                );

        //Sales
        ObservableList<LineChart.Data> lineChartData1 =
                FXCollections.observableArrayList(
                        new LineChart.Data("Jan", 45000),
                        new LineChart.Data("Feb", 30000),
                        new LineChart.Data("Mar", 35000),
                        new LineChart.Data("Apr", 45000),
                        new LineChart.Data("May", 10000),
                        new LineChart.Data("Jun", 35000),
                        new LineChart.Data("Jul", 48000),
                        new LineChart.Data("Aug", 12500),
                        new LineChart.Data("Sep", 20000),
                        new LineChart.Data("Oct", 25000),
                        new LineChart.Data("Nov", 45000),
                        new LineChart.Data("Dec", 35000)
                );

        //Profit of Loss
        ObservableList<LineChart.Data> lineChartData2 =
                FXCollections.observableArrayList(
                        new LineChart.Data("Jan", 10000),
                        new LineChart.Data("Feb", 7500),
                        new LineChart.Data("Mar", -5000),
                        new LineChart.Data("Apr", 1000),
                        new LineChart.Data("May", -1000),
                        new LineChart.Data("Jun", -3500),
                        new LineChart.Data("Jul", 8000),
                        new LineChart.Data("Aug", 500),
                        new LineChart.Data("Sep", 5000),
                        new LineChart.Data("Oct", -4000),
                        new LineChart.Data("Nov", 4000),
                        new LineChart.Data("Dec", 9000)
                );

        lineChart.getData().addAll(new LineChart.Series("Inventory at\nBeginning of the month", lineChartData));
        lineChart.getData().addAll(new LineChart.Series("Sales at \nEnd of the month", lineChartData1));
        lineChart.getData().addAll(new LineChart.Series("Profit or Loss", lineChartData2));

    }

}