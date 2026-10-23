package com.dentrix.view;

import com.dentrix.controller.LoginController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginView {
    public void mostrar(Stage stage) {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #F1F5F9;"); 

        VBox loginCard = new VBox(15);
        loginCard.getStyleClass().add("login-card");
        loginCard.setAlignment(Pos.CENTER);
        loginCard.setMaxWidth(400);

        Label lblTitulo = new Label("Dentrix");
        lblTitulo.getStyleClass().add("title-text");
        Label lblSubtitulo = new Label("Centro Odontológico");
        lblSubtitulo.getStyleClass().add("subtitle-text");
        VBox headerBox = new VBox(5, lblTitulo, lblSubtitulo);
        headerBox.setAlignment(Pos.CENTER);

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

        Label lblError = new Label();
        lblError.getStyleClass().add("error-text");
        lblError.setVisible(false);

        Button btnLogin = new Button("Iniciar Sesión");
        btnLogin.getStyleClass().add("btn-primary");
        btnLogin.setMaxWidth(Double.MAX_VALUE);

        // Instanciar el controlador y pasarle los elementos
        LoginController controller = new LoginController(txtUsuario, txtPassword, lblError);
        btnLogin.setOnAction(e -> controller.procesarLogin(stage));

        loginCard.getChildren().addAll(headerBox, formBox, lblError, btnLogin);
        root.getChildren().add(loginCard);

        Scene scene = new Scene(root, 800, 600);
        // Asegúrate de que la ruta coincida con tu estructura de NetBeans
        scene.getStylesheets().add(getClass().getResource("/styles/login.css").toExternalForm());
        stage.setTitle("Dentrix Dental - Iniciar Sesión");
        stage.setScene(scene);
        stage.show();
    }
}