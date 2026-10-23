package com.dentrix.view;

import com.dentrix.controller.RecepcionController;
import com.dentrix.model.Cita;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class RecepcionView {
    public Pane getView() {
        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #F1F5F9;");

        // --- BARRA SUPERIOR ---
        HBox topBar = new HBox(15);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10, 15, 10, 15));
        topBar.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM yyyy", new Locale("es", "ES"));
        Label lblFecha = new Label("📅 " + LocalDate.now().format(formatter));
        lblFecha.setStyle("-fx-font-weight: bold; -fx-text-fill: #0F172A;");

        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);

        TextField txtBuscar = new TextField();
        txtBuscar.setPromptText("Buscar por DNI...");
        txtBuscar.setStyle("-fx-background-color: #F8FAFC; -fx-border-color: #CBD5E1; -fx-border-radius: 6; -fx-padding: 6;");
        
        Button btnBuscar = new Button("Buscar");
        btnBuscar.setStyle("-fx-background-color: #00A896; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");

        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        Label lblPendientes = new Label("⏳ 0 Pendientes");
        lblPendientes.setStyle("-fx-background-color: #F1F5F9; -fx-text-fill: #475569; -fx-border-color: #E2E8F0; -fx-padding: 4 8; -fx-background-radius: 6; -fx-font-size: 10px; -fx-font-weight: bold;");
        
        Label lblEnSala = new Label("🟡 0 En Sala");
        lblEnSala.setStyle("-fx-background-color: #FEF3C7; -fx-text-fill: #92400E; -fx-border-color: #FCD34D; -fx-padding: 4 8; -fx-background-radius: 6; -fx-font-size: 10px; -fx-font-weight: bold;");
        
        Label lblFaltaron = new Label("✖ 0 Faltaron");
        lblFaltaron.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #991B1B; -fx-border-color: #FCA5A5; -fx-padding: 4 8; -fx-background-radius: 6; -fx-font-size: 10px; -fx-font-weight: bold;");

        topBar.getChildren().addAll(lblFecha, spacer1, txtBuscar, btnBuscar, spacer2, lblPendientes, lblEnSala, lblFaltaron);

        // --- CONTENIDO PRINCIPAL ---
        HBox contentBox = new HBox(20);
        HBox.setHgrow(contentBox, Priority.ALWAYS);

        // --- PANEL IZQUIERDO: TABLA (65%) ---
        VBox tablePanel = new VBox(10);
        tablePanel.setPadding(new Insets(15));
        tablePanel.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);");
        HBox.setHgrow(tablePanel, Priority.ALWAYS);
        tablePanel.setMinWidth(600);

        Label tituloTabla = new Label("Pacientes del Día");
        tituloTabla.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #0F172A;");

        TableView<Cita> tabla = new TableView<>();
        VBox.setVgrow(tabla, Priority.ALWAYS);

        tablePanel.getChildren().addAll(tituloTabla, new Separator(), tabla);

        // --- PANEL DERECHO: FICHA DEL PACIENTE (35%) ---
        VBox panelFicha = new VBox(10);
        panelFicha.setPadding(new Insets(15));
        panelFicha.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);");
        panelFicha.setMinWidth(320);
        panelFicha.setMaxWidth(350);
        panelFicha.setVisible(false); // Oculto hasta seleccionar

        HBox headerFicha = new HBox(10);
        headerFicha.setAlignment(Pos.CENTER_LEFT);
        Label tituloFicha = new Label("Ficha del Paciente");
        tituloFicha.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #0F172A;");
        Region spacerFicha = new Region();
        HBox.setHgrow(spacerFicha, Priority.ALWAYS);
        Label lblEstadoBadge = new Label("En Atención");
        headerFicha.getChildren().addAll(tituloFicha, spacerFicha, lblEstadoBadge);

        // Tarjeta de identificación
        HBox cardIdentificacion = new HBox(10);
        cardIdentificacion.setAlignment(Pos.CENTER_LEFT);
        cardIdentificacion.setStyle("-fx-background-color: #F8FAFC; -fx-border-color: #E2E8F0; -fx-padding: 10; -fx-background-radius: 8;");
        
        Circle avatar = new Circle(20, Color.web("#DBEAFE"));
        Label lblIniciales = new Label();
        lblIniciales.setStyle("-fx-text-fill: #1E40AF; -fx-font-weight: bold; -fx-font-size: 14px;");
        StackPane avatarPane = new StackPane(avatar, lblIniciales);

        VBox infoPaciente = new VBox(2);
        Label lblNombre = new Label();
        lblNombre.setStyle("-fx-font-weight: bold; -fx-text-fill: #0F172A; -fx-font-size: 13px;");
        Label lblDniEdad = new Label();
        lblDniEdad.setStyle("-fx-text-fill: #64748B; -fx-font-size: 11px;");
        infoPaciente.getChildren().addAll(lblNombre, lblDniEdad);
        
        cardIdentificacion.getChildren().addAll(avatarPane, infoPaciente);

        // Sección Datos de la Cita
        VBox seccionCita = new VBox(5);
        Label lblTitCita = new Label("Datos de la Cita");
        lblTitCita.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: #94A3B8;");
        
        GridPane gridCita = new GridPane();
        gridCita.setVgap(8);
        gridCita.setHgap(10);
        
        Label lblTratamiento = new Label(); lblTratamiento.setStyle("-fx-font-weight: bold; -fx-font-size: 11px;");
        Label lblOdontologo = new Label(); lblOdontologo.setStyle("-fx-font-weight: bold; -fx-font-size: 11px;");
        Label lblConsultorio = new Label(); lblConsultorio.setStyle("-fx-font-weight: bold; -fx-font-size: 11px;");
        Label lblHorario = new Label(); lblHorario.setStyle("-fx-font-weight: bold; -fx-font-size: 11px; -fx-font-family: monospace;");

        gridCita.add(new Label("Tratamiento:"), 0, 0); gridCita.add(lblTratamiento, 1, 0);
        gridCita.add(new Label("Odontólogo:"), 0, 1); gridCita.add(lblOdontologo, 1, 1);
        gridCita.add(new Label("Consultorio:"), 0, 2); gridCita.add(lblConsultorio, 1, 2);
        gridCita.add(new Label("Horario:"), 0, 3); gridCita.add(lblHorario, 1, 3);

        seccionCita.getChildren().addAll(lblTitCita, new Separator(), gridCita);

        // Sección Observaciones
        VBox seccionObs = new VBox(5);
        Label lblTitObs = new Label("Observaciones");
        lblTitObs.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: #94A3B8;");
        Label lblObservaciones = new Label();
        lblObservaciones.setWrapText(true);
        lblObservaciones.setStyle("-fx-background-color: #FEF3C7; -fx-text-fill: #92400E; -fx-padding: 8; -fx-background-radius: 6; -fx-font-size: 11px;");
        seccionObs.getChildren().addAll(lblTitObs, new Separator(), lblObservaciones);

        panelFicha.getChildren().addAll(headerFicha, new Separator(), cardIdentificacion, seccionCita, seccionObs);

        contentBox.getChildren().addAll(tablePanel, panelFicha);
        mainLayout.getChildren().addAll(topBar, contentBox);

        // --- CONECTAR CON EL CONTROLADOR ---
        RecepcionController controller = new RecepcionController(
                tabla, txtBuscar, lblPendientes, lblEnSala, lblFaltaron,
                panelFicha, lblIniciales, lblNombre, lblDniEdad, 
                lblTratamiento, lblOdontologo, lblConsultorio, lblHorario, 
                lblObservaciones, lblEstadoBadge
        );

        // Enlazar el botón de buscar
        btnBuscar.setOnAction(e -> controller.cargarCitas());
        // La búsqueda en vivo se maneja en el controlador con el Listener

        return mainLayout;
    }
}