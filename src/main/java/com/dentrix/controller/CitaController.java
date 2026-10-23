package com.dentrix.controller;

import com.dentrix.model.Cita;
import com.dentrix.model.Paciente;
import com.dentrix.model.Procedimiento;
import com.dentrix.model.Usuario;
import com.dentrix.service.CitaService;
import com.dentrix.service.PacienteService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

public class CitaController {
    private CitaService citaService;
    private PacienteService pacienteService;
    private Paciente pacienteSeleccionado;
    private Cita citaEditando = null;

    private TextField txtDniBuscar, txtBuscarTabla;
    private VBox cardPaciente;
    private Label lblIniciales, lblNombre, lblDni, lblEdad, lblCelular, lblContadorCitas, lblTituloForm;
    private ComboBox<Usuario> cbOdontologo;
    private ComboBox<Procedimiento> cbTratamiento;
    private ComboBox<String> cbConsultorio, cbHorario;
    private DatePicker dpFechaCita, dpFechaTabla;
    private TableView<Cita> tabla;
    private Button btnAgendar;

    public CitaController(TextField txtDniBuscar, VBox cardPaciente, Label lblIniciales, Label lblNombre, 
                          Label lblDni, Label lblEdad, Label lblCelular, ComboBox<Usuario> cbOdontologo, 
                          ComboBox<Procedimiento> cbTratamiento, ComboBox<String> cbConsultorio, 
                          DatePicker dpFechaCita, ComboBox<String> cbHorario, TableView<Cita> tabla, 
                          Label lblContadorCitas, TextField txtBuscarTabla, DatePicker dpFechaTabla, 
                          Label lblTituloForm, Button btnAgendar) {
        this.citaService = new CitaService();
        this.pacienteService = new PacienteService();
        
        this.txtDniBuscar = txtDniBuscar; this.cardPaciente = cardPaciente; this.lblIniciales = lblIniciales;
        this.lblNombre = lblNombre; this.lblDni = lblDni; this.lblEdad = lblEdad; this.lblCelular = lblCelular;
        this.cbOdontologo = cbOdontologo; this.cbTratamiento = cbTratamiento; this.cbConsultorio = cbConsultorio;
        this.dpFechaCita = dpFechaCita; this.cbHorario = cbHorario; this.tabla = tabla;
        this.lblContadorCitas = lblContadorCitas; this.txtBuscarTabla = txtBuscarTabla;
        this.dpFechaTabla = dpFechaTabla; this.lblTituloForm = lblTituloForm; this.btnAgendar = btnAgendar;

        inicializarCombos();
        configurarTabla();
        cargarCitasDelDia(LocalDate.now());

        this.dpFechaTabla.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) cargarCitasDelDia(newVal);
        });

        this.txtBuscarTabla.textProperty().addListener((obs, oldVal, newVal) -> filtrarTabla(newVal));
    }

    private void inicializarCombos() {
        cbOdontologo.setItems(FXCollections.observableArrayList(citaService.obtenerOdontologos()));
        cbTratamiento.setItems(FXCollections.observableArrayList(citaService.obtenerProcedimientos()));
        cbConsultorio.setItems(FXCollections.observableArrayList("Consultorio 01", "Consultorio 02"));
        cbHorario.setItems(FXCollections.observableArrayList(
                "08:00 - 08:45", "09:00 - 09:45", "10:00 - 10:45", "11:00 - 11:45", 
                "15:00 - 15:45", "16:00 - 16:45", "17:00 - 17:45"));
    }

    public void buscarPaciente() {
        String dni = txtDniBuscar.getText().trim();
        if (dni.isEmpty() || dni.length() != 8) {
            mostrarAlerta("Aviso", "Ingrese un DNI válido de 8 dígitos.");
            return;
        }

        pacienteSeleccionado = pacienteService.buscarPorDni(dni);
        if (pacienteSeleccionado != null) {
            lblNombre.setText(pacienteSeleccionado.getApellidos() + ", " + pacienteSeleccionado.getNombres());
            lblDni.setText("DNI: " + pacienteSeleccionado.getDni());
            
            if (pacienteSeleccionado.getFechaNacimiento() != null) {
                int edad = Period.between(pacienteSeleccionado.getFechaNacimiento().toLocalDate(), LocalDate.now()).getYears();
                lblEdad.setText("· " + edad + " años");
            } else {
                lblEdad.setText("");
            }
            
            lblCelular.setText("📱 " + (pacienteSeleccionado.getCelular() != null ? pacienteSeleccionado.getCelular() : "No registrado"));
            
            String nom = pacienteSeleccionado.getNombres().substring(0,1).toUpperCase();
            String ape = pacienteSeleccionado.getApellidos().substring(0,1).toUpperCase();
            lblIniciales.setText(nom + ape);

            cardPaciente.setVisible(true);
            cardPaciente.setManaged(true);
        } else {
            pacienteSeleccionado = null;
            cardPaciente.setVisible(false);
            cardPaciente.setManaged(false);
            mostrarAlerta("No encontrado", "No existe un paciente con ese DNI.");
        }
    }

    public void guardarCita() {
        if (pacienteSeleccionado == null) {
            mostrarAlerta("Validación", "Debe buscar un paciente primero.");
            return;
        }
        if (cbOdontologo.getValue() == null || cbTratamiento.getValue() == null || 
            cbConsultorio.getValue() == null || dpFechaCita.getValue() == null || cbHorario.getValue() == null) {
            mostrarAlerta("Validación", "Complete todos los campos de la cita.");
            return;
        }

        try {
            Cita c = citaEditando != null ? citaEditando : new Cita();
            c.setPacienteId(pacienteSeleccionado.getId());
            c.setOdontologoId(cbOdontologo.getValue().getId());
            c.setProcedimientoId(cbTratamiento.getValue().getId());
            c.setConsultorio(cbConsultorio.getValue());
            c.setFecha(Date.valueOf(dpFechaCita.getValue()));
            
            String[] horas = cbHorario.getValue().split(" - ");
            c.setHoraInicio(Time.valueOf(horas[0] + ":00"));
            c.setHoraFin(Time.valueOf(horas[1] + ":00"));
            
            LocalTime hora = LocalTime.parse(horas[0]);
            c.setTurno(hora.isBefore(LocalTime.NOON) ? "MAÑANA" : "TARDE");

            if (citaEditando == null) {
                citaService.guardarCita(c, false);
                mostrarAlerta("Éxito", "Cita agendada correctamente.");
            } else {
                citaService.guardarCita(c, true);
                mostrarAlerta("Éxito", "Cita actualizada correctamente.");
            }
            
            limpiarFormulario();
            cargarCitasDelDia(dpFechaCita.getValue());
            
        } catch (Exception e) {
            mostrarAlerta("Error", e.getMessage());
        }
    }

    private void configurarTabla() {
        TableColumn<Cita, String> colHora = new TableColumn<>("Horario");
        colHora.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getHoraInicio().toString().substring(0,5) + " - " + 
                cell.getValue().getHoraFin().toString().substring(0,5)));
        
        TableColumn<Cita, String> colPaciente = new TableColumn<>("Paciente");
        colPaciente.setCellValueFactory(new PropertyValueFactory<>("pacienteNombre"));
        
        TableColumn<Cita, String> colDni = new TableColumn<>("DNI");
        colDni.setCellValueFactory(new PropertyValueFactory<>("dniPaciente"));
        
        TableColumn<Cita, String> colConsul = new TableColumn<>("Consultorio");
        colConsul.setCellValueFactory(new PropertyValueFactory<>("consultorio"));
        
        TableColumn<Cita, String> colTrat = new TableColumn<>("Tratamiento");
        colTrat.setCellValueFactory(new PropertyValueFactory<>("procedimientoNombre"));

        TableColumn<Cita, Void> colAccion = new TableColumn<>("Acción");
        colAccion.setCellFactory(param -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            {
                btnEditar.setStyle("-fx-background-color: #F1F5F9; -fx-text-fill: #475569; -fx-font-size: 10px; -fx-border-color: #CBD5E1; -fx-border-radius: 4; -fx-cursor: hand;");
                btnEditar.setOnAction(e -> cargarParaEditar(getTableView().getItems().get(getIndex())));
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnEditar);
            }
        });

        tabla.getColumns().addAll(colHora, colPaciente, colDni, colConsul, colTrat, colAccion);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void cargarCitasDelDia(LocalDate fecha) {
        List<Cita> citas = citaService.listarCitasDelDia(Date.valueOf(fecha));
        tabla.setItems(FXCollections.observableArrayList(citas));
        lblContadorCitas.setText(citas.size() + " citas");
    }

    private void filtrarTabla(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            cargarCitasDelDia(dpFechaTabla.getValue());
        } else {
            String lower = filtro.toLowerCase();
            List<Cita> filtradas = citaService.listarCitasDelDia(Date.valueOf(dpFechaTabla.getValue()))
                    .stream()
                    .filter(c -> c.getDniPaciente().contains(lower) || 
                                 c.getPacienteNombre().toLowerCase().contains(lower))
                    .collect(Collectors.toList());
            tabla.setItems(FXCollections.observableArrayList(filtradas));
        }
    }

    private void cargarParaEditar(Cita c) {
        citaEditando = c;
        
        pacienteSeleccionado = pacienteService.buscarPorDni(c.getDniPaciente());
        if(pacienteSeleccionado != null) {
            txtDniBuscar.setText(pacienteSeleccionado.getDni());
            buscarPaciente();
        }

        for (Usuario u : cbOdontologo.getItems()) {
            if (u.getId() == c.getOdontologoId()) { cbOdontologo.setValue(u); break; }
        }
        
        for (Procedimiento p : cbTratamiento.getItems()) {
            if (p.getId() == c.getProcedimientoId()) { cbTratamiento.setValue(p); break; }
        }

        cbConsultorio.setValue(c.getConsultorio());
        dpFechaCita.setValue(c.getFecha().toLocalDate());
        
        String horarioStr = c.getHoraInicio().toString().substring(0,5) + " - " + c.getHoraFin().toString().substring(0,5);
        cbHorario.setValue(horarioStr);

        lblTituloForm.setText("Editando Cita");
        btnAgendar.setText("💾 Actualizar Cita");
    }

    private void limpiarFormulario() {
        citaEditando = null;
        txtDniBuscar.clear();
        cardPaciente.setVisible(false);
        cardPaciente.setManaged(false);
        pacienteSeleccionado = null;
        cbOdontologo.setValue(null);
        cbTratamiento.setValue(null);
        cbConsultorio.setValue(null);
        cbHorario.setValue(null);
        dpFechaCita.setValue(LocalDate.now());
        lblTituloForm.setText("Nueva Cita");
        btnAgendar.setText("💾 Agendar Cita");
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}