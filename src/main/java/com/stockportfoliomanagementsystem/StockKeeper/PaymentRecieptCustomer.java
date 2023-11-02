package com.stockportfoliomanagementsystem.StockKeeper;

import com.stockportfoliomanagementsystem.MySqlCon;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class PaymentRecieptCustomer implements Initializable{

    Connection conn = MySqlCon.MysqlMethod();
    String cid;

    private String CustomerTypeNew = AddNewCustomer.getCustomerType();
    private String CustomerTypeExisting = SelectExistingCustomer.getCustomerType();

    int pdfButtonCount = 0;
    private FileInputStream fis;
    private String pdfFilePath;
    private int invoiceRowCount;
    private String pdfFilename;
    private byte[] pdf;

    Scene scene = SellExisting.getScene();

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
        System.out.println("Customer Type: "+CustomerTypeNew);
        System.out.println("Customer Type: "+CustomerTypeExisting);

        if(CustomerTypeNew != null) {
            cid = AddNewCustomer.getCusIDNew();
            System.out.println("Customer ID: " + cid);
        }else if(CustomerTypeExisting != null){
            cid = SelectExistingCustomer.getCustomerIndex();
            System.out.println("Customer ID: " + cid);
        }

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
            column.setPrefWidth(columnWidth-2);
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
        lblDate.setText(": "+ LocalDate.now());

        String count = "SELECT COUNT(*) FROM PDF_invoices";
        try {
            PreparedStatement statement = conn.prepareStatement(count);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                invoiceRowCount = rs.getInt(1);
                System.out.println(count);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(invoiceRowCount == 0){
            lblInvoiceID.setText(": I_001");
        }else if(invoiceRowCount < 9) {
            lblInvoiceID.setText(": I_00" + (invoiceRowCount + 1));
        }else if(invoiceRowCount < 99){
            lblInvoiceID.setText(": I_0" + (invoiceRowCount + 1));
        }else{
            lblInvoiceID.setText(": I_" + (invoiceRowCount + 1));
        }

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

        lblCusID.setText(": "+cid);
        lblAddress.setText(": "+cAddress);
        lblContact.setText(": "+cContact);
        lblName.setText(": "+cName);

        String sql4= "SELECT SUM(Total) FROM temp_invoice";

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

        Platform.runLater(() -> {
            captureScne();
        });

    }

    private void captureScne() {
        String imgFilename = "img_"+ LocalDate.now()+".png";
        pdfFilename = "pdf_"+ LocalDate.now()+".pdf";

        Screen primaryScreen = Screen.getPrimary();

        double screenWidth = primaryScreen.getBounds().getWidth();
        double screenHeight = primaryScreen.getBounds().getHeight();

        System.out.println(screenWidth);
        System.out.println(screenHeight);
        // Calculate the center position
        double centerX = screenWidth / 2;
        double centerY = screenHeight / 2;

        double boundX = centerX - (850 / 2);
        double boundY = centerY - (640 / 2);

        try{
            Robot robot = new Robot();
            Rectangle rectangle = new Rectangle((int) boundX+7, (int) boundY+30,850,640);
            BufferedImage bi = robot.createScreenCapture(rectangle);
            ImageIO.write(bi, "png", new File("src/main/java/com/stockportfoliomanagementsystem/Invoices/png/"+ imgFilename));

        } catch (AWTException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String imageFilePath = "src/main/java/com/stockportfoliomanagementsystem/Invoices/png/"+ imgFilename;
        pdfFilePath = "src/main/java/com/stockportfoliomanagementsystem/Invoices/pdf/"+ pdfFilename;

        try {
            BufferedImage bufferedImage = ImageIO.read(new File(imageFilePath));
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();

            page.setMediaBox(new PDRectangle(850, 640));
            document.addPage(page);

            PDImageXObject pdImage = LosslessFactory.createFromImage(document, bufferedImage);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.drawImage(pdImage, 0,0);
            contentStream.close();

            try(OutputStream os = new FileOutputStream(pdfFilePath)) {
                document.save(os);
            }
            document.close();

            try{
                fis = new FileInputStream(pdfFilePath);
                pdf = new byte[fis.available()];
                fis.read(pdf);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String sql = "INSERT INTO PDF_invoices (invoice_id, date_, C_ID, pdf) VALUES (?,?,?,?)";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, invoiceRowCount+1);
            pstmt.setDate(2, Date.valueOf(LocalDate.now()));
            pstmt.setString(3, cid);
            pstmt.setBytes(4, pdf);

            pstmt.executeUpdate();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void showPDF() {

        try {
            InputStream pdfInputStream = new FileInputStream(pdfFilePath);
            Path tempPdfFile = Files.createTempFile("temp_pdf_", ".pdf");
            try (FileOutputStream fos = new FileOutputStream(tempPdfFile.toFile())) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = pdfInputStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }
            // Open the temporary PDF file with the default PDF viewer
            Desktop.getDesktop().open(tempPdfFile.toFile());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onBtnPDF(MouseEvent event) {
        showPDF();
    }

    public void showClickErrorDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setTitle("Warning !");

        Label messageLabel = new Label("Already Done !");
        javafx.scene.control.Button closeButton = new Button("Close");
        closeButton.setOnAction(event -> dialog.close());

        VBox dialogVBox = new VBox(10);
        dialogVBox.getChildren().addAll(messageLabel, closeButton);
        dialogVBox.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Scene dialogScene = new Scene(dialogVBox, 300, 100);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }
}
