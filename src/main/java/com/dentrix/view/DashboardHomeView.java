package com.dentrix.view;

import com.dentrix.controller.DashboardHomeController;
import com.dentrix.model.Cita;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DashboardHomeView {
    public Pane getView() {
        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #F1F5F9;");

        // --- BARRA SUPERIOR: BUSCADOR ---
        HBox topBar = new HBox(10);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10, 15, 10, 15));
        topBar.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);");

        TextField txtBuscarDni = new TextField();
        txtBuscarDni.setPromptText("DNI del paciente");
        txtBuscarDni.setPrefWidth(250);
        txtBuscarDni.setStyle("-fx-background-color: #F8FAFC; -fx-border-color: #CBD5E1; -fx-border-radius: 6; -fx-padding: 8;");
        
        Button btnBuscar = new Button("🔍 Buscar");
        btnBuscar.setStyle("-fx-background-color: #00A896; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");

        topBar.getChildren().addAll(txtBuscarDni, btnBuscar);

        // --- TARJETAS KPI ---
        HBox kpiBox = new HBox(15);
        
        VBox kpi1 = crearTarjeta("Citados Hoy", "0", "#F8FAFC", "#0F172A", "#E2E8F0");
        Label lblCitados = (Label) kpi1.getChildren().get(1);
        
        VBox kpi2 = crearTarjeta("Atendidos", "0", "#ECFDF5", "#065F46", "#A7F3D0");
        Label lblAtendidos = (Label) kpi2.getChildren().get(1);
        
        VBox kpi3 = crearTarjeta("En Atención", "0", "#EFF6FF", "#1E40AF", "#BFDBFE");
        Label lblEnAtencion = (Label) kpi3.getChildren().get(1);
        
        VBox kpi4 = crearTarjeta("Pendientes", "0", "#F8FAFC", "#475569", "#E2E8F0");
        Label lblPendientes = (Label) kpi4.getChildren().get(1);

        HBox.setHgrow(kpi1, Priority.ALWAYS);
        HBox.setHgrow(kpi2, Priority.ALWAYS);
        HBox.setHgrow(kpi3, Priority.ALWAYS);
        HBox.setHgrow(kpi4, Priority.ALWAYS);
        kpiBox.getChildren().addAll(kpi1, kpi2, kpi3, kpi4);

        // --- TABLA DE PACIENTES ---
        VBox tableBox = new VBox(10);
        tableBox.setPadding(new Insets(15));
        tableBox.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);");
        VBox.setVgrow(tableBox, Priority.ALWAYS);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM yyyy", new Locale("es", "ES"));
        Label tituloTabla = new Label("Pacientes Programados • " + LocalDate.now().format(formatter));
        tituloTabla.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #0F172A;");

        TableView<Cita> tabla = new TableView<>();
        VBox.setVgrow(tabla, Priority.ALWAYS);

        tableBox.getChildren().addAll(tituloTabla, new Separator(), tabla);

        mainLayout.getChildren().addAll(topBar, kpiBox, tableBox);

        // --- CONECTAR CONTROLADOR ---
        DashboardHomeController controller = new DashboardHomeController(
                lblCitados, lblAtendidos, lblEnAtencion, lblPendientes, tabla, txtBuscarDni
        );

        btnBuscar.setOnAction(e -> controller.buscarPaciente());
        // Actualizar datos al presionar Enter en el buscador
        txtBuscarDni.setOnAction(e -> controller.buscarPaciente());

        return mainLayout;
    }

    private VBox crearTarjeta(String titulo, String valor, String bgColor, String textColor, String borderColor) {
        VBox box = new VBox(5);
        box.setStyle("-fx-background-color: " + bgColor + "; -fx-border-color: " + borderColor + "; -fx-padding: 15; -fx-background-radius: 8; -fx-border-radius: 8;");
        Label lblTit = new Label(titulo);
        lblTit.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: " + textColor + "; -fx-opacity: 0.8;");
        Label lblVal = new Label(valor);
        lblVal.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: " + textColor + "; -fx-font-family: monospace;");
        box.getChildren().addAll(lblTit, lblVal);
        return box;
    }
}