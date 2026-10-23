package com.dentrix.controller;

import com.dentrix.model.*;
import com.dentrix.service.FichaClinicaService;
import com.dentrix.service.PacienteService;
import com.dentrix.util.SesionGlobal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FichaClinicaController {
    private FichaClinicaService fichaService;
    private PacienteService pacienteService;
    private Paciente pacienteActual;
    
    private TextField txtDni;
    private Label lblCodigoPaciente, lblNombrePaciente, lblAlergias, lblTotalPresupuesto;
    private TextArea txtAnamnesis, txtPrescripcion;
    private ComboBox<Cie10> cbCie10;
    private ComboBox<Procedimiento> cbPlanTratamiento;
    private TableView<FichaDetalle> tablaDetalles;
    private ObservableList<FichaDetalle> listaDetalles;
    
    private double totalPresupuesto = 0.0;

    public FichaClinicaController(TextField txtDni, Label lblCodigoPaciente, Label lblNombrePaciente, 
                                  Label lblAlergias, Label lblTotalPresupuesto, TextArea txtAnamnesis, 
                                  TextArea txtPrescripcion, ComboBox<Cie10> cbCie10, 
                                  ComboBox<Procedimiento> cbPlanTratamiento, TableView<FichaDetalle> tablaDetalles) {
        this.fichaService = new FichaClinicaService();
        this.pacienteService = new PacienteService();
        
        this.txtDni = txtDni; this.lblCodigoPaciente = lblCodigoPaciente; this.lblNombrePaciente = lblNombrePaciente;
        this.lblAlergias = lblAlergias; this.lblTotalPresupuesto = lblTotalPresupuesto;
        this.txtAnamnesis = txtAnamnesis; this.txtPrescripcion = txtPrescripcion;
        this.cbCie10 = cbCie10; this.cbPlanTratamiento = cbPlanTratamiento;
        this.tablaDetalles = tablaDetalles;
        this.listaDetalles = FXCollections.observableArrayList();

        inicializarCombos();
        configurarTabla();
    }

    private void inicializarCombos() {
        cbCie10.setItems(FXCollections.observableArrayList(fichaService.obtenerCie10()));
        cbCie10.setCellFactory(param -> new ListCell<>() {
            @Override protected void updateItem(Cie10 c, boolean empty) {
                super.updateItem(c, empty);
                setText(empty || c == null ? null : c.getCodigo() + " " + c.getNombre());
            }
        });
        cbCie10.setButtonCell(cbCie10.getCellFactory().call(null));

        cbPlanTratamiento.setItems(FXCollections.observableArrayList(fichaService.obtenerProcedimientos()));
        cbPlanTratamiento.setCellFactory(param -> new ListCell<>() {
            @Override protected void updateItem(Procedimiento p, boolean empty) {
                super.updateItem(p, empty);
                setText(empty || p == null ? null : p.getCodigo() + ": " + p.getNombre() + " — S/ " + String.format("%.2f", p.getPrecio()));
            }
        });
        cbPlanTratamiento.setButtonCell(cbPlanTratamiento.getCellFactory().call(null));
    }

    private void configurarTabla() {
        TableColumn<FichaDetalle, String> colPieza = new TableColumn<>("Pieza");
        colPieza.setCellValueFactory(new PropertyValueFactory<>("piezaDental"));
        
        TableColumn<FichaDetalle, String> colDiag = new TableColumn<>("Procedimiento");
        colDiag.setCellValueFactory(new PropertyValueFactory<>("diagnostico"));
        
        TableColumn<FichaDetalle, String> colCosto = new TableColumn<>("Costo");
        colCosto.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty("S/ " + String.format("%.2f", cell.getValue().getSubtotal())));
        
        TableColumn<FichaDetalle, Void> colAccion = new TableColumn<>("Quitar");
        colAccion.setCellFactory(param -> new TableCell<>() {
            private final Button btnQuitar = new Button("🗑");
            {
                btnQuitar.setStyle("-fx-background-color: #FEF2F2; -fx-text-fill: #DC2626; -fx-font-size: 10px; -fx-border-color: #FCA5A5; -fx-border-radius: 4; -fx-cursor: hand;");
                btnQuitar.setOnAction(e -> {
                    FichaDetalle det = getTableView().getItems().get(getIndex());
                    listaDetalles.remove(det);
                    recalcularTotal();
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnQuitar);
            }
        });

        tablaDetalles.getColumns().addAll(colPieza, colDiag, colCosto, colAccion);
        tablaDetalles.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tablaDetalles.setItems(listaDetalles);
    }

    public void cargarPaciente() {
        String dni = txtDni.getText().trim();
        if (dni.length() != 8) {
            mostrarAlerta("Aviso", "DNI inválido.");
            return;
        }

        pacienteActual = pacienteService.buscarPorDni(dni);
        if (pacienteActual != null) {
            lblNombrePaciente.setText(pacienteActual.getApellidos() + ", " + pacienteActual.getNombres());
            lblCodigoPaciente.setText(pacienteActual.getCodigoHc());
            lblAlergias.setText(pacienteActual.getAlergias() != null && !pacienteActual.getAlergias().isEmpty() ? 
                    pacienteActual.getAlergias() : "Sin alergias registradas.");
        } else {
            pacienteActual = null;
            mostrarAlerta("Error", "Paciente no encontrado.");
        }
    }

    public void abrirDialogoDiente(String numeroDiente, Button botonDiente) {
        if (pacienteActual == null) {
            mostrarAlerta("Error", "Cargue un paciente primero.");
            return;
        }

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Pieza Dental " + numeroDiente);

        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: white;");

        ComboBox<String> cbEstado = new ComboBox<>();
        cbEstado.getItems().addAll("Caries", "Curado", "Extracción");
        cbEstado.setPromptText("Seleccione estado");
        cbEstado.setMaxWidth(Double.MAX_VALUE);

        ComboBox<Procedimiento> cbTrat = new ComboBox<>();
        cbTrat.setItems(cbPlanTratamiento.getItems());
        cbTrat.setCellFactory(cbPlanTratamiento.getCellFactory());
        cbTrat.setButtonCell(cbPlanTratamiento.getButtonCell());
        cbTrat.setPromptText("Seleccione tratamiento");
        cbTrat.setMaxWidth(Double.MAX_VALUE);

        Button btnGuardar = new Button("Guardar Hallazgo");
        btnGuardar.setStyle("-fx-background-color: #00A896; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        btnGuardar.setMaxWidth(Double.MAX_VALUE);

        btnGuardar.setOnAction(e -> {
            if (cbEstado.getValue() == null || cbTrat.getValue() == null) {
                mostrarAlerta("Error", "Seleccione estado y tratamiento.");
                return;
            }
            
            Procedimiento p = cbTrat.getValue();
            FichaDetalle det = new FichaDetalle();
            det.setPiezaDental(numeroDiente);
            det.setCaraDental("Oclusal");
            det.setDiagnostico(p.getNombre());
            det.setEstado(cbEstado.getValue());
            det.setProcedimientoId(p.getId());
            det.setPrecioAplicado(p.getPrecio());
            det.setSubtotal(p.getPrecio());
            
            listaDetalles.add(det);
            recalcularTotal();

            if (cbEstado.getValue().equals("Caries")) {
                botonDiente.setStyle("-fx-background-color: #FEE2E2; -fx-border-color: #EF4444; -fx-text-fill: #991B1B; -fx-cursor: hand; -fx-font-size: 11px; -fx-font-weight: bold;");
            } else if (cbEstado.getValue().equals("Curado")) {
                botonDiente.setStyle("-fx-background-color: #DBEAFE; -fx-border-color: #3B82F6; -fx-text-fill: #1E3A8A; -fx-cursor: hand; -fx-font-size: 11px; -fx-font-weight: bold;");
            } else {
                botonDiente.setStyle("-fx-background-color: #F3F4F6; -fx-border-color: #9CA3AF; -fx-text-fill: #374151; -fx-cursor: hand; -fx-strikethrough: true; -fx-font-size: 11px; -fx-font-weight: bold;");
            }
            
            dialog.close();
        });

        root.getChildren().addAll(new Label("Estado:"), cbEstado, new Label("Tratamiento:"), cbTrat, btnGuardar);
        dialog.setScene(new Scene(root, 280, 250));
        dialog.showAndWait();
    }

    public void agregarTratamientoALista(FichaDetalle det) {
        listaDetalles.add(det);
        recalcularTotal();
    }

    private void recalcularTotal() {
        totalPresupuesto = listaDetalles.stream().mapToDouble(FichaDetalle::getSubtotal).sum();
        lblTotalPresupuesto.setText(String.format("TOTAL: S/ %.2f", totalPresupuesto));
    }

    public void guardarFicha(String estadoPresupuesto) {
        if (pacienteActual == null) {
            mostrarAlerta("Validación", "Debe cargar un paciente primero.");
            return;
        }
        if (listaDetalles.isEmpty()) {
            mostrarAlerta("Validación", "Registre al menos un hallazgo o tratamiento.");
            return;
        }

        try {
            FichaClinica ficha = new FichaClinica();
            ficha.setPacienteId(pacienteActual.getId());
            // citaId se queda null por defecto, lo que permite guardar en la BD
            ficha.setOdontologoId(SesionGlobal.usuarioLogueado.getId());
            ficha.setFecha(Date.valueOf(LocalDate.now()));
            ficha.setAnamnesis(txtAnamnesis.getText());
            ficha.setDiagnosticoCie10(cbCie10.getValue() != null ? cbCie10.getValue().getCodigo() + " " + cbCie10.getValue().getNombre() : "Sin diagnóstico");
            ficha.setPlanTratamiento(cbPlanTratamiento.getValue() != null ? cbPlanTratamiento.getValue().getNombre() : "Múltiples tratamientos");
            ficha.setPrescripcion(txtPrescripcion.getText());
            ficha.setTotalPresupuesto(totalPresupuesto);
            ficha.setMontoPagado(0.0);
            ficha.setSaldo(totalPresupuesto);
            ficha.setEstadoPresupuesto(estadoPresupuesto);
            ficha.setDetalles(new ArrayList<>(listaDetalles));

            fichaService.guardarFicha(ficha);
            
            mostrarAlerta("Éxito", "Ficha guardada correctamente con estado: " + estadoPresupuesto);
            limpiarFormulario();
            
        } catch (Exception e) {
            mostrarAlerta("Error de Guardado", e.getMessage());
        }
    }

    public void mostrarModalFichas() {
        if (pacienteActual == null) {
            mostrarAlerta("Error", "Cargue un paciente primero.");
            return;
        }

        List<FichaClinica> fichas = fichaService.listarFichasPorPaciente(pacienteActual.getId());

        Stage modal = new Stage();
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.setTitle("Historial de Fichas - " + pacienteActual.getNombres());

        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #F1F5F9;");

        Label titulo = new Label("Historial de Fichas");
        titulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        VBox listaFichas = new VBox(10);
        if (fichas.isEmpty()) {
            listaFichas.getChildren().add(new Label("No hay fichas registradas para este paciente."));
        } else {
            for (FichaClinica f : fichas) {
                VBox tarjeta = new VBox(5);
                String colorBorde = f.getEstadoPresupuesto().equals("PENDIENTE") ? "#FCD34D" : "#A7F3D0";
                tarjeta.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-color: " + colorBorde + "; -fx-border-width: 2; -fx-background-radius: 8; -fx-border-radius: 8;");
                
                Label lblFecha = new Label("Fecha: " + f.getFecha() + " | Estado: " + f.getEstadoPresupuesto());
                lblFecha.setStyle("-fx-font-weight: bold;");
                Label lblDiag = new Label("Diagnóstico: " + f.getDiagnosticoCie10());
                Label lblTotal = new Label("Total: S/ " + String.format("%.2f", f.getTotalPresupuesto()));
                
                tarjeta.getChildren().addAll(lblFecha, lblDiag, lblTotal);
                listaFichas.getChildren().add(tarjeta);
            }
        }

        ScrollPane scroll = new ScrollPane(listaFichas);
        scroll.setFitToWidth(true);
        scroll.setPrefHeight(300);

        Button btnCerrar = new Button("Cerrar");
        btnCerrar.setOnAction(e -> modal.close());

        root.getChildren().addAll(titulo, scroll, btnCerrar);
        modal.setScene(new Scene(root, 500, 400));
        modal.showAndWait();
    }

    private void limpiarFormulario() {
        txtDni.clear();
        lblNombrePaciente.setText("");
        lblCodigoPaciente.setText("");
        lblAlergias.setText("Sin alergias registradas.");
        txtAnamnesis.clear();
        txtPrescripcion.clear();
        cbCie10.setValue(null);
        cbPlanTratamiento.setValue(null);
        listaDetalles.clear();
        totalPresupuesto = 0.0;
        lblTotalPresupuesto.setText("TOTAL: S/ 0.00");
        pacienteActual = null;
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}