package com.stockportfoliomanagementsystem.StockKeeper;

import com.stockportfoliomanagementsystem.MainController;
import com.stockportfoliomanagementsystem.MySqlCon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class StockKeeperController implements Initializable {
    static Connection conn = MySqlCon.MysqlMethod();

    @FXML
    private LineChart<?, ?> lineChart;

    @FXML
    private PieChart pieChart;

    @FXML
    private Circle circle;

    @FXML
    private Label lblAVG;

    @FXML
    private Label lblBought;

    @FXML
    private Label lblSold;
    

    @FXML
    private Label txtName;


    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private ImageView imageView;

    MainController mainController = new MainController();
    String Fname = mainController.getFname();
    String Lname = mainController.getLname();
    String username = mainController.getUsername();
    String password = mainController.getPwd();
    private double total;
    byte[] image = new byte[1024];
    private double bought;
    private double sold;

    @FXML
    void onReportsButton(MouseEvent event) {
    //ADD =================================================================================
    }


    @FXML
    void onEditProfile(MouseEvent event) {
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
    void onCustomersButton(MouseEvent event) throws IOException {
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

    @FXML
    void onSellProducts(MouseEvent event) throws IOException {

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
    void onLogOutButton(MouseEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/Main.fxml"));
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
    void onRefreshButton(MouseEvent event) {
        loadFromDB();
    }




    @FXML
    void onSupplierButton(MouseEvent event) throws IOException {
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadFromDB();
    }

    private void loadFromDB(){
        String sql = "SELECT Pic FROM Users WHERE Username = ?";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                InputStream is = rs.getBinaryStream("Pic");

                // Read the image data and save it to a file

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

                    Image image = new Image(new FileInputStream("photo.jpg"));
                    circle.setFill(new ImagePattern(image));

                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException f) {
            throw new RuntimeException(f);
        } catch (IOException g) {
            throw new RuntimeException(g);
        }

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
        System.out.println(Fname+" PM "+Lname);
        txtName.setAlignment(Pos.CENTER);
        txtName.setText(Fname+" "+Lname);

        String goodsSold = "SELECT SUM(Total) AS Sold\n" +
                "FROM transactions_cus\n" +
                "WHERE YEAR(Date_) = YEAR(CURDATE()) \n" +
                "AND MONTH(Date_) = MONTH(CURDATE())";

        try {
            PreparedStatement pstmt = conn.prepareStatement(goodsSold);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                sold = rs.getDouble("Sold");
            }
            System.out.println(sold);
            lblSold.setText("LKR "+sold);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String goodsBought = "SELECT SUM(Total) AS Bought\n" +
                "FROM transactions_sup\n" +
                "WHERE YEAR(Date_) = YEAR(CURDATE()) \n" +
                "AND MONTH(Date_) = MONTH(CURDATE())";

        try {
            PreparedStatement pstmt = conn.prepareStatement(goodsBought);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                bought= rs.getDouble("Bought");
            }
            System.out.println(bought);
            lblBought.setText("LKR "+bought);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        ObservableList<PieChart.Data> pieChartData = fetchDataFromDatabase();

        pieChart.setData(pieChartData);

        pieChartData.forEach(data ->
                data.nameProperty().setValue(data.getName() + "\nAmount: " + ((int) data.getPieValue()))
        );
    }
    public static void dbUpdate(){
        String stockTotal = "UPDATE stock SET Total = Selling_price*Qty";

        try {
            PreparedStatement pstmt = conn.prepareStatement(stockTotal);
            pstmt.executeUpdate();
            System.out.println("stock update");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ObservableList<PieChart.Data> fetchDataFromDatabase() {


        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT P_Name, Qty FROM stock");

            while (resultSet.next()) {
                String category = resultSet.getString("P_Name");
                int value = resultSet.getInt("Qty");

                // Create a PieChart.Data object and add it to the list
                pieChartData.add(new PieChart.Data(category, value));
            }

            resultSet.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pieChartData;
    }
}