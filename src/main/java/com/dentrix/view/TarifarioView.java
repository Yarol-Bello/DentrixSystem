package com.dentrix.view;

import com.dentrix.controller.TarifarioController;
import com.dentrix.model.Procedimiento;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class TarifarioView {
    public Pane getView() {
        HBox contentBox = new HBox(20);
        contentBox.setPadding(new Insets(20));
        contentBox.setStyle("-fx-background-color: #F1F5F9;");

        // Panel Izquierdo: Formulario
        VBox formPanel = new VBox(10);
        formPanel.setPadding(new Insets(15));
        formPanel.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);");
        formPanel.setMinWidth(350); formPanel.setMaxWidth(380);

        Label tituloForm = new Label("Nuevo Procedimiento");
        tituloForm.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");

        TextField txtCodigo = new TextField(); txtCodigo.setPromptText("TRAT-06");
        TextField txtNombre = new TextField(); txtNombre.setPromptText("Ej: Endodoncia...");
        TextField txtPrecio = new TextField(); txtPrecio.setPromptText("0.00");
        
        ComboBox<String> cbEsp = new ComboBox<>();
        cbEsp.getItems().addAll("Operatoria", "Periodoncia", "Ortodoncia", "Endodoncia", "Cirugía");
        cbEsp.setPromptText("Especialidad"); cbEsp.setMaxWidth(Double.MAX_VALUE);
        
        ComboBox<String> cbCalc = new ComboBox<>();
        cbCalc.getItems().addAll("Por Pieza", "Sesión", "Mensual");
        cbCalc.setPromptText("Cálculo"); cbCalc.setMaxWidth(Double.MAX_VALUE);

        Button btnGuardar = new Button("💾 Crear Procedimiento");
        btnGuardar.setMaxWidth(Double.MAX_VALUE);
        btnGuardar.setStyle("-fx-background-color: #00A896; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10; -fx-cursor: hand;");

        formPanel.getChildren().addAll(tituloForm, new Separator(),
                new Label("Código *"), txtCodigo, new Label("Nombre *"), txtNombre, 
                new Label("Precio *"), txtPrecio, new Label("Especialidad *"), cbEsp, 
                new Label("Cálculo *"), cbCalc, btnGuardar);

        // Panel Derecho: Tabla
        VBox tablePanel = new VBox(10);
        tablePanel.setPadding(new Insets(15));
        tablePanel.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);");
        HBox.setHgrow(tablePanel, Priority.ALWAYS);

        HBox headerTabla = new HBox(10);
        headerTabla.setAlignment(Pos.CENTER_LEFT);
        TextField txtBuscar = new TextField(); txtBuscar.setPromptText("Buscar por nombre o código...");
        txtBuscar.setStyle("-fx-background-color: #F8FAFC; -fx-border-color: #CBD5E1; -fx-border-radius: 6; -fx-padding: 8;");
        HBox.setHgrow(txtBuscar, Priority.ALWAYS);
        
        Button btnNuevo = new Button("➕ Nuevo");
        btnNuevo.setStyle("-fx-background-color: #00A896; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        headerTabla.getChildren().addAll(txtBuscar, btnNuevo);

        TableView<Procedimiento> tabla = new TableView<>();
        VBox.setVgrow(tabla, Priority.ALWAYS);

        tablePanel.getChildren().addAll(headerTabla, tabla);

        contentBox.getChildren().addAll(formPanel, tablePanel);

        // --- CONECTAR CONTROLADOR ---
        TarifarioController controller = new TarifarioController(tabla, txtCodigo, txtNombre, txtPrecio, 
                cbEsp, cbCalc, txtBuscar, btnGuardar, tituloForm);

        btnGuardar.setOnAction(e -> controller.guardarProcedimiento());
        btnNuevo.setOnAction(e -> {
            txtCodigo.clear(); txtNombre.clear(); txtPrecio.clear(); 
            cbEsp.setValue(null); cbCalc.setValue(null); 
            txtCodigo.requestFocus();
            btnGuardar.setText("💾 Crear Procedimiento");
            tituloForm.setText("Nuevo Procedimiento");
        });

        return contentBox;
    }
}