package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Article {
    private final SimpleStringProperty title;
    private final SimpleStringProperty author;
    private final SimpleStringProperty issn;
    private final SimpleIntegerProperty year;
    private final SimpleBooleanProperty available;

    public Article(String title, String author, String issn, int year, boolean available) {
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.issn = new SimpleStringProperty(issn);
        this.year = new SimpleIntegerProperty(year);
        this.available = new SimpleBooleanProperty(available);
    }

   
    public static boolean validateISSN(String issn) {
        return issn != null && issn.matches("\\d{8}");
    }


    public static boolean validateYear(int year) {
        return year >= -800 && year <= 2025;
    }

    public SimpleStringProperty titleProperty() { return title; }
    public SimpleStringProperty authorProperty() { return author; }
    public SimpleStringProperty ISSNProperty() { return issn; }
    public SimpleIntegerProperty yearProperty() { return year; }
    public SimpleBooleanProperty availableProperty() { return available; }

   
    public String getTitle() { return title.get(); }
    public String getAuthor() { return author.get(); }
    public String getISSN() { return issn.get(); }
    public int getYear() { return year.get(); }
    public boolean isAvailable() { return available.get(); }
}