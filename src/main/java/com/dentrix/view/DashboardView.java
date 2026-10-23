package com.dentrix.view;

import com.dentrix.controller.DashboardController;
import com.dentrix.model.Usuario;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class DashboardView {
    private List<Button> botonesMenu = new ArrayList<>();

    public void mostrar(Stage stage, Usuario usuario) {
        BorderPane root = new BorderPane();
        
        DashboardController controller = new DashboardController(root, usuario);

        // --- SIDEBAR (MENÚ LATERAL CON RBAC) ---
        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: white; -fx-border-color: transparent #E2E8F0 transparent transparent; -fx-min-width: 220;");

        Label titulo = new Label("Dentrix");
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #0F766E;");
        Label subtitulo = new Label("Centro Odontológico");
        subtitulo.setStyle("-fx-font-size: 11px; -fx-text-fill: #64748B;");
        
        // --- BOTONES BASE (Todos tienen acceso a Inicio) ---
        Button btnInicio = crearBotonMenu("Inicio");
        botonesMenu.add(btnInicio);
        btnInicio.setOnAction(e -> { controller.abrirModulo("INICIO"); marcarBotonActivo(btnInicio); });
        sidebar.getChildren().addAll(titulo, subtitulo, btnInicio);

        // --- RBAC: CONTROL DE ACCESO SEGÚN EL ROL ---
        String rol = usuario.getRol();

        if (rol.equals("ADMIN") || rol.equals("RECEPCION")) {
            Button btnRecepcion = crearBotonMenu("Recepción / Asistencia");
            botonesMenu.add(btnRecepcion);
            btnRecepcion.setOnAction(e -> { controller.abrirModulo("RECEPCION"); marcarBotonActivo(btnRecepcion); });
            sidebar.getChildren().add(btnRecepcion);
        }

        if (rol.equals("ADMIN") || rol.equals("RECEPCION") || rol.equals("ODONTOLOGO")) {
            Button btnPacientes = crearBotonMenu("Pacientes");
            botonesMenu.add(btnPacientes);
            btnPacientes.setOnAction(e -> { controller.abrirModulo("PACIENTES"); marcarBotonActivo(btnPacientes); });
            sidebar.getChildren().add(btnPacientes);
        }

        if (rol.equals("ADMIN") || rol.equals("RECEPCION") || rol.equals("ODONTOLOGO")) {
            Button btnAgenda = crearBotonMenu("Agenda Citas");
            botonesMenu.add(btnAgenda);
            btnAgenda.setOnAction(e -> { controller.abrirModulo("AGENDA"); marcarBotonActivo(btnAgenda); });
            sidebar.getChildren().add(btnAgenda);
        }

        if (rol.equals("ADMIN") || rol.equals("ODONTOLOGO")) {
            Button btnFicha = crearBotonMenu("Ficha Clínica");
            botonesMenu.add(btnFicha);
            btnFicha.setOnAction(e -> { controller.abrirModulo("FICHA"); marcarBotonActivo(btnFicha); });
            sidebar.getChildren().add(btnFicha);
        }

        if (rol.equals("ADMIN") || rol.equals("RECEPCION")) {
            Button btnCaja = crearBotonMenu("Caja / Cobros");
            botonesMenu.add(btnCaja);
            btnCaja.setOnAction(e -> { controller.abrirModulo("CAJA"); marcarBotonActivo(btnCaja); });
            sidebar.getChildren().add(btnCaja);
        }

        if (rol.equals("ADMIN") || rol.equals("RECEPCION")) {
            Button btnReportes = crearBotonMenu("Reportes");
            botonesMenu.add(btnReportes);
            btnReportes.setOnAction(e -> { controller.abrirModulo("REPORTES"); marcarBotonActivo(btnReportes); });
            sidebar.getChildren().add(btnReportes);
        }

        // SOLO EL ADMINISTRADOR VE CONFIGURACIÓN
        if (rol.equals("ADMIN")) {
            Button btnConfig = crearBotonMenu("Configuración");
            botonesMenu.add(btnConfig);
            btnConfig.setOnAction(e -> { controller.abrirModulo("CONFIG"); marcarBotonActivo(btnConfig); });
            sidebar.getChildren().add(btnConfig);
        }

        // --- PERFIL DEL USUARIO Y CERRAR SESIÓN ---
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Label lblUser = new Label(usuario.getNombres() + " " + usuario.getApellidos());
        lblUser.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");
        Label lblRol = new Label(usuario.getRol());
        lblRol.setStyle("-fx-text-fill: #64748B; -fx-font-size: 11px;");

        Button btnLogout = new Button("Cerrar Sesión");
        btnLogout.setStyle("-fx-background-color: #FEF2F2; -fx-text-fill: #DC2626; -fx-font-weight: bold; -fx-cursor: hand;");
        btnLogout.setMaxWidth(Double.MAX_VALUE);
        btnLogout.setOnAction(e -> {
            com.dentrix.util.SesionGlobal.usuarioLogueado = null;
            LoginView login = new LoginView();
            login.mostrar(new Stage());
            ((Stage) btnLogout.getScene().getWindow()).close();
        });

        VBox userBox = new VBox(5, lblUser, lblRol, btnLogout);
        userBox.setPadding(new Insets(20, 0, 0, 0));

        sidebar.getChildren().addAll(spacer, userBox);
        root.setLeft(sidebar);

        // Cargar panel de inicio por defecto y pintar su botón
        controller.abrirModulo("INICIO");
        marcarBotonActivo(btnInicio);

        Scene scene = new Scene(root, 1200, 700);
        scene.getStylesheets().add(getClass().getResource("/styles/dashboard.css").toExternalForm());
        stage.setTitle("Dentrix Dental - Panel de Control");
        stage.setScene(scene);
        stage.setMinWidth(1000);
        stage.setMinHeight(600);
        stage.show();
    }

    // Método para crear el botón con su estilo por defecto (inactivo)
    private Button crearBotonMenu(String texto) {
        Button btn = new Button(texto);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #475569; -fx-font-size: 13px; -fx-font-weight: bold; -fx-alignment: center-left; -fx-padding: 10 15; -fx-cursor: hand;");
        return btn;
    }

    // Método que apaga todos los botones y enciende el que se presionó
    private void marcarBotonActivo(Button botonActivo) {
        for (Button btn : botonesMenu) {
            // Color inactivo (gris)
            btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #475569; -fx-font-size: 13px; -fx-font-weight: bold; -fx-alignment: center-left; -fx-padding: 10 15; -fx-cursor: hand;");
        }
        // Color activo (verde claro)
        botonActivo.setStyle("-fx-background-color: #E0F7F6; -fx-text-fill: #0F766E; -fx-font-size: 13px; -fx-font-weight: bold; -fx-alignment: center-left; -fx-padding: 10 15; -fx-background-radius: 8; -fx-cursor: hand;");
    }
}
