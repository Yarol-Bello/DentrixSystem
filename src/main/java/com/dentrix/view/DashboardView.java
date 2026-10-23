package com.dentrix.view;

import com.dentrix.model.Usuario;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class DashboardView {

    private Usuario usuarioLogueado;
    private BorderPane root;
    private VBox sidebar;

    public void mostrar(Stage stage, Usuario usuario) {
        this.usuarioLogueado = usuario;

        root = new BorderPane();
        
        // 1. Crear el Sidebar (Menú Lateral)
        sidebar = crearSidebar();
        root.setLeft(sidebar);

        // 2. Crear el Panel Central (Inicio)
        VBox mainPanel = crearPanelInicio();
        root.setCenter(mainPanel);

        // 3. Configurar la escena
        Scene scene = new Scene(root, 1024, 700);
        scene.getStylesheets().add(getClass().getResource("/styles/dashboard.css").toExternalForm());

        stage.setTitle("Dentrix Dental - Panel de Control");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.centerOnScreen();
        stage.show();
    }

    private VBox crearSidebar() {
        VBox sidebar = new VBox(10);
        sidebar.getStyleClass().add("sidebar");

        // Logo / Título
        Label lblTitulo = new Label("Dentrix");
        lblTitulo.getStyleClass().add("sidebar-title");
        Label lblSubtitulo = new Label("Centro Odontológico");
        lblSubtitulo.getStyleClass().add("sidebar-subtitle");
        
        VBox logoBox = new VBox(2, lblTitulo, lblSubtitulo);
        logoBox.setPadding(new Insets(0, 0, 20, 0));
        logoBox.setStyle("-fx-border-color: transparent transparent #E2E8F0 transparent; -fx-border-width: 0 0 1 0;");

        // Botones del menú
        Button btnInicio = crearBotonMenu("Inicio", true);
        Button btnRecepcion = crearBotonMenu("Recepción / Asistencia", false);
        Button btnPacientes = crearBotonMenu("Pacientes", false);
        Button btnAgenda = crearBotonMenu("Agenda Citas", false);
        Button btnFicha = crearBotonMenu("Ficha Clínica", false);
        Button btnCaja = crearBotonMenu("Caja / Cobros", false);
        Button btnReportes = crearBotonMenu("Reportes", false);
        Button btnConfig = crearBotonMenu("Configuración", false);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // Perfil de Usuario
        Label lblUser = new Label(usuarioLogueado.getNombres() + " " + usuarioLogueado.getApellidos());
        lblUser.setStyle("-fx-font-weight: bold; -fx-font-size: 12px; -fx-text-fill: #0F172A;");
        Label lblRol = new Label(usuarioLogueado.getRol());
        lblRol.getStyleClass().add("sidebar-subtitle");

        Button btnLogout = new Button("Cerrar Sesión");
        btnLogout.setStyle("-fx-background-color: #FEF2F2; -fx-text-fill: #DC2626; -fx-font-weight: bold; -fx-font-size: 11px; -fx-background-radius: 6; -fx-cursor: hand;");
        btnLogout.setMaxWidth(Double.MAX_VALUE);
        btnLogout.setOnAction(e -> {
            // Lógica para volver al login
            LoginView login = new LoginView();
            login.mostrar(new Stage());
            Stage stageActual = (Stage) btnLogout.getScene().getWindow();
            stageActual.close();
        });

        VBox userBox = new VBox(5, lblUser, lblRol, btnLogout);
        userBox.setPadding(new Insets(20, 0, 0, 0));
        userBox.setStyle("-fx-border-color: #E2E8F0 transparent transparent transparent; -fx-border-width: 1 0 0 0;");

        sidebar.getChildren().addAll(
                logoBox, 
                btnInicio, btnRecepcion, btnPacientes, btnAgenda, 
                btnFicha, btnCaja, btnReportes, btnConfig,
                spacer, userBox
        );

        return sidebar;
    }

    private Button crearBotonMenu(String texto, boolean activo) {
        Button btn = new Button(texto);
        btn.getStyleClass().add("menu-btn");
        btn.setMaxWidth(Double.MAX_VALUE);
        if (activo) {
            btn.getStyleClass().add("menu-btn-active");
        }
        return btn;
    }

    private VBox crearPanelInicio() {
        VBox panel = new VBox(20);
        panel.getStyleClass().add("main-panel");

        // Título de la página
        Label lblTitle = new Label("Panel de Control");
        lblTitle.getStyleClass().add("page-title");

        // Grid de KPIs (Tarjetas de resumen)
        HBox kpiContainer = new HBox(15);
        
        // Tarjeta 1
        VBox card1 = crearTarjetaKPI("Citados Hoy", "0"); // Luego lo conectamos a BD
        VBox card2 = crearTarjetaKPI("Atendidos", "0");
        VBox card3 = crearTarjetaKPI("En Atención", "0");
        VBox card4 = crearTarjetaKPI("Pendientes", "0");

        HBox.setHgrow(card1, Priority.ALWAYS);
        HBox.setHgrow(card2, Priority.ALWAYS);
        HBox.setHgrow(card3, Priority.ALWAYS);
        HBox.setHgrow(card4, Priority.ALWAYS);

        kpiContainer.getChildren().addAll(card1, card2, card3, card4);

        // Aquí luego irá la tabla de pacientes del día
        Label lblProximamente = new Label("Bienvenido al sistema. Seleccione un módulo del menú lateral.");
        lblProximamente.setStyle("-fx-text-fill: #64748B; -fx-font-size: 14px;");
        
        VBox contentBox = new VBox(lblProximamente);
        contentBox.setAlignment(Pos.CENTER);
        VBox.setVgrow(contentBox, Priority.ALWAYS);

        panel.getChildren().addAll(lblTitle, kpiContainer, contentBox);
        return panel;
    }

    private VBox crearTarjetaKPI(String titulo, String valor) {
        VBox card = new VBox(5);
        card.getStyleClass().add("kpi-card");
        
        Label lblTitulo = new Label(titulo);
        lblTitulo.getStyleClass().add("kpi-title");
        
        Label lblValor = new Label(valor);
        lblValor.getStyleClass().add("kpi-value");
        
        card.getChildren().addAll(lblTitulo, lblValor);
        return card;
    }
}