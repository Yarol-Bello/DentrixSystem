package com.dentrix.controller;

import com.dentrix.model.ConfiguracionClinica;
import com.dentrix.service.ConfiguracionService;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class ConfiguracionController {
    private ConfiguracionService service;
    private ConfiguracionClinica configActual;

    private TextField txtRuc, txtRazonSocial, txtNombreComercial, txtDireccion, txtSerie, 
                      txtCelularYape, txtTitularYape, txtDirector, txtCop, txtRne, txtLicencia;

    public ConfiguracionController(TextField txtRuc, TextField txtRazonSocial, TextField txtNombreComercial, 
                                   TextField txtDireccion, TextField txtSerie, TextField txtCelularYape, 
                                   TextField txtTitularYape, TextField txtDirector, TextField txtCop, 
                                   TextField txtRne, TextField txtLicencia) {
        this.service = new ConfiguracionService();
        this.txtRuc = txtRuc; this.txtRazonSocial = txtRazonSocial; this.txtNombreComercial = txtNombreComercial;
        this.txtDireccion = txtDireccion; this.txtSerie = txtSerie; this.txtCelularYape = txtCelularYape;
        this.txtTitularYape = txtTitularYape; this.txtDirector = txtDirector; this.txtCop = txtCop;
        this.txtRne = txtRne; this.txtLicencia = txtLicencia;

        cargarDatos();
    }

    private void cargarDatos() {
        configActual = service.obtenerConfiguracion();
        if (configActual != null) {
            txtRuc.setText(configActual.getRuc());
            txtRazonSocial.setText(configActual.getRazonSocial());
            txtNombreComercial.setText(configActual.getNombreComercial());
            txtDireccion.setText(configActual.getDireccionFiscal());
            txtSerie.setText(configActual.getSerieBoleta());
            txtCelularYape.setText(configActual.getCelularYape());
            txtTitularYape.setText(configActual.getTitularYape());
            txtDirector.setText(configActual.getDirectorMedico());
            txtCop.setText(configActual.getCop());
            txtRne.setText(configActual.getRne());
            txtLicencia.setText(configActual.getLicenciaMunicipal());
        }
    }

    public void guardarDatos() {
        try {
            if (configActual == null) throw new Exception("No se encontró configuración previa.");
            
            configActual.setRuc(txtRuc.getText().trim());
            configActual.setRazonSocial(txtRazonSocial.getText().trim());
            configActual.setNombreComercial(txtNombreComercial.getText().trim());
            configActual.setDireccionFiscal(txtDireccion.getText().trim());
            configActual.setSerieBoleta(txtSerie.getText().trim());
            configActual.setCelularYape(txtCelularYape.getText().trim());
            configActual.setTitularYape(txtTitularYape.getText().trim());
            configActual.setDirectorMedico(txtDirector.getText().trim());
            configActual.setCop(txtCop.getText().trim());
            configActual.setRne(txtRne.getText().trim());
            configActual.setLicenciaMunicipal(txtLicencia.getText().trim());

            service.guardarConfiguracion(configActual);
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Configuración guardada correctamente.");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage());
            alert.showAndWait();
        }
    }
}