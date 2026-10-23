package com.dentrix.controller;

import com.dentrix.model.Usuario;
import com.dentrix.service.UsuarioService;
import com.dentrix.util.SesionGlobal;
import com.dentrix.view.DashboardView;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    private TextField txtUsuario;
    private PasswordField txtPassword;
    private Label lblError;

    public LoginController(TextField txtUsuario, PasswordField txtPassword, Label lblError) {
        this.txtUsuario = txtUsuario;
        this.txtPassword = txtPassword;
        this.lblError = lblError;
    }

    public void procesarLogin(Stage stageActual) {
        String user = txtUsuario.getText().trim();
        String pass = txtPassword.getText().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            mostrarError("Ingrese sus credenciales.");
            return;
        }

        try {
            UsuarioService service = new UsuarioService();
            Usuario usuario = service.login(user, pass); 

            SesionGlobal.usuarioLogueado = usuario;
            
            // Abrir Dashboard y cerrar Login
            DashboardView dashboard = new DashboardView();
            Stage dashboardStage = new Stage();
            dashboard.mostrar(dashboardStage, usuario);
            stageActual.close();
            
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        lblError.setText(mensaje);
        lblError.setVisible(true);
    }
}