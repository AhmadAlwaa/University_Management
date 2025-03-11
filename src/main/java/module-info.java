module com.example.university_management {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires org.apache.poi.ooxml.schemas;
    requires org.apache.xmlbeans;
    requires org.apache.logging.log4j;
    requires org.apache.commons.collections4;
    requires java.desktop;

    opens com.example.university_management to javafx.fxml;
    exports com.example.university_management;
}