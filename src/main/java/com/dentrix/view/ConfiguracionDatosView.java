package com.dentrix.view;

import com.dentrix.controller.ConfiguracionController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class ConfiguracionDatosView {
    public Pane getView() {
        HBox contentBox = new HBox(20);
        contentBox.setPadding(new Insets(20));
        contentBox.setStyle("-fx-background-color: #F1F5F9;");

        // --- PANEL IZQUIERDO: DATOS FISCALES (40%) ---
        VBox panelFiscal = new VBox(10);
        panelFiscal.setPadding(new Insets(15));
        panelFiscal.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);");
        panelFiscal.setMinWidth(350); panelFiscal.setMaxWidth(400);

        Label tituloFiscal = new Label("Datos Fiscales");
        tituloFiscal.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");

        TextField txtRuc = new TextField();
        TextField txtRazonSocial = new TextField();
        TextField txtNombreComercial = new TextField();
        TextField txtDireccion = new TextField();
        TextField txtSerie = new TextField();
        TextField txtCelularYape = new TextField();
        TextField txtTitularYape = new TextField();

        Button btnGuardar = new Button("💾 Guardar Parámetros");
        btnGuardar.setMaxWidth(Double.MAX_VALUE);
        btnGuardar.setStyle("-fx-background-color: #00A896; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10; -fx-cursor: hand;");

        panelFiscal.getChildren().addAll(tituloFiscal, new Separator(),
                new Label("RUC *"), txtRuc, new Label("Razón Social *"), txtRazonSocial,
                new Label("Nombre Comercial *"), txtNombreComercial, new Label("Dirección Fiscal *"), txtDireccion,
                new Label("Serie Boleta *"), txtSerie, 
                new Label("Celular Yape/Plin *"), txtCelularYape, new Label("Titular Yape/Plin *"), txtTitularYape,
                btnGuardar);

        // --- PANEL DERECHO: ACREDITACIÓN (60%) ---
        VBox panelAcreditacion = new VBox(10);
        panelAcreditacion.setPadding(new Insets(15));
        panelAcreditacion.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);");
        HBox.setHgrow(panelAcreditacion, Priority.ALWAYS);

        Label tituloAcred = new Label("Acreditación Profesional");
        tituloAcred.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");

        TextField txtDirector = new TextField();
        TextField txtCop = new TextField();
        TextField txtRne = new TextField();
        TextField txtLicencia = new TextField();

        panelAcreditacion.getChildren().addAll(tituloAcred, new Separator(),
                new Label("Director Médico *"), txtDirector, 
                new Label("COP *"), txtCop, 
                new Label("RNE"), txtRne, 
                new Label("Licencia Municipal"), txtLicencia);

        contentBox.getChildren().addAll(panelFiscal, panelAcreditacion);

        // --- CONECTAR CONTROLADOR ---
        ConfiguracionController controller = new ConfiguracionController(
                txtRuc, txtRazonSocial, txtNombreComercial, txtDireccion, txtSerie, 
                txtCelularYape, txtTitularYape, txtDirector, txtCop, txtRne, txtLicencia
        );

        btnGuardar.setOnAction(e -> controller.guardarDatos());

        return contentBox;
    }
}