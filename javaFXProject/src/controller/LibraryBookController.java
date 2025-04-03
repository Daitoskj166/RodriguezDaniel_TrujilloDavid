package controller;

import application.Main;
import data.BookDataManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Book;

public class LibraryBookController {
    
    @FXML
    private TableView<Book> tableview;

    @FXML
    private Button btnBack;

    @FXML
    private Button btnMenup;

    @FXML
    private TableColumn<Book, String> colAuthor;

    @FXML
    private TableColumn<Book, Long> colISBN;

    @FXML
    private TableColumn<Book, String> colTittle;

    @FXML
    private TableColumn<Book, Integer> colYear;
    
    private BookDataManager bookManager = BookDataManager.getInstance();
    
    @FXML
    public void initialize() {
        ObservableList<Book> books = FXCollections.observableArrayList();
        for(Book book:bookManager.getBooks()) {
            if(book.isDisponible()) {
                books.add(book);
            }
        }
        
        colTittle.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colISBN.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("año"));
        tableview.setItems(books);
    }

    @FXML
    void goToBack(ActionEvent event) {
        Main.loadScene("/view/MenuBook.fxml");
    }

    @FXML
    void goToMenuSelection(ActionEvent event) { 
        Main.loadScene("/view/MenuSelection.fxml"); 
    }
}
