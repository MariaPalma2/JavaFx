module org.palmamaria.gestionrestaurante {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jasperreports;


    opens org.palmamaria.gestionrestaurante to javafx.fxml;
    exports org.palmamaria.gestionrestaurante;
    exports org.palmamaria.gestionrestaurante.modelos;
    opens org.palmamaria.gestionrestaurante.modelos to javafx.fxml;
    exports org.palmamaria.gestionrestaurante.controllers;
    opens org.palmamaria.gestionrestaurante.controllers to javafx.fxml;
}