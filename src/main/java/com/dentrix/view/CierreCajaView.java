package com.dentrix.view;

import com.dentrix.model.Pago;
import com.dentrix.model.TurnoCaja;
import com.dentrix.service.CajaService;
import com.dentrix.util.SesionGlobal;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class CierreCajaView {
    private CajaService cajaService = new CajaService();
    private TurnoCaja turnoActual;
    private List<Pago> pagosTurno;

    private double totalEfectivo = 0.0;
    private double totalDigital = 0.0;
    private double diferencia = 0.0;

    public Pane getView() {
        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #F1F5F9;");

        turnoActual = cajaService.obtenerTurnoActual();
        pagosTurno = cajaService.obtenerPagosTurnoActual();

        for (Pago p : pagosTurno) {
            if (p.getMetodoPago().equals("EFECTIVO")) {
                totalEfectivo += p.getMonto();
            } else {
                totalDigital += p.getMonto();
            }
        }

        // Barra Superior
        HBox topBar = new HBox(10);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10, 15, 10, 15));
        topBar.setStyle("-fx-background-color: white; -fx-background-radius: 8;");
        
        String estadoTurno = (turnoActual != null) ? "🟢 Turno Abierto" : "🔴 No hay turno abierto";
        Label lblEstado = new Label(estadoTurno);
        lblEstado.setStyle("-fx-background-color: #D1FAE5; -fx-text-fill: #065F46; -fx-padding: 4 10; -fx-background-radius: 12; -fx-font-weight: bold;");
        
        String nombreCajero = SesionGlobal.usuarioLogueado != null ? SesionGlobal.usuarioLogueado.getNombres() + " " + SesionGlobal.usuarioLogueado.getApellidos() : "Sin sesión";
        Label lblDatos = new Label("Cajero: " + nombreCajero + " · Fecha: " + java.time.LocalDate.now());
        lblDatos.setStyle("-fx-font-weight: bold; -fx-text-fill: #475569;");
        
        topBar.getChildren().addAll(lblEstado, new Label("·"), lblDatos);

        // Contenido
        HBox contentBox = new HBox(20);
        HBox.setHgrow(contentBox, Priority.ALWAYS);

        // Panel Izquierdo: Arqueo
        VBox panelArqueo = new VBox(10);
        panelArqueo.setPadding(new Insets(15));
        panelArqueo.setStyle("-fx-background-color: white; -fx-background-radius: 8;");
        panelArqueo.setMinWidth(400);

        Label lblFondo = new Label("Fondo Inicial (S/):");
        TextField txtFondo = new TextField(turnoActual != null ? String.format("%.2f", turnoActual.getFondoInicial()) : "0.00");
        
        Label lblEfectivo = new Label(String.format("Cobros en Efectivo: S/ %.2f", totalEfectivo));
        lblEfectivo.setStyle("-fx-font-weight: bold; -fx-text-fill: #0F766E;");
        
        Label lblDigital = new Label(String.format("Cobros Yape/Plin: S/ %.2f", totalDigital));
        lblDigital.setStyle("-fx-font-weight: bold; -fx-text-fill: #6B21A8;");
        
        Label lblTotal = new Label(String.format("Total Ingresos: S/ %.2f", totalEfectivo + totalDigital));
        lblTotal.setStyle("-fx-font-weight: bold;");
        
        Label lblContado = new Label("Conteo Físico (S/):");
        TextField txtContado = new TextField("0.00");
        
        Label lblDiferencia = new Label("Diferencia: S/ 0.00");
        lblDiferencia.setStyle("-fx-background-color: #ECFDF5; -fx-text-fill: #047857; -fx-padding: 8; -fx-font-weight: bold; -fx-background-radius: 6;");

        txtContado.textProperty().addListener((obs, old, newVal) -> {
            try {
                double contado = Double.parseDouble(newVal.isEmpty() ? "0" : newVal);
                double fondo = Double.parseDouble(txtFondo.getText().isEmpty() ? "0" : txtFondo.getText());
                double esperado = fondo + totalEfectivo;
                diferencia = contado - esperado;
                
                if (diferencia == 0) {
                    lblDiferencia.setText("S/ 0.00 (Exacto)");
                    lblDiferencia.setStyle("-fx-background-color: #D1FAE5; -fx-text-fill: #065F46; -fx-padding: 8; -fx-font-weight: bold; -fx-background-radius: 6;");
                } else if (diferencia > 0) {
                    lblDiferencia.setText(String.format("S/ +%.2f (Sobrante)", diferencia));
                    lblDiferencia.setStyle("-fx-background-color: #DBEAFE; -fx-text-fill: #1E40AF; -fx-padding: 8; -fx-font-weight: bold; -fx-background-radius: 6;");
                } else {
                    lblDiferencia.setText(String.format("S/ %.2f (Faltante)", diferencia));
                    lblDiferencia.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #991B1B; -fx-padding: 8; -fx-font-weight: bold; -fx-background-radius: 6;");
                }
            } catch (NumberFormatException e) {
                lblDiferencia.setText("Error");
            }
        });

        Button btnConsolidar = new Button("🔒 Consolidar Cierre de Caja");
        btnConsolidar.setMaxWidth(Double.MAX_VALUE);
        btnConsolidar.setStyle("-fx-background-color: #1E293B; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10; -fx-cursor: hand;");
        
        btnConsolidar.setOnAction(e -> {
            try {
                if (turnoActual == null) throw new Exception("No hay turno abierto para cerrar.");
                
                double fondo = Double.parseDouble(txtFondo.getText());
                double contado = Double.parseDouble(txtContado.getText());
                int idCajero = SesionGlobal.usuarioLogueado != null ? SesionGlobal.usuarioLogueado.getId() : 1;
                
                cajaService.consolidarCierreCaja(turnoActual, fondo, contado, totalEfectivo, totalDigital, diferencia, idCajero);
                new Alert(Alert.AlertType.INFORMATION, "✅ Cierre de caja consolidado correctamente.").showAndWait();
                ((Stage) btnConsolidar.getScene().getWindow()).close();
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Error: " + ex.getMessage()).showAndWait();
            }
        });

        panelArqueo.getChildren().addAll(new Label("Fondo Inicial y Arqueo"), lblFondo, txtFondo, 
                new Separator(), lblEfectivo, lblDigital, lblTotal, 
                new Separator(), lblContado, txtContado, lblDiferencia, btnConsolidar);

        // Panel Derecho: Transacciones y Acta
        VBox panelActa = new VBox(10);
        panelActa.setPadding(new Insets(15));
        panelActa.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-border-color: #0F766E; -fx-border-width: 2;");
        HBox.setHgrow(panelActa, Priority.ALWAYS);

        TableView<Pago> tablaTransacciones = new TableView<>();
        TableColumn<Pago, String> colHora = new TableColumn<>("Hora");
        colHora.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFechaPago().toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm"))));
        TableColumn<Pago, String> colComp = new TableColumn<>("Comprobante");
        colComp.setCellValueFactory(new PropertyValueFactory<>("comprobante"));
        TableColumn<Pago, String> colMetodo = new TableColumn<>("Método");
        colMetodo.setCellValueFactory(new PropertyValueFactory<>("metodoPago"));
        TableColumn<Pago, String> colMonto = new TableColumn<>("Monto");
        colMonto.setCellValueFactory(cell -> new SimpleStringProperty("S/ " + String.format("%.2f", cell.getValue().getMonto())));
        
        tablaTransacciones.getColumns().addAll(colHora, colComp, colMetodo, colMonto);
        tablaTransacciones.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tablaTransacciones.setItems(FXCollections.observableArrayList(pagosTurno));
        VBox.setVgrow(tablaTransacciones, Priority.ALWAYS);

        Label lblTitActa = new Label("CENTRO ODONTOLÓGICO DENTRIX");
        lblTitActa.setStyle("-fx-font-weight: bold; -fx-text-fill: #0F766E;");
        Label lblSubActa = new Label("Acta de Arqueo y Cierre • Turno " + (turnoActual != null ? turnoActual.getTurno() : "N/A"));
        Label lblDatosActa = new Label("Fecha: " + java.time.LocalDate.now() + "\nN° Cierre: CIE-" + java.time.LocalDate.now().getYear() + "-" + (turnoActual != null ? String.format("%04d", turnoActual.getId()) : "0000"));
        
        panelActa.getChildren().addAll(lblTitActa, lblSubActa, new Separator(), lblDatosActa, tablaTransacciones);

        contentBox.getChildren().addAll(panelArqueo, panelActa);
        mainLayout.getChildren().addAll(topBar, contentBox);

        return mainLayout;
    }
}