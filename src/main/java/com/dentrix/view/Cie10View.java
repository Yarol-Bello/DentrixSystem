package com.dentrix.view;

import com.dentrix.controller.Cie10Controller;
import com.dentrix.model.Cie10;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class Cie10View {
    public Pane getView() {
        HBox contentBox = new HBox(20);
        contentBox.setPadding(new Insets(20));
        contentBox.setStyle("-fx-background-color: #F1F5F9;");

        // Panel Izquierdo: Ficha Técnica
        VBox panelDetalle = new VBox(10);
        panelDetalle.setPadding(new Insets(15));
        panelDetalle.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);");
        panelDetalle.setMinWidth(350); panelDetalle.setMaxWidth(380);

        Label tituloFicha = new Label("Ficha Técnica CIE-10");
        tituloFicha.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");

        // Tarjeta Morada (Código)
        VBox cardCodigo = new VBox(2);
        cardCodigo.setStyle("-fx-background-color: #F5F3FF; -fx-border-color: #DDD6FE; -fx-padding: 10; -fx-background-radius: 8; -fx-border-radius: 8;");
        Label lblCodigo = new Label("K02.1");
        lblCodigo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #6D28D9; -fx-font-family: monospace;");
        Label lblGrupo = new Label("K02 Caries dental");
        lblGrupo.setStyle("-fx-font-size: 10px; -fx-text-fill: #8B5CF6; -fx-font-weight: bold;");
        Label lblPrioridad = new Label("⭐ FRECUENTE");
        lblPrioridad.setStyle("-fx-background-color: #FEF3C7; -fx-text-fill: #B45309; -fx-font-size: 10px; -fx-font-weight: bold; -fx-padding: 2 6; -fx-background-radius: 4;");
        cardCodigo.getChildren().addAll(lblCodigo, lblGrupo, lblPrioridad);

        TextField txtNombre = new TextField(); txtNombre.setEditable(false); txtNombre.setStyle("-fx-background-color: #F8FAFC; -fx-font-weight: bold;");
        TextArea txtDescripcion = new TextArea(); txtDescripcion.setEditable(false); txtDescripcion.setWrapText(true); txtDescripcion.setPrefRowCount(5);

        panelDetalle.getChildren().addAll(tituloFicha, new Separator(), cardCodigo, 
                new Label("Denominación Oficial:"), txtNombre, 
                new Label("Criterio Clínico OMS:"), txtDescripcion);

        // Panel Derecho: Tabla
        VBox panelTabla = new VBox(10);
        panelTabla.setPadding(new Insets(15));
        panelTabla.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);");
        HBox.setHgrow(panelTabla, Priority.ALWAYS);

        TextField txtBuscar = new TextField(); txtBuscar.setPromptText("Buscar por código o diagnóstico...");
        txtBuscar.setStyle("-fx-background-color: #F8FAFC; -fx-border-color: #CBD5E1; -fx-border-radius: 6; -fx-padding: 8;");

        TableView<Cie10> tabla = new TableView<>();
        VBox.setVgrow(tabla, Priority.ALWAYS);

        panelTabla.getChildren().addAll(txtBuscar, tabla);

        contentBox.getChildren().addAll(panelDetalle, panelTabla);

        // --- CONECTAR CONTROLADOR ---
        Cie10Controller controller = new Cie10Controller(tabla, lblCodigo, lblGrupo, lblPrioridad, 
                txtNombre, txtDescripcion, txtBuscar);

        return contentBox;
    }
}