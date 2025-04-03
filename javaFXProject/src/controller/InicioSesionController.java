package controller;

import application.Main;
import data.UserDataManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
//import model.User;
import model.User;

public class InicioSesionController {

    @FXML
    private Button btnIngreso;

    @FXML
    private Button btnMenuP;

    @FXML
    private PasswordField txtContraseña;

    @FXML
    private TextField txtNombreUsuario;

    
    private UserDataManager userManager = UserDataManager.getInstance();
    
    @FXML
    public void initialize() {
    	userManager.getUsers();
    }
    
    @FXML
    void IngresoUsuario(ActionEvent event) {
        String nombreUsuario = txtNombreUsuario.getText().trim();
        String contraseña = txtContraseña.getText().trim();

        if (nombreUsuario.isEmpty() || contraseña.isEmpty()) {
            mostrarAlerta("Error", "Campos vacíos", "Por favor, complete todos los campos.");
            return;
        }

        
        boolean credencialesValidas = false;
        for (User usuario : userManager.getUsers()) {
            if (usuario.validarCredenciales(nombreUsuario, contraseña)) {
                credencialesValidas = true;
                break;
            }
        }

        if (credencialesValidas) {
            Main.loadScene("/view/MenuSelection.fxml"); 
        } else {
            mostrarAlerta("Error", "Credenciales incorrectas", "El nombre de usuario o la contraseña son incorrectos.");
        }
    }

    @FXML
    void goToMainMenuPrincipal(ActionEvent event) {
        Main.loadScene("/view/MenuPrincipal.fxml");
    }

    private void mostrarAlerta(String titulo, String cabecera, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}