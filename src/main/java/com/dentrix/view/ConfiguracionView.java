package com.dentrix.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;

public class ConfiguracionView {
    public Pane getView() {
        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #F1F5F9;");

        // --- BARRA DE PESTAÑAS ---
        HBox topBar = new HBox(10);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10, 15, 10, 15));
        topBar.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);");

        Button btnUsuarios = new Button("👤 Usuarios & Accesos");
        Button btnTarifario = new Button("🏷 Tarifario");
        Button btnCie10 = new Button("📖 CIE-10"); // <-- AGREGADO
        Button btnDatos = new Button("🏢 Datos Clínica");

        String styleActive = "-fx-background-color: #E0F7F6; -fx-text-fill: #0F766E; -fx-font-weight: bold; -fx-padding: 6 12; -fx-background-radius: 6;";
        String styleInactive = "-fx-background-color: transparent; -fx-text-fill: #475569; -fx-font-weight: bold; -fx-padding: 6 12; -fx-background-radius: 6; -fx-cursor: hand;";

        btnUsuarios.setStyle(styleActive);
        btnTarifario.setStyle(styleInactive);
        btnCie10.setStyle(styleInactive);
        btnDatos.setStyle(styleInactive);

        topBar.getChildren().addAll(btnUsuarios, btnTarifario, btnCie10, btnDatos); // <-- AGREGADO AL HBOX

        // --- CONTENEDOR CENTRAL ---
        StackPane contentContainer = new StackPane();
        VBox.setVgrow(contentContainer, Priority.ALWAYS);

        // --- EVENTOS DE NAVEGACIÓN ---
        btnUsuarios.setOnAction(e -> {
            resetStyles(btnUsuarios, btnTarifario, btnCie10, btnDatos);
            btnUsuarios.setStyle(styleActive);
            contentContainer.getChildren().setAll(new UsuariosView().getView());
        });

        btnTarifario.setOnAction(e -> {
            resetStyles(btnUsuarios, btnTarifario, btnCie10, btnDatos);
            btnTarifario.setStyle(styleActive);
            contentContainer.getChildren().setAll(new TarifarioView().getView()); 
        });

        btnCie10.setOnAction(e -> {
            resetStyles(btnUsuarios, btnTarifario, btnCie10, btnDatos);
            btnCie10.setStyle(styleActive);
            contentContainer.getChildren().setAll(new Cie10View().getView()); // <-- CONECTADO
        });

        btnDatos.setOnAction(e -> {
            resetStyles(btnUsuarios, btnTarifario, btnCie10, btnDatos);
            btnDatos.setStyle(styleActive);
            // Aquí llamaremos a Datos Clinica cuando lo creemos
            contentContainer.getChildren().setAll(new ConfiguracionDatosView().getView()); 
        });

        // Cargar Usuarios por defecto
        contentContainer.getChildren().add(new UsuariosView().getView());

        mainLayout.getChildren().addAll(topBar, contentContainer);
        return mainLayout;
    }

    private void resetStyles(Button... buttons) {
        for (Button btn : buttons) {
            btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #475569; -fx-font-weight: bold; -fx-padding: 6 12; -fx-background-radius: 6; -fx-cursor: hand;");
        }
    }
}