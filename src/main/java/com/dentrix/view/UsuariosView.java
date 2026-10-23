package com.dentrix.view;

import com.dentrix.controller.UsuariosController;
import com.dentrix.model.Usuario;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class UsuariosView {
    public Pane getView() {
        HBox contentBox = new HBox(20);
        contentBox.setPadding(new Insets(20));
        contentBox.setStyle("-fx-background-color: #F1F5F9;");

        // Panel Izquierdo: Formulario
        VBox formPanel = new VBox(10);
        formPanel.setPadding(new Insets(15));
        formPanel.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);");
        formPanel.setMinWidth(350); formPanel.setMaxWidth(380);

        Label tituloForm = new Label("Nuevo Usuario");
        tituloForm.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");

        TextField txtDni = new TextField(); txtDni.setPromptText("8 dígitos");
        TextField txtApellidos = new TextField(); txtApellidos.setPromptText("Apellidos");
        TextField txtNombres = new TextField(); txtNombres.setPromptText("Nombres");
        TextField txtUsuario = new TextField(); txtUsuario.setPromptText("ej: jperez");
        PasswordField txtPassword = new PasswordField(); txtPassword.setPromptText("Contraseña");
        
        ComboBox<String> cbRol = new ComboBox<>(); cbRol.getItems().addAll("ADMIN", "ODONTOLOGO", "RECEPCION"); cbRol.setPromptText("Rol"); cbRol.setMaxWidth(Double.MAX_VALUE);
        ComboBox<String> cbConsultorio = new ComboBox<>(); cbConsultorio.getItems().addAll("Consultorio 01", "Consultorio 02", "Recepción"); cbConsultorio.setPromptText("Consultorio"); cbConsultorio.setMaxWidth(Double.MAX_VALUE);

        Button btnGuardar = new Button("💾 Crear Usuario");
        btnGuardar.setMaxWidth(Double.MAX_VALUE);
        btnGuardar.setStyle("-fx-background-color: #00A896; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10; -fx-cursor: hand;");

        formPanel.getChildren().addAll(tituloForm, new Separator(),
                new Label("DNI *"), txtDni, new Label("Apellidos *"), txtApellidos,
                new Label("Nombres *"), txtNombres, new Label("Usuario *"), txtUsuario,
                new Label("Contraseña *"), txtPassword, new Label("Rol *"), cbRol,
                new Label("Consultorio *"), cbConsultorio, btnGuardar);

        // Panel Derecho: Tabla
        VBox tablePanel = new VBox(10);
        tablePanel.setPadding(new Insets(15));
        tablePanel.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);");
        HBox.setHgrow(tablePanel, Priority.ALWAYS);

        TextField txtBuscar = new TextField(); txtBuscar.setPromptText("Buscar por DNI, usuario o apellido...");
        txtBuscar.setStyle("-fx-background-color: #F8FAFC; -fx-border-color: #CBD5E1; -fx-border-radius: 6; -fx-padding: 8;");

        TableView<Usuario> tabla = new TableView<>();
        VBox.setVgrow(tabla, Priority.ALWAYS);

        tablePanel.getChildren().addAll(txtBuscar, tabla);

        contentBox.getChildren().addAll(formPanel, tablePanel);

        // --- CONECTAR CONTROLADOR ---
        UsuariosController controller = new UsuariosController(tabla, txtDni, txtApellidos, txtNombres, 
                txtUsuario, txtPassword, cbRol, cbConsultorio, txtBuscar, btnGuardar, tituloForm);

        btnGuardar.setOnAction(e -> controller.guardarUsuario());

        return contentBox;
    }
}