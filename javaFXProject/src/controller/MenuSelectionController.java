package controller;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MenuSelectionController {

    @FXML
    void goToArticles(ActionEvent event) {
        Main.loadScene("/view/MenuArticle.fxml");
    }

    @FXML
    void goToBooks(ActionEvent event) {
        Main.loadScene("/view/MenuBook.fxml");
    }

    @FXML
    void goToLogin(ActionEvent event) {
        Main.loadScene("/view/InicioSesion.fxml");
    }
}