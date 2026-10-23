package com.dentrix.view;

import com.dentrix.model.Paciente;
import com.dentrix.service.PacienteService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PacientesView extends HBox {

    private PacienteService service;
    private TableView<Paciente> tabla;
    private ObservableList<Paciente> listaOriginal;
    private Paciente pacienteEditando = null;

    private TextField txtDni, txtApellidos, txtNombres, txtCelular, txtCorreo, txtDireccion, txtBuscar;
    private DatePicker dpFechaNac;
    private ComboBox<String> cbSexo;
    private TextArea txtAlergias;
    private Button btnGuardar, btnCancelar;

    public PacientesView() {
        this.service = new PacienteService();
        this.setSpacing(20);
        this.setPadding(new Insets(20));
        this.setStyle("-fx-background-color: #F1F5F9;");

        VBox formPanel = crearFormulario();
        HBox.setHgrow(formPanel, Priority.ALWAYS);

        VBox tablePanel = crearTabla();
        HBox.setHgrow(tablePanel, Priority.ALWAYS);

        this.getChildren().addAll(formPanel, tablePanel);
        cargarTabla();
    }

    private VBox crearFormulario() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(15));
        panel.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);");
        panel.setMaxWidth(400);

        Label titulo = new Label("Registro de Paciente");
        titulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #0F172A;");

        txtDni = crearInputNumerico("DNI (8 dígitos)", 8);
        txtApellidos = crearInput("Apellidos");
        txtNombres = crearInput("Nombres");
        
        dpFechaNac = new DatePicker();
        dpFechaNac.setPromptText("Fecha Nacimiento");
        dpFechaNac.getStyleClass().add("form-input");
        dpFechaNac.setMaxWidth(Double.MAX_VALUE);

        cbSexo = new ComboBox<>();
        cbSexo.getItems().addAll("Masculino", "Femenino");
        cbSexo.setPromptText("Sexo");
        cbSexo.getStyleClass().add("form-input");
        cbSexo.setMaxWidth(Double.MAX_VALUE);

        txtCelular = crearInputNumerico("Celular (9 dígitos)", 9);
        txtCorreo = crearInput("Correo (Opcional)");
        txtDireccion = crearInput("Dirección");

        txtAlergias = new TextArea();
        txtAlergias.setPromptText("Alergias / Alertas médicas");
        txtAlergias.getStyleClass().add("form-input");
        txtAlergias.setPrefRowCount(2);

        btnGuardar = new Button("Guardar Paciente");
        btnGuardar.getStyleClass().add("btn-primary");
        btnGuardar.setMaxWidth(Double.MAX_VALUE);
        btnGuardar.setOnAction(e -> guardarPaciente());

        btnCancelar = new Button("Cancelar");
        btnCancelar.getStyleClass().add("btn-secondary");
        btnCancelar.setMaxWidth(Double.MAX_VALUE);
        btnCancelar.setVisible(false);
        btnCancelar.setManaged(false);
        btnCancelar.setOnAction(e -> limpiarFormulario());

        HBox btnBox = new HBox(10, btnGuardar, btnCancelar);
        HBox.setHgrow(btnGuardar, Priority.ALWAYS);
        HBox.setHgrow(btnCancelar, Priority.ALWAYS);

        panel.getChildren().addAll(
            titulo,
            crearGrupo("DNI", txtDni),
            crearGrupo("Apellidos", txtApellidos),
            crearGrupo("Nombres", txtNombres),
            crearGrupo("F. Nacimiento", dpFechaNac),
            crearGrupo("Sexo", cbSexo),
            crearGrupo("Celular", txtCelular),
            crearGrupo("Correo", txtCorreo),
            crearGrupo("Dirección", txtDireccion),
            crearGrupo("Alergias", txtAlergias),
            btnBox
        );
        return panel;
    }

    private VBox crearTabla() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(15));
        panel.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);");

        Label titulo = new Label("Padrón de Pacientes");
        titulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #0F172A;");

        txtBuscar = new TextField();
        txtBuscar.setPromptText("Buscar por DNI...");
        txtBuscar.getStyleClass().add("form-input");
        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> filtrarTabla(newValue));

        tabla = new TableView<>();
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Paciente, String> colCodigo = new TableColumn<>("Código");
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigoHc"));

        TableColumn<Paciente, String> colDni = new TableColumn<>("DNI");
        colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));

        TableColumn<Paciente, String> colNombres = new TableColumn<>("Paciente");
        colNombres.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getApellidos() + ", " + cellData.getValue().getNombres())
        );

        TableColumn<Paciente, Void> colAcciones = new TableColumn<>("Acciones");
        colAcciones.setCellFactory(param -> new TableCell<>() {
            private final Button btnEdit = new Button("Editar");
            private final Button btnDel = new Button("Eliminar");
            private final HBox pane = new HBox(5, btnEdit, btnDel);

            {
                btnEdit.getStyleClass().add("btn-secondary");
                btnDel.getStyleClass().add("btn-danger");
                
                btnEdit.setOnAction(event -> cargarParaEditar(getTableView().getItems().get(getIndex())));
                btnDel.setOnAction(event -> eliminarPaciente(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });

        tabla.getColumns().addAll(colCodigo, colDni, colNombres, colAcciones);
        VBox.setVgrow(tabla, Priority.ALWAYS);

        panel.getChildren().addAll(titulo, txtBuscar, tabla);
        return panel;
    }

    private TextField crearInputNumerico(String prompt, int maxLength) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.getStyleClass().add("form-input");
        tf.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d{0," + maxLength + "}")) {
                return change;
            }
            return null;
        }));
        return tf;
    }

    private TextField crearInput(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.getStyleClass().add("form-input");
        return tf;
    }

    private VBox crearGrupo(String label, Control control) {
        Label lbl = new Label(label);
        lbl.getStyleClass().add("form-label");
        return new VBox(4, lbl, control);
    }

    private void guardarPaciente() {
        try {
            Paciente p = pacienteEditando != null ? pacienteEditando : new Paciente();
            
            p.setDni(txtDni.getText().trim());
            p.setApellidos(txtApellidos.getText().trim());
            p.setNombres(txtNombres.getText().trim());
            p.setFechaNacimiento(dpFechaNac.getValue() != null ? Date.valueOf(dpFechaNac.getValue()) : null);
            p.setSexo(cbSexo.getValue());
            p.setCelular(txtCelular.getText().trim());
            p.setCorreo(txtCorreo.getText().trim());
            p.setDireccion(txtDireccion.getText().trim());
            p.setAlergias(txtAlergias.getText().trim());

            if (pacienteEditando == null) {
                service.registrarPaciente(p);
                mostrarAlerta("Éxito", "Paciente registrado correctamente.", Alert.AlertType.INFORMATION);
            } else {
                service.actualizarPaciente(p);
                mostrarAlerta("Éxito", "Paciente actualizado correctamente.", Alert.AlertType.INFORMATION);
            }
            
            limpiarFormulario();
            cargarTabla();
            
        } catch (IllegalArgumentException e) {
            mostrarAlerta("Validación", e.getMessage(), Alert.AlertType.WARNING);
        } catch (Exception e) {
            mostrarAlerta("Error", "Error inesperado: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void eliminarPaciente(Paciente p) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Está seguro de eliminar a " + p.getNombres() + "?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    service.eliminarPaciente(p.getId());
                    cargarTabla();
                } catch (Exception e) {
                    mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
    }

    private void cargarParaEditar(Paciente p) {
        pacienteEditando = p;
        txtDni.setText(p.getDni());
        txtDni.setDisable(true);
        txtApellidos.setText(p.getApellidos());
        txtNombres.setText(p.getNombres());
        dpFechaNac.setValue(p.getFechaNacimiento() != null ? p.getFechaNacimiento().toLocalDate() : null);
        cbSexo.setValue(p.getSexo());
        txtCelular.setText(p.getCelular());
        txtCorreo.setText(p.getCorreo());
        txtDireccion.setText(p.getDireccion());
        txtAlergias.setText(p.getAlergias());

        btnGuardar.setText("Actualizar Paciente");
        btnCancelar.setVisible(true);
        btnCancelar.setManaged(true);
    }

    private void limpiarFormulario() {
        pacienteEditando = null;
        txtDni.clear();
        txtDni.setDisable(false);
        txtApellidos.clear();
        txtNombres.clear();
        dpFechaNac.setValue(null);
        cbSexo.setValue(null);
        txtCelular.clear();
        txtCorreo.clear();
        txtDireccion.clear();
        txtAlergias.clear();
        
        btnGuardar.setText("Guardar Paciente");
        btnCancelar.setVisible(false);
        btnCancelar.setManaged(false);
    }

    private void cargarTabla() {
        listaOriginal = FXCollections.observableArrayList(service.obtenerTodos());
        tabla.setItems(listaOriginal);
    }

    private void filtrarTabla(String dni) {
        if (dni == null || dni.isEmpty()) {
            tabla.setItems(listaOriginal);
        } else {
            List<Paciente> filtrados = listaOriginal.stream()
                .filter(p -> p.getDni().contains(dni))
                .collect(Collectors.toList());
            tabla.setItems(FXCollections.observableArrayList(filtrados));
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}