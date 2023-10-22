package com.stockportfoliomanagementsystem.PortfolioManager;

import com.stockportfoliomanagementsystem.MainController;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PortfolioManagerController implements Initializable{

    MainController mainController = new MainController();
    String Fname = mainController.getFname();
    String Lname = mainController.getLname();
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
    public void manageUsers(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/com/stockportfoliomanagementsystem/ManageUsers.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setHeight(700);
        stage.setWidth(1210);
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(Fname+" PM "+Lname);
        txtName.setText(Fname+" "+Lname);

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