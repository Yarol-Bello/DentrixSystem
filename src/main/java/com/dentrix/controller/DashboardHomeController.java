package com.dentrix.controller;

import com.dentrix.model.Cita;
import com.dentrix.model.Paciente;
import com.dentrix.service.DashboardService;
import com.dentrix.service.PacienteService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;
import java.util.List;

public class DashboardHomeController {
    private DashboardService service;
    private PacienteService pacienteService;

    // KPIs
    private Label lblCitados, lblAtendidos, lblEnAtencion, lblPendientes;
    private TableView<Cita> tabla;
    private TextField txtBuscarDni;

    public DashboardHomeController(Label lblCitados, Label lblAtendidos, Label lblEnAtencion, Label lblPendientes, 
                                   TableView<Cita> tabla, TextField txtBuscarDni) {
        this.service = new DashboardService();
        this.pacienteService = new PacienteService();
        
        this.lblCitados = lblCitados; this.lblAtendidos = lblAtendidos; 
        this.lblEnAtencion = lblEnAtencion; this.lblPendientes = lblPendientes;
        this.tabla = tabla; this.txtBuscarDni = txtBuscarDni;

        configurarTabla();
        cargarDatos();
    }

    private void configurarTabla() {
        TableColumn<Cita, String> colHora = new TableColumn<>("Hora");
        colHora.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getHoraInicio().toString().substring(0,5)));
        
        TableColumn<Cita, String> colPaciente = new TableColumn<>("Paciente");
        colPaciente.setCellValueFactory(new PropertyValueFactory<>("pacienteNombre"));
        
        TableColumn<Cita, String> colDni = new TableColumn<>("DNI");
        colDni.setCellValueFactory(new PropertyValueFactory<>("dniPaciente"));
        
        TableColumn<Cita, String> colTrat = new TableColumn<>("Tratamiento");
        colTrat.setCellValueFactory(new PropertyValueFactory<>("procedimientoNombre"));
        
        TableColumn<Cita, String> colOdo = new TableColumn<>("Odontólogo");
        colOdo.setCellValueFactory(new PropertyValueFactory<>("odontologoNombre"));

        TableColumn<Cita, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colEstado.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Label badge = new Label();
                    switch (item) {
                        case "PENDIENTE":
                            badge.setText("Pendiente");
                            badge.setStyle("-fx-background-color: #F1F5F9; -fx-text-fill: #475569; -fx-border-color: #CBD5E1; -fx-padding: 2 8; -fx-background-radius: 4; -fx-font-size: 10px; -fx-font-weight: bold;");
                            break;
                        case "EN_SALA":
                            badge.setText("🟡 En Sala");
                            badge.setStyle("-fx-background-color: #FEF3C7; -fx-text-fill: #92400E; -fx-border-color: #FCD34D; -fx-padding: 2 8; -fx-background-radius: 4; -fx-font-size: 10px; -fx-font-weight: bold;");
                            break;
                        case "EN_ATENCION":
                            badge.setText("● En Atención");
                            badge.setStyle("-fx-background-color: #DBEAFE; -fx-text-fill: #1E40AF; -fx-border-color: #93C5FD; -fx-padding: 2 8; -fx-background-radius: 4; -fx-font-size: 10px; -fx-font-weight: bold;");
                            break;
                        case "ATENDIDO":
                            badge.setText("Atendido");
                            badge.setStyle("-fx-background-color: #D1FAE5; -fx-text-fill: #065F46; -fx-border-color: #6EE7B7; -fx-padding: 2 8; -fx-background-radius: 4; -fx-font-size: 10px; -fx-font-weight: bold;");
                            break;
                        case "FALTO":
                            badge.setText("Faltó");
                            badge.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #991B1B; -fx-border-color: #FCA5A5; -fx-padding: 2 8; -fx-background-radius: 4; -fx-font-size: 10px; -fx-font-weight: bold;");
                            break;
                    }
                    setGraphic(badge);
                    setAlignment(javafx.geometry.Pos.CENTER);
                }
            }
        });

        tabla.getColumns().addAll(colHora, colPaciente, colDni, colTrat, colOdo, colEstado);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public void cargarDatos() {
        LocalDate hoy = LocalDate.now();
        
        // Actualizar KPIs
        lblCitados.setText(String.valueOf(service.countCitasHoy(hoy)));
        lblAtendidos.setText(String.valueOf(service.countCitasPorEstado("ATENDIDO", hoy)));
        lblEnAtencion.setText(String.valueOf(service.countCitasPorEstado("EN_ATENCION", hoy)));
        lblPendientes.setText(String.valueOf(service.countCitasPorEstado("PENDIENTE", hoy)));

        // Cargar Tabla
        List<Cita> citas = service.listarCitasHoy(hoy);
        tabla.setItems(FXCollections.observableArrayList(citas));
    }

    public void buscarPaciente() {
        String dni = txtBuscarDni.getText().trim();
        if (dni.length() == 8) {
            Paciente p = pacienteService.buscarPorDni(dni);
            if (p != null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Paciente Encontrado");
                alert.setHeaderText(p.getApellidos() + ", " + p.getNombres());
                alert.setContentText("DNI: " + p.getDni() + "\nCelular: " + p.getCelular() + "\nAlergias: " + p.getAlergias());
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "No se encontró ningún paciente con ese DNI.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Ingrese un DNI válido de 8 dígitos.");
            alert.showAndWait();
        }
    }
}