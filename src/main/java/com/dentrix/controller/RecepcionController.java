package com.dentrix.controller;

import com.dentrix.model.Cita;
import com.dentrix.model.Paciente;
import com.dentrix.service.CitaService;
import com.dentrix.service.PacienteService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

public class RecepcionController {
    private CitaService citaService;
    private PacienteService pacienteService;

    private TableView<Cita> tabla;
    private ObservableList<Cita> listaOriginal;
    private List<Cita> citasDelDia;

    private TextField txtBuscar;
    private Label lblPendientes, lblEnSala, lblFaltaron;
    
    // Panel Derecho (Ficha)
    private VBox panelFicha;
    private Label lblIniciales, lblNombre, lblDniEdad, lblTratamiento, lblOdontologo, lblConsultorio, lblHorario, lblObservaciones;
    private Label lblEstadoBadge;

    public RecepcionController(TableView<Cita> tabla, TextField txtBuscar, 
                               Label lblPendientes, Label lblEnSala, Label lblFaltaron,
                               VBox panelFicha, Label lblIniciales, Label lblNombre, Label lblDniEdad,
                               Label lblTratamiento, Label lblOdontologo, Label lblConsultorio, 
                               Label lblHorario, Label lblObservaciones, Label lblEstadoBadge) {
        this.citaService = new CitaService();
        this.pacienteService = new PacienteService();
        
        this.tabla = tabla;
        this.txtBuscar = txtBuscar;
        this.lblPendientes = lblPendientes; this.lblEnSala = lblEnSala; this.lblFaltaron = lblFaltaron;
        
        this.panelFicha = panelFicha; this.lblIniciales = lblIniciales; this.lblNombre = lblNombre;
        this.lblDniEdad = lblDniEdad; this.lblTratamiento = lblTratamiento; this.lblOdontologo = lblOdontologo;
        this.lblConsultorio = lblConsultorio; this.lblHorario = lblHorario; 
        this.lblObservaciones = lblObservaciones; this.lblEstadoBadge = lblEstadoBadge;

        configurarTabla();
        cargarCitas();

        // Listener para el buscador
        this.txtBuscar.textProperty().addListener((obs, oldVal, newVal) -> filtrarTabla(newVal));
        
        // Listener para seleccionar fila
        this.tabla.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) mostrarFichaPaciente(newVal);
        });
    }

    private void configurarTabla() {
        TableColumn<Cita, String> colHora = new TableColumn<>("Hora");
        colHora.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getHoraInicio().toString().substring(0,5)));
        
        TableColumn<Cita, String> colPaciente = new TableColumn<>("Paciente");
        colPaciente.setCellValueFactory(new PropertyValueFactory<>("pacienteNombre"));
        
        TableColumn<Cita, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        // Personalizar celda de estado para que se vea como en el prototipo
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
                            badge.setStyle("-fx-background-color: #F1F5F9; -fx-text-fill: #475569; -fx-border-color: #CBD5E1; -fx-padding: 2 8; -fx-background-radius: 4; -fx-border-radius: 4; -fx-font-size: 10px; -fx-font-weight: bold;");
                            break;
                        case "EN_SALA":
                            badge.setText("🟡 En Sala");
                            badge.setStyle("-fx-background-color: #FEF3C7; -fx-text-fill: #92400E; -fx-border-color: #FCD34D; -fx-padding: 2 8; -fx-background-radius: 4; -fx-border-radius: 4; -fx-font-size: 10px; -fx-font-weight: bold;");
                            break;
                        case "EN_ATENCION":
                            badge.setText("● En Atención");
                            badge.setStyle("-fx-background-color: #DBEAFE; -fx-text-fill: #1E40AF; -fx-border-color: #93C5FD; -fx-padding: 2 8; -fx-background-radius: 4; -fx-border-radius: 4; -fx-font-size: 10px; -fx-font-weight: bold;");
                            break;
                        case "ATENDIDO":
                            badge.setText("Atendido");
                            badge.setStyle("-fx-background-color: #D1FAE5; -fx-text-fill: #065F46; -fx-border-color: #6EE7B7; -fx-padding: 2 8; -fx-background-radius: 4; -fx-border-radius: 4; -fx-font-size: 10px; -fx-font-weight: bold;");
                            break;
                        case "FALTO":
                            badge.setText("Faltó");
                            badge.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #991B1B; -fx-border-color: #FCA5A5; -fx-padding: 2 8; -fx-background-radius: 4; -fx-border-radius: 4; -fx-font-size: 10px; -fx-font-weight: bold;");
                            break;
                    }
                    setGraphic(badge);
                    setAlignment(javafx.geometry.Pos.CENTER);
                }
            }
        });

        TableColumn<Cita, Void> colAccion = new TableColumn<>("Acción");
        colAccion.setCellFactory(param -> new TableCell<>() {
            private final Button btnAsistio = new Button("✔ Asistió");
            private final Button btnFalto = new Button("✖");
            private final javafx.scene.layout.HBox pane = new javafx.scene.layout.HBox(5, btnAsistio, btnFalto);
            
            {
                btnAsistio.setStyle("-fx-background-color: #059669; -fx-text-fill: white; -fx-font-size: 10px; -fx-font-weight: bold; -fx-cursor: hand;");
                btnFalto.setStyle("-fx-background-color: white; -fx-text-fill: #DC2626; -fx-border-color: #FCA5A5; -fx-border-radius: 4; -fx-font-size: 10px; -fx-font-weight: bold; -fx-cursor: hand;");
                pane.setAlignment(javafx.geometry.Pos.CENTER);
                
                btnAsistio.setOnAction(e -> cambiarEstado(getTableView().getItems().get(getIndex()), "EN_SALA"));
                btnFalto.setOnAction(e -> cambiarEstado(getTableView().getItems().get(getIndex()), "FALTO"));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Cita c = getTableView().getItems().get(getIndex());
                    // Solo mostrar botones si la cita está pendiente
                    if (c.getEstado().equals("PENDIENTE")) {
                        setGraphic(pane);
                    } else {
                        setGraphic(new Label("—"));
                        setAlignment(javafx.geometry.Pos.CENTER);
                    }
                }
            }
        });

        tabla.getColumns().addAll(colHora, colPaciente, colEstado, colAccion);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public void cargarCitas() {
        citasDelDia = citaService.listarCitasDelDia(Date.valueOf(LocalDate.now()));
        listaOriginal = FXCollections.observableArrayList(citasDelDia);
        tabla.setItems(listaOriginal);
        actualizarContadores();
        panelFicha.setVisible(false); // Ocultar ficha al recargar
    }

    private void actualizarContadores() {
        long pendientes = citasDelDia.stream().filter(c -> c.getEstado().equals("PENDIENTE")).count();
        long enSala = citasDelDia.stream().filter(c -> c.getEstado().equals("EN_SALA") || c.getEstado().equals("EN_ATENCION")).count();
        long faltaron = citasDelDia.stream().filter(c -> c.getEstado().equals("FALTO")).count();

        lblPendientes.setText("⏳ " + pendientes + " Pendientes");
        lblEnSala.setText("🟡 " + enSala + " En Sala");
        lblFaltaron.setText("✖ " + faltaron + " Faltaron");
    }

    private void filtrarTabla(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            tabla.setItems(listaOriginal);
        } else {
            String lower = filtro.toLowerCase();
            List<Cita> filtradas = listaOriginal.stream()
                .filter(c -> c.getDniPaciente().contains(lower) || c.getPacienteNombre().toLowerCase().contains(lower))
                .collect(Collectors.toList());
            tabla.setItems(FXCollections.observableArrayList(filtradas));
        }
    }

    private void cambiarEstado(Cita c, String nuevoEstado) {
        try {
            citaService.actualizarEstadoCita(c.getId(), nuevoEstado);
            cargarCitas(); // Recargar para actualizar contadores y tabla
            tabla.getSelectionModel().clearSelection();
            panelFicha.setVisible(false);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void mostrarFichaPaciente(Cita c) {
        panelFicha.setVisible(true);
        
        // Cargar datos del paciente reales
        Paciente p = pacienteService.buscarPorDni(c.getDniPaciente());
        
        if (p != null) {
            String nom = p.getNombres().substring(0,1).toUpperCase();
            String ape = p.getApellidos().substring(0,1).toUpperCase();
            lblIniciales.setText(nom + ape);
            lblNombre.setText(p.getApellidos() + ", " + p.getNombres());
            
            int edad = p.getFechaNacimiento() != null ? Period.between(p.getFechaNacimiento().toLocalDate(), LocalDate.now()).getYears() : 0;
            String sexo = p.getSexo() != null ? p.getSexo() : "";
            lblDniEdad.setText("DNI: " + p.getDni() + " · " + edad + " años · " + sexo);
        }

        lblTratamiento.setText(c.getProcedimientoNombre());
        lblOdontologo.setText(c.getOdontologoNombre());
        lblConsultorio.setText(c.getConsultorio());
        lblHorario.setText(c.getHoraInicio().toString().substring(0,5) + " - " + c.getHoraFin().toString().substring(0,5));
        lblObservaciones.setText(c.getObservaciones() != null && !c.getObservaciones().isEmpty() ? c.getObservaciones() : "Sin observaciones.");
        
        // Badge de estado
        lblEstadoBadge.setText(c.getEstado().replace("_", " "));
        if (c.getEstado().equals("EN_ATENCION")) {
             lblEstadoBadge.setStyle("-fx-background-color: #DBEAFE; -fx-text-fill: #1E40AF; -fx-border-color: #93C5FD; -fx-padding: 2 8; -fx-background-radius: 4; -fx-border-radius: 4; -fx-font-size: 10px; -fx-font-weight: bold;");
        } else {
             lblEstadoBadge.setStyle("-fx-background-color: #F1F5F9; -fx-text-fill: #475569; -fx-border-color: #CBD5E1; -fx-padding: 2 8; -fx-background-radius: 4; -fx-border-radius: 4; -fx-font-size: 10px; -fx-font-weight: bold;");
        }
    }
}