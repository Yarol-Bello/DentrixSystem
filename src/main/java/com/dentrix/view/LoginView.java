package com.dentrix.view;

import com.dentrix.dao.UsuarioDAO;
import com.dentrix.model.Usuario;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class LoginView {

    public void mostrar(Stage stage) {
        // 1. Crear los componentes visuales
        Label lblTitulo = new Label("Dentrix Dental");
        lblTitulo.setFont(Font.font("System", FontWeight.BOLD, 24));
        lblTitulo.setStyle("-fx-text-fill: #0F766E;"); // Color verde oscuro del prototipo

        Label lblSubtitulo = new Label("Centro Odontológico");
        lblSubtitulo.setStyle("-fx-text-fill: #64748B; -fx-font-size: 12px;");

        TextField txtUsuario = new TextField();
        txtUsuario.setPromptText("Usuario");
        txtUsuario.setMaxWidth(250);

        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Contraseña");
        txtPassword.setMaxWidth(250);

        Button btnLogin = new Button("Iniciar Sesión");
        btnLogin.setStyle("-fx-background-color: #00A896; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        btnLogin.setMaxWidth(250);

        Label lblError = new Label();
        lblError.setStyle("-fx-text-fill: red; -fx-font-size: 11px;");

        // 2. Acción del botón
        btnLogin.setOnAction(e -> {
            String user = txtUsuario.getText();
            String pass = txtPassword.getText();

            if (user.isEmpty() || pass.isEmpty()) {
                lblError.setText("Por favor, llene todos los campos.");
                return;
            }

            UsuarioDAO dao = new UsuarioDAO();
            Usuario usuario = dao.validarLogin(user, pass);

            if (usuario != null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Bienvenido");
                alert.setHeaderText(null);
                alert.setContentText("¡Bienvenido " + usuario.getNombres() + "!\nRol: " + usuario.getRol());
                alert.showAndWait();
                // Aquí luego abriremos el Módulo 2 (Dashboard)
            } else {
                lblError.setText("Usuario o contraseña incorrectos.");
            }
        });

        // 3. Organizar en un contenedor vertical
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #F8FAFC;"); // Color de fondo claro
        root.getChildren().addAll(lblTitulo, lblSubtitulo, txtUsuario, txtPassword, btnLogin, lblError);

        // 4. Crear la escena y mostrarla
        Scene scene = new Scene(root, 400, 500);
        stage.setTitle("Dentrix - Login");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}