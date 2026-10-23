package com.dentrix.view;

import com.dentrix.dao.UsuarioDAO;
import com.dentrix.model.Usuario;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class LoginView {

    public void mostrar(Stage stage) {
        // 1. Contenedor principal (Fondo gris claro)
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #F1F5F9;");

        // 2. Tarjeta blanca de Login
        VBox loginCard = new VBox(15);
        loginCard.getStyleClass().add("login-card");
        loginCard.setAlignment(Pos.CENTER);
        loginCard.setMaxWidth(400);
        loginCard.setMaxHeight(450);

        // Títulos
        Label lblTitulo = new Label("Dentrix");
        lblTitulo.getStyleClass().add("title-text");

        Label lblSubtitulo = new Label("Centro Odontológico");
        lblSubtitulo.getStyleClass().add("subtitle-text");

        VBox headerBox = new VBox(5, lblTitulo, lblSubtitulo);
        headerBox.setAlignment(Pos.CENTER);

        // Campos del formulario
        VBox formBox = new VBox(10);
        formBox.setMaxWidth(300);
        formBox.setAlignment(Pos.CENTER_LEFT);

        Label lblUser = new Label("USUARIO");
        lblUser.getStyleClass().add("label-bold");
        TextField txtUsuario = new TextField();
        txtUsuario.setPromptText("Nombre de usuario");

        Label lblPass = new Label("CONTRASEÑA");
        lblPass.getStyleClass().add("label-bold");
        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Contraseña");

        formBox.getChildren().addAll(lblUser, txtUsuario, lblPass, txtPassword);

        // Mensaje de error (oculto por defecto)
        Label lblError = new Label();
        lblError.getStyleClass().add("error-text");
        lblError.setVisible(false);

        // Botón de Ingreso
        Button btnLogin = new Button("Iniciar Sesión");
        btnLogin.getStyleClass().add("btn-primary");
        btnLogin.setMaxWidth(Double.MAX_VALUE);
        btnLogin.setMaxWidth(300);

        // Lógica del botón
        btnLogin.setOnAction(e -> {
            String user = txtUsuario.getText().trim();
            String pass = txtPassword.getText().trim();

            if (user.isEmpty() || pass.isEmpty()) {
                lblError.setText("Por favor, ingrese sus credenciales.");
                lblError.setVisible(true);
                return;
            }

            UsuarioDAO dao = new UsuarioDAO();
            Usuario usuario = dao.validarLogin(user, pass);

            if (usuario != null) {
                // Éxito: Abrir el Dashboard y cerrar la ventana de Login
                DashboardView dashboard = new DashboardView();
                Stage dashboardStage = new Stage();
                dashboard.mostrar(dashboardStage, usuario);

                // Cerrar la ventana actual de login
                Stage loginStage = (Stage) btnLogin.getScene().getWindow();
                loginStage.close();
            } else {
                lblError.setText("Usuario o contraseña incorrectos.");
                lblError.setVisible(true);
            }
        });

        // Texto de ayuda inferior
        Label lblAyuda = new Label("El sistema redirige automáticamente según tu rol.");
        lblAyuda.getStyleClass().add("subtitle-text");
        lblAyuda.setTextAlignment(TextAlignment.CENTER);

        // Ensamblar la tarjeta
        loginCard.getChildren().addAll(headerBox, formBox, lblError, btnLogin, lblAyuda);
        root.getChildren().add(loginCard);

        // 3. Configurar la escena
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles/login.css").toExternalForm());

        // Título nativo de la ventana
        stage.setTitle("Dentrix Dental - Iniciar Sesión");
        stage.setScene(scene);
        stage.setResizable(false);

        // Centrar la ventana en la pantalla al abrir
        stage.centerOnScreen();

        stage.show();
    }
}
