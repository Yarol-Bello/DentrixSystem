package com.dentrix.controller;

import com.dentrix.model.Cie10;
import com.dentrix.service.Cie10Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;
import java.util.stream.Collectors;

public class Cie10Controller {
    private Cie10Service service;
    private TableView<Cie10> tabla;
    private ObservableList<Cie10> listaOriginal;
    
    // Componentes del panel izquierdo
    private Label lblCodigo, lblGrupo, lblPrioridad;
    private TextField txtNombre;
    private TextArea txtDescripcion;

    public Cie10Controller(TableView<Cie10> tabla, Label lblCodigo, Label lblGrupo, Label lblPrioridad, 
                           TextField txtNombre, TextArea txtDescripcion, TextField txtBuscar) {
        this.service = new Cie10Service();
        this.tabla = tabla;
        this.lblCodigo = lblCodigo; this.lblGrupo = lblGrupo; this.lblPrioridad = lblPrioridad;
        this.txtNombre = txtNombre; this.txtDescripcion = txtDescripcion;

        configurarTabla();
        cargarTabla();

        // Listener para cuando seleccionan una fila
        this.tabla.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) mostrarDetalle(newVal);
        });

        // Listener para el buscador
        txtBuscar.textProperty().addListener((obs, oldVal, newVal) -> filtrarTabla(newVal));
    }

    private void configurarTabla() {
        TableColumn<Cie10, String> colCodigo = new TableColumn<>("Código");
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        
        TableColumn<Cie10, String> colNombre = new TableColumn<>("Diagnóstico");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        
        TableColumn<Cie10, String> colGrupo = new TableColumn<>("Grupo");
        colGrupo.setCellValueFactory(new PropertyValueFactory<>("grupo"));
        
        TableColumn<Cie10, String> colPrioridad = new TableColumn<>("Prioridad");
        colPrioridad.setCellValueFactory(new PropertyValueFactory<>("prioridad"));

        tabla.getColumns().addAll(colCodigo, colNombre, colGrupo, colPrioridad);
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
            List<Cie10> filtrados = listaOriginal.stream()
                .filter(c -> c.getCodigo().toLowerCase().contains(lower) || 
                             c.getNombre().toLowerCase().contains(lower))
                .collect(Collectors.toList());
            tabla.setItems(FXCollections.observableArrayList(filtrados));
        }
    }

    private void mostrarDetalle(Cie10 c) {
        lblCodigo.setText(c.getCodigo());
        lblGrupo.setText(c.getGrupo());
        lblPrioridad.setText("⭐ " + c.getPrioridad());
        txtNombre.setText(c.getNombre());
        txtDescripcion.setText(c.getDescripcion() != null ? c.getDescripcion() : "Sin descripción disponible.");
    }
}