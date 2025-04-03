package controller;

import application.Main;
import data.ArticleDataManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Article;

public class LibraryArticleController {
    
    @FXML
    private TableView<Article> tableview;

    @FXML
    private Button btnBack;

    @FXML
    private Button btnMenup;

    @FXML
    private TableColumn<Article, String> colAuthor;

    @FXML
    private TableColumn<Article, String> colISSN;

    @FXML
    private TableColumn<Article, String> colTitle;

    @FXML
    private TableColumn<Article, Integer> colYear;
    
    private ArticleDataManager articleManager = ArticleDataManager.getInstance();
    
    @FXML
    public void initialize() {
        ObservableList<Article> articles = FXCollections.observableArrayList();
        for(Article article : articleManager.getArticles()) {
            if(article.isAvailable()) {
                articles.add(article);
            }
        }
        
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colISSN.setCellValueFactory(new PropertyValueFactory<>("ISSN"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        tableview.setItems(articles);
    }

    @FXML
    void goToBack(ActionEvent event) {
        Main.loadScene("/view/MenuArticle.fxml");
    }

    @FXML
    void goToMenuSelection(ActionEvent event) {  
        Main.loadScene("/view/MenuSelection.fxml"); 
    }
}