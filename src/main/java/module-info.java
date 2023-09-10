module com.example.stockportfoliomanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires com.jfoenix;

    opens com.stockportfoliomanagementsystem to javafx.fxml;
    exports com.stockportfoliomanagementsystem;
}