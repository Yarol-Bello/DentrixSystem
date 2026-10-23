package com.dentrix.controller;

import com.dentrix.model.Paciente;
import com.dentrix.service.PacienteService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PacientesController {
    private PacienteService service;
    private TableView<Paciente> tabla;
    private ObservableList<Paciente> listaOriginal;
    private Paciente pacienteEditando = null;

    private TextField txtDni, txtApellidos, txtNombres, txtCelular, txtCorreo, txtDireccion, txtBuscar;
    private DatePicker dpFechaNac;
    private ComboBox<String> cbSexo;
    private TextArea txtAlergias;
    private Button btnGuardar;

    public PacientesController(TableView<Paciente> tabla, TextField txtDni, TextField txtApellidos, 
                               TextField txtNombres, TextField txtCelular, TextField txtCorreo, 
                               TextField txtDireccion, DatePicker dpFechaNac, ComboBox<String> cbSexo, 
                               TextArea txtAlergias, TextField txtBuscar, Button btnGuardar) {
        this.service = new PacienteService();
        this.tabla = tabla;
        this.txtDni = txtDni; this.txtApellidos = txtApellidos; this.txtNombres = txtNombres;
        this.txtCelular = txtCelular; this.txtCorreo = txtCorreo; this.txtDireccion = txtDireccion;
        this.dpFechaNac = dpFechaNac; this.cbSexo = cbSexo; this.txtAlergias = txtAlergias;
        this.txtBuscar = txtBuscar; this.btnGuardar = btnGuardar;

        configurarTabla();
        cargarTabla();

        this.txtBuscar.textProperty().addListener((obs, oldVal, newVal) -> filtrarTabla(newVal));
    }

    private void configurarTabla() {
        TableColumn<Paciente, String> colCodigo = new TableColumn<>("Código");
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigoHc"));
        TableColumn<Paciente, String> colDni = new TableColumn<>("DNI");
        colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        TableColumn<Paciente, String> colNombres = new TableColumn<>("Paciente");
        colNombres.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getApellidos() + ", " + cellData.getValue().getNombres()));
        TableColumn<Paciente, String> colCelular = new TableColumn<>("Celular");
        colCelular.setCellValueFactory(new PropertyValueFactory<>("celular"));

        TableColumn<Paciente, Void> colAcciones = new TableColumn<>("Acciones");
        colAcciones.setCellFactory(param -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnEliminar = new Button("🗑");
            private final javafx.scene.layout.HBox pane = new javafx.scene.layout.HBox(5, btnEditar, btnEliminar);
            
            {
                btnEditar.setStyle("-fx-background-color: #F1F5F9; -fx-text-fill: #475569; -fx-font-size: 10px; -fx-border-color: #CBD5E1; -fx-border-radius: 4; -fx-cursor: hand;");
                btnEliminar.setStyle("-fx-background-color: #FEF2F2; -fx-text-fill: #DC2626; -fx-font-size: 10px; -fx-border-color: #FCA5A5; -fx-border-radius: 4; -fx-cursor: hand;");
                
                btnEditar.setOnAction(e -> cargarParaEditar(getTableView().getItems().get(getIndex())));
                btnEliminar.setOnAction(e -> eliminarPaciente(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });

        tabla.getColumns().addAll(colCodigo, colDni, colNombres, colCelular, colAcciones);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public void cargarTabla() {
        List<Paciente> pacientes = service.listarTodos();
        listaOriginal = FXCollections.observableArrayList(pacientes);
        tabla.setItems(listaOriginal);
    }

    private void filtrarTabla(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            tabla.setItems(listaOriginal);
        } else {
            String lower = filtro.toLowerCase();
            List<Paciente> filtrados = listaOriginal.stream()
                .filter(p -> p.getDni().contains(lower) || p.getApellidos().toLowerCase().contains(lower) || p.getCodigoHc().toLowerCase().contains(lower))
                .collect(Collectors.toList());
            tabla.setItems(FXCollections.observableArrayList(filtrados));
        }
    }

    public void guardarPaciente() {
        try {
            // VALIDACIONES LÓGICAS ESTRICTAS
            if (txtDni.getText().trim().isEmpty() || txtDni.getText().length() != 8) 
                throw new Exception("El DNI debe tener exactamente 8 dígitos numéricos.");
            
            if (txtApellidos.getText().trim().isEmpty() || txtNombres.getText().trim().isEmpty()) 
                throw new Exception("Nombres y apellidos son campos obligatorios.");
            
            if (!txtCelular.getText().trim().isEmpty() && txtCelular.getText().length() != 9)
                throw new Exception("El celular debe tener exactamente 9 dígitos numéricos.");

            Paciente p = pacienteEditando != null ? pacienteEditando : new Paciente();
            
            p.setDni(txtDni.getText().trim());
            p.setApellidos(txtApellidos.getText().trim());
            p.setNombres(txtNombres.getText().trim());
            p.setCelular(txtCelular.getText().trim());
            p.setCorreo(txtCorreo.getText().trim());
            p.setDireccion(txtDireccion.getText().trim());
            p.setAlergias(txtAlergias.getText().trim());
            p.setSexo(cbSexo.getValue());
            
            if (dpFechaNac.getValue() != null) p.setFechaNacimiento(Date.valueOf(dpFechaNac.getValue()));

            if (pacienteEditando == null) {
                service.registrarPaciente(p);
                mostrarAlerta("Éxito", "Paciente registrado correctamente.", Alert.AlertType.INFORMATION);
            } else {
                service.actualizarPaciente(p);
                mostrarAlerta("Éxito", "Paciente actualizado correctamente.", Alert.AlertType.INFORMATION);
            }
            
            limpiarFormulario();
            cargarTabla();
            
        } catch (Exception e) {
            mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void cargarParaEditar(Paciente p) {
        pacienteEditando = p; 
        
        txtDni.setText(p.getDni());
        txtDni.setDisable(true); 
        txtApellidos.setText(p.getApellidos());
        txtNombres.setText(p.getNombres());
        txtCelular.setText(p.getCelular());
        txtCorreo.setText(p.getCorreo());
        txtDireccion.setText(p.getDireccion());
        txtAlergias.setText(p.getAlergias());
        cbSexo.setValue(p.getSexo());
        if (p.getFechaNacimiento() != null) dpFechaNac.setValue(p.getFechaNacimiento().toLocalDate());
        
        btnGuardar.setText("💾 Guardar Cambios"); 
    }

    private void eliminarPaciente(Paciente p) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Eliminar a " + p.getNombres() + " " + p.getApellidos() + "?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(res -> {
            if (res == ButtonType.YES) {
                try {
                    service.eliminarPaciente(p.getId()); 
                    mostrarAlerta("Eliminado", "Paciente eliminado correctamente.", Alert.AlertType.INFORMATION);
                    cargarTabla(); 
                } catch (Exception e) {
                    mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
    }

    private void limpiarFormulario() {
        pacienteEditando = null;
        txtDni.clear(); txtDni.setDisable(false);
        txtApellidos.clear(); txtNombres.clear(); txtCelular.clear();
        txtCorreo.clear(); txtDireccion.clear(); txtAlergias.clear();
        dpFechaNac.setValue(null); cbSexo.setValue(null);
        btnGuardar.setText("💾 Guardar Paciente");
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}