package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Article;
import model.Book;
import model.User;

public class OracleDBConnection {
	private final String username="ajerez";
	private final String password="4j3r3z";
	private final String host = "192.168.254.215";
	private final String port = "1521";
	private final String service = "orcl";


    
    // Method to fetch books from the database
    public ArrayList<Book> fetchBooks() throws SQLException {
        ArrayList<Book> books = new ArrayList<>();
        String query = "SELECT Title, Author, ISBN, Year, Available FROM Book";
        
        try (Connection conn = DriverManager.getConnection(getConnectionString(), username, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                String title = rs.getString("Title");
                String author = rs.getString("Author");
                long isbn = rs.getLong("ISBN");
                int year = rs.getInt("Year");
                boolean available = rs.getBoolean("Available");
                
                Book book = new Book(title, author, isbn, year, available);
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error fetching books from the database.");
        }

        return books;
    }
    
    
    // Method to fetch users from the database
    public ArrayList<User> fetchUsers() throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        String query = "SELECT nickname, password FROM UserAdmin";       
        try (Connection conn = DriverManager.getConnection(getConnectionString(), username, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                String userName = rs.getString("nickname");
                String password = rs.getString("password");
                
                User user = new User(userName, password);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error fetching users from the database.");
        }

        return users;
    }
    
    // Method to insert a new book into the database
    public void insertBook(Book book) throws SQLException {
        String query = "INSERT INTO Book (Title, Author, ISBN, Year, Available) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(getConnectionString(), username, password);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
        	

            pstmt.setString(1, book.getTitulo());
            pstmt.setString(2, book.getAutor());
            pstmt.setLong(3, book.getISBN());
            pstmt.setInt(4, book.getAño());
            pstmt.setBoolean(5, book.isDisponible());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Book inserted successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error inserting book into the database.");
        }
    }
    
 
    public ArrayList<Article> fetchArticles() throws SQLException {
        ArrayList<Article> articles = new ArrayList<>();
        String query = "SELECT Title, Author, ISSN, Year, Available FROM Article";
        
        try (Connection conn = DriverManager.getConnection(getConnectionString(), username, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                String title = rs.getString("Title");
                String author = rs.getString("Author");
                String issn = rs.getString("ISSN");
                int year = rs.getInt("Year");
                boolean available = rs.getBoolean("Available");
                
                articles.add(new Article(title, author, issn, year, available));
            }
        }
        return articles;
    }

   
    public void insertArticle(Article article) throws SQLException {
        String query = "INSERT INTO Article (Title, Author, ISSN, Year, Available) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(getConnectionString(), username, password);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, article.getTitle());
            pstmt.setString(2, article.getAuthor());
            pstmt.setString(3, article.getISSN());
            pstmt.setInt(4, article.getYear());
            pstmt.setBoolean(5, article.isAvailable());

            pstmt.executeUpdate();
        }
    }
    
    
 
    public void insertUser(User user) throws SQLException {
        String query = "INSERT INTO UserAdmin (Nickname, Password) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(getConnectionString(), username, password);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, user.getNombreUsuario());
            pstmt.setString(2, user.getContraseña());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User inserted successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error inserting user into the database.");
        }
    }
    
	public String getConnectionString() {
		return String.format("jdbc:oracle:thin:@%s:%s:%s", this.host, this.port, this.service);
	}
	
	
}


