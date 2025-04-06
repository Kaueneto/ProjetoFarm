module com.example.provan1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires java.desktop;
    opens com.example.provan1.controller to javafx.fxml;
    opens com.example.provan1 to javafx.fxml;
    exports com.example.provan1;
}