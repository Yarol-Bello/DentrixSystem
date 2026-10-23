package com.dentrix.view;

import com.dentrix.controller.PacientesController;
import com.dentrix.model.Paciente;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class PacientesView {
    public Pane getView() {
        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #F1F5F9;");

        // --- 1. BARRA SUPERIOR (BUSCADOR) ---
        HBox topBar = new HBox(10);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10, 15, 10, 15));
        topBar.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);");

        Label lblBuscar = new Label("🔍");
        TextField txtBuscar = new TextField();
        txtBuscar.setPromptText("DNI, apellido o código...");
        txtBuscar.setStyle("-fx-background-color: #F8FAFC; -fx-border-color: #CBD5E1; -fx-border-radius: 6; -fx-padding: 8;");
        HBox.setHgrow(txtBuscar, Priority.ALWAYS);
        txtBuscar.setMaxWidth(300);
        
        topBar.getChildren().addAll(lblBuscar, txtBuscar);

        // --- 2. CONTENIDO PRINCIPAL (DOS COLUMNAS) ---
        HBox contentBox = new HBox(20);
        HBox.setHgrow(contentBox, Priority.ALWAYS);

        // --- PANEL IZQUIERDO: FORMULARIO (40%) ---
        VBox formPanel = new VBox(10);
        formPanel.setPadding(new Insets(15));
        formPanel.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);");
        formPanel.setMinWidth(350);
        formPanel.setMaxWidth(380);

        Label tituloForm = new Label("Ficha Demográfica del Paciente");
        tituloForm.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #0F172A;");

        // --- CAMPOS CON RESTRICCIONES (TextFormatter) ---
        TextField txtDni = new TextField(); 
        txtDni.setPromptText("8 dígitos");
        // Bloquear si no son números o si supera los 8 caracteres
        txtDni.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d{0,8}")) return change;
            return null;
        }));

        TextField txtApellidos = new TextField(); 
        txtApellidos.setPromptText("Apellidos");
        // Solo letras y espacios
        txtApellidos.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*")) return change;
            return null;
        }));

        TextField txtNombres = new TextField(); 
        txtNombres.setPromptText("Nombres");
        // Solo letras y espacios
        txtNombres.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*")) return change;
            return null;
        }));

        TextField txtCelular = new TextField(); 
        txtCelular.setPromptText("9 dígitos");
        // Bloquear si no son números o si supera los 9 caracteres
        txtCelular.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d{0,9}")) return change;
            return null;
        }));

        DatePicker dpFecha = new DatePicker(); 
        dpFecha.setPromptText("F. Nacimiento"); 
        dpFecha.setMaxWidth(Double.MAX_VALUE);
        
        ComboBox<String> cbSexo = new ComboBox<>(); 
        cbSexo.getItems().addAll("Masculino", "Femenino"); 
        cbSexo.setPromptText("Sexo"); 
        cbSexo.setMaxWidth(Double.MAX_VALUE);
        
        TextField txtCorreo = new TextField(); 
        txtCorreo.setPromptText("Correo");
        
        TextField txtDireccion = new TextField(); 
        txtDireccion.setPromptText("Dirección");
        
        TextArea txtAlergias = new TextArea(); 
        txtAlergias.setPromptText("Alergias..."); 
        txtAlergias.setPrefRowCount(2);

        Button btnGuardar = new Button("💾 Guardar Paciente");
        btnGuardar.setMaxWidth(Double.MAX_VALUE);
        btnGuardar.setStyle("-fx-background-color: #00A896; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10; -fx-cursor: hand; -fx-background-radius: 6;");

        formPanel.getChildren().addAll(tituloForm, new Separator(),
                new Label("DNI / Identificación *"), txtDni, 
                new Label("Apellidos *"), txtApellidos, 
                new Label("Nombres *"), txtNombres, 
                new Label("F. Nacimiento"), dpFecha, 
                new Label("Sexo"), cbSexo, 
                new Label("Celular *"), txtCelular, 
                new Label("Correo"), txtCorreo, 
                new Label("Dirección"), txtDireccion, 
                new Label("Alergias"), txtAlergias, btnGuardar);

        // --- PANEL DERECHO: TABLA (60%) ---
        VBox tablePanel = new VBox(10);
        tablePanel.setPadding(new Insets(15));
        tablePanel.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);");
        HBox.setHgrow(tablePanel, Priority.ALWAYS);

        Label tituloTabla = new Label("Padrón General Registrado");
        tituloTabla.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #0F172A;");

        TableView<Paciente> tabla = new TableView<>();
        VBox.setVgrow(tabla, Priority.ALWAYS);

        tablePanel.getChildren().addAll(tituloTabla, tabla);
        
        contentBox.getChildren().addAll(formPanel, tablePanel);
        mainLayout.getChildren().addAll(topBar, contentBox);

        // --- CONECTAR CON EL CONTROLADOR ---
        PacientesController controller = new PacientesController(tabla, txtDni, txtApellidos, txtNombres, 
                txtCelular, txtCorreo, txtDireccion, dpFecha, cbSexo, txtAlergias, txtBuscar, btnGuardar);
        
        btnGuardar.setOnAction(e -> controller.guardarPaciente());

        return mainLayout;
    }
}