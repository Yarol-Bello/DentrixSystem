package com.dentrix.controller;

import com.dentrix.model.Usuario;
import com.dentrix.service.UsuarioService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;
import java.util.stream.Collectors;

public class UsuariosController {
    private UsuarioService service;
    private TableView<Usuario> tabla;
    private ObservableList<Usuario> listaOriginal;
    private Usuario usuarioEditando = null;

    private TextField txtDni, txtApellidos, txtNombres, txtUsuario, txtPassword, txtBuscar;
    private ComboBox<String> cbRol, cbConsultorio;
    private Button btnGuardar;
    private Label lblTituloForm;

    public UsuariosController(TableView<Usuario> tabla, TextField txtDni, TextField txtApellidos, 
                              TextField txtNombres, TextField txtUsuario, TextField txtPassword, 
                              ComboBox<String> cbRol, ComboBox<String> cbConsultorio, 
                              TextField txtBuscar, Button btnGuardar, Label lblTituloForm) {
        this.service = new UsuarioService();
        this.tabla = tabla;
        this.txtDni = txtDni; this.txtApellidos = txtApellidos; this.txtNombres = txtNombres;
        this.txtUsuario = txtUsuario; this.txtPassword = txtPassword;
        this.cbRol = cbRol; this.cbConsultorio = cbConsultorio;
        this.txtBuscar = txtBuscar; this.btnGuardar = btnGuardar;
        this.lblTituloForm = lblTituloForm;

        configurarTabla();
        cargarTabla();

        this.txtBuscar.textProperty().addListener((obs, oldVal, newVal) -> filtrarTabla(newVal));
    }

    private void configurarTabla() {
        TableColumn<Usuario, String> colUser = new TableColumn<>("Usuario");
        colUser.setCellValueFactory(new PropertyValueFactory<>("username"));
        
        TableColumn<Usuario, String> colNombre = new TableColumn<>("Personal");
        colNombre.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getApellidos() + ", " + cell.getValue().getNombres()));
        
        TableColumn<Usuario, String> colRol = new TableColumn<>("Rol");
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        
        TableColumn<Usuario, String> colCons = new TableColumn<>("Consultorio");
        colCons.setCellValueFactory(new PropertyValueFactory<>("consultorioAsignado"));

        TableColumn<Usuario, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().isActivo() ? "● Activo" : "○ Inactivo"));
        colEstado.setStyle("-fx-alignment: CENTER;");

        TableColumn<Usuario, Void> colAccion = new TableColumn<>("Acciones");
        colAccion.setCellFactory(param -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnEstado = new Button();
            private final javafx.scene.layout.HBox pane = new javafx.scene.layout.HBox(5, btnEditar, btnEstado);
            
            {
                btnEditar.setStyle("-fx-background-color: #F1F5F9; -fx-text-fill: #475569; -fx-font-size: 10px; -fx-border-color: #CBD5E1; -fx-border-radius: 4; -fx-cursor: hand;");
                btnEstado.setStyle("-fx-background-color: #FEF2F2; -fx-text-fill: #DC2626; -fx-font-size: 10px; -fx-border-color: #FCA5A5; -fx-border-radius: 4; -fx-cursor: hand;");
                pane.setAlignment(javafx.geometry.Pos.CENTER);
                
                btnEditar.setOnAction(e -> cargarParaEditar(getTableView().getItems().get(getIndex())));
                btnEstado.setOnAction(e -> toggleEstado(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Usuario u = getTableView().getItems().get(getIndex());
                    if (u.isActivo()) {
                        btnEstado.setText("Desactivar");
                        btnEstado.setStyle("-fx-background-color: #FEF2F2; -fx-text-fill: #DC2626; -fx-font-size: 10px; -fx-border-color: #FCA5A5; -fx-border-radius: 4; -fx-cursor: hand;");
                    } else {
                        btnEstado.setText("Activar");
                        btnEstado.setStyle("-fx-background-color: #ECFDF5; -fx-text-fill: #047857; -fx-font-size: 10px; -fx-border-color: #A7F3D0; -fx-border-radius: 4; -fx-cursor: hand;");
                    }
                    setGraphic(pane);
                }
            }
        });

        tabla.getColumns().addAll(colUser, colNombre, colRol, colCons, colEstado, colAccion);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public void cargarTabla() {
        listaOriginal = FXCollections.observableArrayList(service.listarTodos());
        tabla.setItems(listaOriginal);
    }

    private void filtrarTabla(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            tabla.setItems(listaOriginal);
        } else {
            String lower = filtro.toLowerCase();
            List<Usuario> filtrados = listaOriginal.stream()
                .filter(u -> u.getUsername().toLowerCase().contains(lower) || 
                             u.getApellidos().toLowerCase().contains(lower) ||
                             u.getDni().contains(lower))
                .collect(Collectors.toList());
            tabla.setItems(FXCollections.observableArrayList(filtrados));
        }
    }

    public void guardarUsuario() {
        try {
            if (txtDni.getText().length() != 8) throw new Exception("El DNI debe tener 8 dígitos.");
            if (txtApellidos.getText().trim().isEmpty() || txtNombres.getText().trim().isEmpty()) 
                throw new Exception("Nombres y Apellidos son obligatorios.");
            if (txtUsuario.getText().trim().isEmpty()) throw new Exception("El Usuario es obligatorio.");

            Usuario u = usuarioEditando != null ? usuarioEditando : new Usuario();
            u.setDni(txtDni.getText().trim());
            u.setApellidos(txtApellidos.getText().trim());
            u.setNombres(txtNombres.getText().trim());
            u.setUsername(txtUsuario.getText().trim());
            u.setRol(cbRol.getValue());
            u.setConsultorioAsignado(cbConsultorio.getValue());

            if (usuarioEditando == null) {
                service.registrar(u, txtPassword.getText());
                mostrarAlerta("Éxito", "Usuario creado correctamente.");
            } else {
                service.actualizar(u, txtPassword.getText());
                mostrarAlerta("Éxito", "Usuario actualizado correctamente.");
            }
            limpiarFormulario();
            cargarTabla();
        } catch (Exception e) {
            mostrarAlerta("Error", e.getMessage());
        }
    }

    private void cargarParaEditar(Usuario u) {
        usuarioEditando = u;
        txtDni.setText(u.getDni());
        txtDni.setDisable(true);
        txtApellidos.setText(u.getApellidos());
        txtNombres.setText(u.getNombres());
        txtUsuario.setText(u.getUsername());
        cbRol.setValue(u.getRol());
        cbConsultorio.setValue(u.getConsultorioAsignado());
        txtPassword.clear();
        btnGuardar.setText("💾 Guardar Cambios");
        lblTituloForm.setText("Editando Usuario");
    }

    private void toggleEstado(Usuario u) {
        try {
            service.cambiarEstadoUsuario(u.getId(), !u.isActivo());
            cargarTabla();
        } catch (Exception e) {
            mostrarAlerta("Error", e.getMessage());
        }
    }

    private void limpiarFormulario() {
        usuarioEditando = null;
        txtDni.clear(); txtDni.setDisable(false);
        txtApellidos.clear(); txtNombres.clear();
        txtUsuario.clear(); txtPassword.clear();
        cbRol.setValue(null); cbConsultorio.setValue(null);
        btnGuardar.setText("💾 Crear Usuario");
        lblTituloForm.setText("Nuevo Usuario");
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}