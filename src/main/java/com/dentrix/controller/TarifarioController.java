package com.dentrix.controller;

import com.dentrix.model.Procedimiento;
import com.dentrix.service.ProcedimientoService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;
import java.util.stream.Collectors;

public class TarifarioController {
    private ProcedimientoService service;
    private TableView<Procedimiento> tabla;
    private ObservableList<Procedimiento> listaOriginal;
    private Procedimiento procedimientoEditando = null;

    private TextField txtCodigo, txtNombre, txtPrecio, txtBuscar;
    private ComboBox<String> cbEspecialidad, cbCalculo;
    private Button btnGuardar;
    private Label lblTituloForm;

    public TarifarioController(TableView<Procedimiento> tabla, TextField txtCodigo, TextField txtNombre, 
                               TextField txtPrecio, ComboBox<String> cbEspecialidad, ComboBox<String> cbCalculo, 
                               TextField txtBuscar, Button btnGuardar, Label lblTituloForm) {
        this.service = new ProcedimientoService();
        this.tabla = tabla;
        this.txtCodigo = txtCodigo; this.txtNombre = txtNombre; this.txtPrecio = txtPrecio;
        this.cbEspecialidad = cbEspecialidad; this.cbCalculo = cbCalculo;
        this.txtBuscar = txtBuscar; this.btnGuardar = btnGuardar; this.lblTituloForm = lblTituloForm;

        configurarTabla();
        cargarTabla();

        this.txtBuscar.textProperty().addListener((obs, oldVal, newVal) -> filtrarTabla(newVal));
    }

    private void configurarTabla() {
        TableColumn<Procedimiento, String> colCodigo = new TableColumn<>("Código");
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        
        TableColumn<Procedimiento, String> colNombre = new TableColumn<>("Procedimiento");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        
        TableColumn<Procedimiento, String> colEsp = new TableColumn<>("Especialidad");
        colEsp.setCellValueFactory(new PropertyValueFactory<>("especialidad"));
        
        TableColumn<Procedimiento, String> colCalc = new TableColumn<>("Cálculo");
        colCalc.setCellValueFactory(new PropertyValueFactory<>("calculo"));
        
        TableColumn<Procedimiento, String> colPrecio = new TableColumn<>("Tarifa");
        colPrecio.setCellValueFactory(cell -> new SimpleStringProperty("S/ " + String.format("%.2f", cell.getValue().getPrecio())));

        TableColumn<Procedimiento, Void> colAccion = new TableColumn<>("Acciones");
        colAccion.setCellFactory(param -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnEliminar = new Button("🗑");
            private final javafx.scene.layout.HBox pane = new javafx.scene.layout.HBox(5, btnEditar, btnEliminar);
            
            {
                btnEditar.setStyle("-fx-background-color: #F1F5F9; -fx-text-fill: #475569; -fx-font-size: 10px; -fx-border-color: #CBD5E1; -fx-border-radius: 4; -fx-cursor: hand;");
                btnEliminar.setStyle("-fx-background-color: #FEF2F2; -fx-text-fill: #DC2626; -fx-font-size: 10px; -fx-border-color: #FCA5A5; -fx-border-radius: 4; -fx-cursor: hand;");
                pane.setAlignment(javafx.geometry.Pos.CENTER);
                
                btnEditar.setOnAction(e -> cargarParaEditar(getTableView().getItems().get(getIndex())));
                btnEliminar.setOnAction(e -> eliminarProcedimiento(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });

        tabla.getColumns().addAll(colCodigo, colNombre, colEsp, colCalc, colPrecio, colAccion);
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
            List<Procedimiento> filtrados = listaOriginal.stream()
                .filter(p -> p.getNombre().toLowerCase().contains(lower) || p.getCodigo().toLowerCase().contains(lower))
                .collect(Collectors.toList());
            tabla.setItems(FXCollections.observableArrayList(filtrados));
        }
    }

    public void guardarProcedimiento() {
        try {
            if (txtCodigo.getText().trim().isEmpty() || txtNombre.getText().trim().isEmpty())
                throw new Exception("Código y Nombre son obligatorios.");
            if (cbEspecialidad.getValue() == null || cbCalculo.getValue() == null)
                throw new Exception("Seleccione especialidad y cálculo.");

            Procedimiento p = procedimientoEditando != null ? procedimientoEditando : new Procedimiento();
            p.setCodigo(txtCodigo.getText().trim().toUpperCase());
            p.setNombre(txtNombre.getText().trim());
            p.setEspecialidad(cbEspecialidad.getValue());
            p.setCalculo(cbCalculo.getValue());
            p.setPrecio(Double.parseDouble(txtPrecio.getText()));

            if (procedimientoEditando == null) {
                service.guardar(p);
                mostrarAlerta("Éxito", "Procedimiento creado correctamente.");
            } else {
                service.actualizar(p);
                mostrarAlerta("Éxito", "Procedimiento actualizado correctamente.");
            }
            limpiarFormulario();
            cargarTabla();
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El precio debe ser un número válido.");
        } catch (Exception e) {
            mostrarAlerta("Error", e.getMessage());
        }
    }

    private void cargarParaEditar(Procedimiento p) {
        procedimientoEditando = p;
        txtCodigo.setText(p.getCodigo());
        txtCodigo.setDisable(true); // El código no se cambia para no romper la BD
        txtNombre.setText(p.getNombre());
        txtPrecio.setText(String.format("%.2f", p.getPrecio()));
        cbEspecialidad.setValue(p.getEspecialidad());
        cbCalculo.setValue(p.getCalculo());
        btnGuardar.setText("💾 Guardar Cambios");
        lblTituloForm.setText("Editando Procedimiento");
    }

    private void eliminarProcedimiento(Procedimiento p) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Eliminar el procedimiento " + p.getCodigo() + "?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(res -> {
            if (res == ButtonType.YES) {
                try {
                    service.eliminar(p.getId());
                    cargarTabla();
                } catch (Exception e) {
                    mostrarAlerta("Error", e.getMessage());
                }
            }
        });
    }

    private void limpiarFormulario() {
        procedimientoEditando = null;
        txtCodigo.clear(); txtCodigo.setDisable(false);
        txtNombre.clear(); txtPrecio.clear();
        cbEspecialidad.setValue(null); cbCalculo.setValue(null);
        btnGuardar.setText("💾 Crear Procedimiento");
        lblTituloForm.setText("Nuevo Procedimiento");
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}