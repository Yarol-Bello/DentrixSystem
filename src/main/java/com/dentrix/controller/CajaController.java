package com.dentrix.controller;

import com.dentrix.model.FichaClinica;
import com.dentrix.model.Paciente;
import com.dentrix.model.Pago;
import com.dentrix.service.CajaService;
import com.dentrix.service.PacienteService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CajaController {
    private CajaService cajaService;
    private PacienteService pacienteService;
    private Paciente pacienteActual;
    private FichaClinica fichaACobrar;

    private TextField txtDni, txtMonto, txtOperacion;
    private Label lblCodigo, lblPaciente, lblSaldoPorCobrar, lblTotalCobrar, lblSaldoDeudor;
    private ComboBox<String> cbModalidad, cbMetodo;
    private TableView<Pago> tablaHistorial;

    public CajaController(TextField txtDni, Label lblCodigo, Label lblPaciente, Label lblSaldoPorCobrar, 
                          Label lblTotalCobrar, Label lblSaldoDeudor, ComboBox<String> cbModalidad, 
                          ComboBox<String> cbMetodo, TextField txtMonto, TextField txtOperacion, 
                          TableView<Pago> tablaHistorial) {
        this.cajaService = new CajaService();
        this.pacienteService = new PacienteService();
        
        this.txtDni = txtDni; this.lblCodigo = lblCodigo; this.lblPaciente = lblPaciente; 
        this.lblSaldoPorCobrar = lblSaldoPorCobrar; this.lblTotalCobrar = lblTotalCobrar; 
        this.lblSaldoDeudor = lblSaldoDeudor; this.cbModalidad = cbModalidad; this.cbMetodo = cbMetodo;
        this.txtMonto = txtMonto; this.txtOperacion = txtOperacion; this.tablaHistorial = tablaHistorial;

        configurarTablaHistorial();
        this.txtMonto.textProperty().addListener((obs, oldVal, newVal) -> calcularSaldo());
    }

    public void cargarPaciente() {
        String dni = txtDni.getText().trim();
        if (dni.length() != 8) {
            mostrarAlerta("Aviso", "DNI inválido.");
            return;
        }

        pacienteActual = pacienteService.buscarPorDni(dni);
        if (pacienteActual != null) {
            lblPaciente.setText(pacienteActual.getApellidos() + ", " + pacienteActual.getNombres());
            lblCodigo.setText(pacienteActual.getCodigoHc());
            
            fichaACobrar = cajaService.obtenerFichaPendiente(pacienteActual.getId());
            
            if (fichaACobrar != null) {
                lblTotalCobrar.setText(String.format("S/ %.2f", fichaACobrar.getSaldo()));
                lblSaldoPorCobrar.setText(String.format("Por Cobrar: S/ %.2f", fichaACobrar.getSaldo()));
                txtMonto.setText(String.format("%.2f", fichaACobrar.getSaldo()));
            } else {
                lblTotalCobrar.setText("S/ 0.00");
                lblSaldoPorCobrar.setText("Por Cobrar: S/ 0.00");
                txtMonto.setText("0.00");
                mostrarAlerta("Información", "El paciente no tiene presupuestos pendientes.");
            }
            cargarHistorialPagos();
        } else {
            pacienteActual = null;
            fichaACobrar = null;
            lblPaciente.setText("Paciente no encontrado");
            lblCodigo.setText("P-000");
            lblSaldoPorCobrar.setText("Por Cobrar: S/ 0.00");
            lblTotalCobrar.setText("S/ 0.00");
            txtMonto.setText("0.00");
            tablaHistorial.getItems().clear();
        }
    }

    private void calcularSaldo() {
        if (fichaACobrar == null) return;
        try {
            double monto = Double.parseDouble(txtMonto.getText().isEmpty() ? "0" : txtMonto.getText());
            double saldoRestante = fichaACobrar.getSaldo() - monto;
            if (saldoRestante <= 0) {
                lblSaldoDeudor.setText("S/ 0.00 (Al día)");
                lblSaldoDeudor.setStyle("-fx-text-fill: #047857; -fx-font-weight: bold;");
            } else {
                lblSaldoDeudor.setText(String.format("S/ %.2f (Pendiente)", saldoRestante));
                lblSaldoDeudor.setStyle("-fx-text-fill: #DC2626; -fx-font-weight: bold;");
            }
        } catch (NumberFormatException e) {
            lblSaldoDeudor.setText("Error");
        }
    }

    private void configurarTablaHistorial() {
        TableColumn<Pago, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getFechaPago().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
        
        TableColumn<Pago, String> colBoleta = new TableColumn<>("Comprobante");
        colBoleta.setCellValueFactory(new PropertyValueFactory<>("comprobante"));
        
        TableColumn<Pago, String> colMetodo = new TableColumn<>("Método");
        colMetodo.setCellValueFactory(new PropertyValueFactory<>("metodoPago"));
        
        TableColumn<Pago, String> colMonto = new TableColumn<>("Monto");
        colMonto.setCellValueFactory(cell -> new SimpleStringProperty("S/ " + String.format("%.2f", cell.getValue().getMonto())));

        tablaHistorial.getColumns().addAll(colFecha, colBoleta, colMetodo, colMonto);
        tablaHistorial.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void cargarHistorialPagos() {
        if (pacienteActual != null) {
            List<Pago> pagos = cajaService.listarPagosPaciente(pacienteActual.getId());
            tablaHistorial.setItems(FXCollections.observableArrayList(pagos));
        }
    }

    public void confirmarCobro() {
        if (pacienteActual == null || fichaACobrar == null) {
            mostrarAlerta("Error", "Debe cargar un paciente con saldo pendiente.");
            return;
        }

        try {
            double monto = Double.parseDouble(txtMonto.getText());
            String metodo = cbMetodo.getValue();
            String nroOp = txtOperacion.getText().trim();
            String comprobante = "B001-" + String.format("%06d", (int)(Math.random() * 1000000));

            cajaService.cobrarFicha(fichaACobrar, monto, metodo, nroOp, comprobante);
            
            mostrarAlerta("Éxito", "Pago registrado. Boleta " + comprobante + " emitida.");
            cargarPaciente(); // Recargar
            
        } catch (Exception e) {
            mostrarAlerta("Error", e.getMessage());
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
        // Métodos para que la vista actualice la boleta
    public Paciente getPacienteActual() { return pacienteActual; }
    public FichaClinica getFichaACobrar() { return fichaACobrar; }
    public Pago getUltimoPago() {
        if (tablaHistorial.getItems().isEmpty()) return null;
        return tablaHistorial.getItems().get(0); // El primero es el más reciente
    }
}