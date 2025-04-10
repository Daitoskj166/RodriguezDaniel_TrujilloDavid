package data;

import java.sql.*;
import java.util.ArrayList;
import model.Article;
import model.Book;
import model.User;

public class OracleDBConnection {
    private static OracleDBConnection instance;
    private final String username = "ajerez";
    private final String password = "4j3r3z";
    private final String host = "192.168.254.215";
    private final String port = "1521";
    private final String service = "orcl";

    OracleDBConnection() {}

    public static synchronized OracleDBConnection getInstance() {
        if (instance == null) {
            instance = new OracleDBConnection();
        }
        return instance;
    }

    Connection getConnection() throws SQLException {
        return DriverManager.getConnection(getConnectionString(), username, password);
    }

    private String getConnectionString() {
        return String.format("jdbc:oracle:thin:@%s:%s:%s", host, port, service);
    }

    // Métodos para Libros
    public ArrayList<Book> fetchBooks() throws SQLException {
        ArrayList<Book> books = new ArrayList<>();
        String query = "SELECT Title, Author, ISBN, Year, Available FROM Book";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        }
        return books;
    }

    public Book fetchBookByISBN(long isbn) throws SQLException {
        String query = "SELECT Title, Author, ISBN, Year, Available FROM Book WHERE ISBN = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setLong(1, isbn);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBook(rs);
                }
            }
        }
        return null;
    }

    public void insertBook(Book book) throws SQLException {
        String query = "INSERT INTO Book (Title, Author, ISBN, Year, Available) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            setBookParameters(pstmt, book);
            pstmt.executeUpdate();
        }
    }

    public void updateBook(Book book) throws SQLException {
        String query = "UPDATE Book SET Title = ?, Author = ?, Year = ?, Available = ? WHERE ISBN = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, book.getTitulo());
            pstmt.setString(2, book.getAutor());
            pstmt.setInt(3, book.getAño());
            pstmt.setBoolean(4, book.isDisponible());
            pstmt.setLong(5, book.getISBN());
            
            pstmt.executeUpdate();
        }
    }

    public void deleteBook(long isbn) throws SQLException {
        String query = "DELETE FROM Book WHERE ISBN = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setLong(1, isbn);
            pstmt.executeUpdate();
        }
    }

    // Métodos para Artículos
    public ArrayList<Article> fetchArticles() throws SQLException {
        ArrayList<Article> articles = new ArrayList<>();
        String query = "SELECT Title, Author, ISSN, Year, Available FROM Article";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                articles.add(mapResultSetToArticle(rs));
            }
        }
        return articles;
    }

    public Article fetchArticleByISSN(String issn) throws SQLException {
        String query = "SELECT Title, Author, ISSN, Year, Available FROM Article WHERE ISSN = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, issn);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToArticle(rs);
                }
            }
        }
        return null;
    }

    public void insertArticle(Article article) throws SQLException {
        String query = "INSERT INTO Article (Title, Author, ISSN, Year, Available) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            setArticleParameters(pstmt, article);
            pstmt.executeUpdate();
        }
    }

    public void updateArticle(Article article) throws SQLException {
        String query = "UPDATE Article SET Title = ?, Author = ?, Year = ?, Available = ? WHERE ISSN = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, article.getTitle());
            pstmt.setString(2, article.getAuthor());
            pstmt.setInt(3, article.getYear());
            pstmt.setBoolean(4, article.isAvailable());
            pstmt.setString(5, article.getISSN());
            
            pstmt.executeUpdate();
        }
    }

    public void deleteArticle(String issn) throws SQLException {
        String query = "DELETE FROM Article WHERE ISSN = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, issn);
            pstmt.executeUpdate();
        }
    }

    // Métodos para Usuarios
    public ArrayList<User> fetchUsers() throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        String query = "SELECT nickname, password FROM UserAdmin";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                users.add(new User(
                    rs.getString("nickname"),
                    rs.getString("password")
                ));
            }
        }
        return users;
    }

    public void insertUser(User user) throws SQLException {
        String query = "INSERT INTO UserAdmin (Nickname, Password) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, user.getNombreUsuario());
            pstmt.setString(2, user.getContraseña());
            pstmt.executeUpdate();
        }
    }

    // Métodos auxiliares
    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        return new Book(
            rs.getString("Title"),
            rs.getString("Author"),
            rs.getLong("ISBN"),
            rs.getInt("Year"),
            rs.getBoolean("Available")
        );
    }

    private Article mapResultSetToArticle(ResultSet rs) throws SQLException {
        return new Article(
            rs.getString("Title"),
            rs.getString("Author"),
            rs.getString("ISSN"),
            rs.getInt("Year"),
            rs.getBoolean("Available")
        );
    }

    private void setBookParameters(PreparedStatement pstmt, Book book) throws SQLException {
        pstmt.setString(1, book.getTitulo());
        pstmt.setString(2, book.getAutor());
        pstmt.setLong(3, book.getISBN());
        pstmt.setInt(4, book.getAño());
        pstmt.setBoolean(5, book.isDisponible());
    }

    private void setArticleParameters(PreparedStatement pstmt, Article article) throws SQLException {
        pstmt.setString(1, article.getTitle());
        pstmt.setString(2, article.getAuthor());
        pstmt.setString(3, article.getISSN());
        pstmt.setInt(4, article.getYear());
        pstmt.setBoolean(5, article.isAvailable());
    }
}