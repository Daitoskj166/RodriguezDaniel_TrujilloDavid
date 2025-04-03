package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import model.Article;
import application.Main;
import data.ArticleDataManager;

public class MenuArticleController {

    @FXML
    private TextField txtTitle;

    @FXML
    private TextField txtAuthor;

    @FXML
    private TextField txtYear;

    @FXML
    private CheckBox chkAvailability;

    @FXML
    private TextField txtISSN;

    @FXML
    private Button btnRegister;

    @FXML
    private Button btnShowArticles;

    @FXML
    private Button btnMenup;

    private ArticleDataManager articleManager = ArticleDataManager.getInstance();
    

    @FXML
    void registerArticle(ActionEvent event) {
        String titulo = txtTitle.getText().trim();
        String autor = txtAuthor.getText().trim();
        String añoStr = txtYear.getText().trim();
        String issnStr = txtISSN.getText().trim();
        boolean disponible = chkAvailability.isSelected();

        if (titulo.isEmpty() || autor.isEmpty() || añoStr.isEmpty() || issnStr.isEmpty()) {
            mostrarAlerta("Error", "Campos vacíos", "Por favor, complete todos los campos.");
            return;
        }

        try {
            int año = Integer.parseInt(añoStr);

            if (!Article.validateISSN(issnStr)) {
                mostrarAlerta("Error", "ISSN inválido", "El ISSN debe tener exactamente 8 dígitos.");
                return;
            }

            if (!Article.validateYear(año)) {
                mostrarAlerta("Error", "Año inválido", "El año debe estar entre 800 a. C. y 2025 d. C.");
                return;
            }

            for (Article article : articleManager.getArticles()) {
                if (article.getISSN().equals(issnStr)) {
                    mostrarAlerta("Error", "ISSN repetido", "El ISSN ya está registrado.");
                    return;
                }
            }

            Article article = new Article(titulo, autor, issnStr, año, disponible);
            articleManager.addArticle(article);
            mostrarAlerta("Éxito", "Artículo registrado", "El artículo se ha registrado correctamente.");
            limpiarCampos();
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Formato inválido", "El año debe ser un número válido.");
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al registrar", e.getMessage());
        }
    }

    @FXML
    void showAvailableArticles(ActionEvent event) {
        Main.loadScene("/view/LibraryArticle.fxml");
    }

    @FXML
    void goToMenuSelection(ActionEvent event) { 
        Main.loadScene("/view/MenuSelection.fxml");  
    }

    private void mostrarAlerta(String titulo, String cabecera, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void limpiarCampos() {
        txtTitle.clear();
        txtAuthor.clear();
        txtYear.clear();
        txtISSN.clear();
        chkAvailability.setSelected(false);
    }
}