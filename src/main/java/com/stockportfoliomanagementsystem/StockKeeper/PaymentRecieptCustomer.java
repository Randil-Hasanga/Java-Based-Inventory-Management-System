package com.stockportfoliomanagementsystem.StockKeeper;

import com.stockportfoliomanagementsystem.MySqlCon;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class PaymentRecieptCustomer implements Initializable {

    Connection conn = MySqlCon.MysqlMethod();
    String cid = SelectExistingCustomer.getCustomerIndex();

    Scene scene = BuyExisting.getScene();

    private String cName;
    private String cAddress;
    private String cContact;
    private double Total;

    @FXML
    private Stage stage;
    private Parent root;


    @FXML
    private Label lblAddress;

    @FXML
    private Label lblContact;

    @FXML
    private Label lblCusID;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblInvoiceID;

    @FXML
    private Label lblName;

    @FXML
    private Label txtTotal;

    @FXML
    private TableView<ObservableList<String>> tblInvoice;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadInvoiceFromDB();
        tblInvoice.setSelectionModel(null);

    }

    private void loadInvoiceFromDB() {

        ObservableList<TableColumn<ObservableList<String>, ?>> columns = tblInvoice.getColumns();
        columns.clear();
        String[] columnNames = {"Product ID","Product Name","Quantity","Price","Amount"};

        double columnWidth = tblInvoice.getPrefWidth() / columnNames.length;

        // Add the columns to the cart table with fixed names
        for (int i = 0; i < columnNames.length; i++) {
            final int columnIndex = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(columnNames[i]);
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(columnIndex)));
            column.setPrefWidth(columnWidth);
            columns.add(column);
        }

        String sql = "SELECT temp_invoice.P_ID, stock.P_Name,temp_invoice.Qty,temp_invoice.Price, temp_invoice.Total FROM stock, temp_invoice WHERE stock.P_ID = temp_invoice.P_ID";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= columnNames.length; i++) {
                    row.add(rs.getString(i));
                }
                data.add(row);
            }

            tblInvoice.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        lblDate.setText(String.valueOf(java.time.LocalDate.now()));;

        String sql3= "SELECT C_ID, C_Name, C_Location, C_Contact FROM customer WHERE C_ID = ?";// Aluthen pdf invoice table ekak hadala e table eke count ek aran invoice id ek hdnn ona

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql3);
            preparedStatement.setString(1,cid);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                cName = rs.getString("C_Name");
                cAddress = rs.getString("C_Location");
                cContact = rs.getString("C_Contact");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        lblCusID.setText(cid);
        lblAddress.setText(cAddress);
        lblContact.setText(cContact);
        lblName.setText(cName);

        String sql4= "SELECT SUM(Total) FROM temp_invoice";// Aluthen pdf invoice table ekak hadala e table eke count ek aran invoice id ek hdnn ona

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql4);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                Total = rs.getDouble(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        txtTotal.setText(String.valueOf(Total));

        String sql2 = "DELETE FROM temp_invoice";

        try {
            PreparedStatement pstmt2 = conn.prepareStatement(sql2);
            pstmt2.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        captureScne();
    }

    private void captureScne() {
        String filename = "img_"+LocalDate.now()+".png";
        try{
            Robot robot = new Robot();
            Rectangle rectangle = new Rectangle(600,400);
            BufferedImage bi = robot.createScreenCapture(rectangle);
            ImageIO.write(bi, "png", new File("src/main/java/com/stockportfoliomanagementsystem/Invoices/pdf/"+ filename));



        } catch (AWTException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        WritableImage image = scene.snapshot(null);
//
//        // Save the captured image to a file (PNG)
//        try {
//            File imageFile = new File("scene_capture.png");
//            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", imageFile);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @FXML
    void onBtnPDF(MouseEvent event) {
        captureScne();
    }
}
