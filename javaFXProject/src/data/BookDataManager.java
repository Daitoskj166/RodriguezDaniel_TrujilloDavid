package data;

import java.sql.SQLException;
import java.util.ArrayList;
import model.Book;

public class BookDataManager implements CRUD_Operation<Book> {
    private static BookDataManager instance;
    private ArrayList<Book> bookCache = new ArrayList<>();
    private OracleDBConnection dbManager = OracleDBConnection.getInstance();

    private BookDataManager() {}

    public static synchronized BookDataManager getInstance() {
        if (instance == null) {
            instance = new BookDataManager();
        }
        return instance;
    }

    public ArrayList<Book> findAll() {
        if (bookCache.isEmpty()) {
            refreshCache();
        }
        return new ArrayList<>(bookCache);
    }

    public Book findById(Object id) {
        if (!(id instanceof Long)) throw new IllegalArgumentException("ID must be Long");
        long isbn = (Long) id;
        
        return bookCache.stream()
                .filter(book -> book.getISBN() == isbn)
                .findFirst()
                .orElseGet(() -> {
                    try {
                        return dbManager.fetchBookByISBN(isbn);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        return null;
                    }
                });
    }

    public void save(Book book) throws Exception {
        try {
            dbManager.insertBook(book);
            refreshCache();
        } catch (SQLException e) {
            throw new Exception("Error al guardar el libro: " + e.getMessage());
        }
    }

    public void update(Book book) throws Exception {
        try {
            dbManager.updateBook(book);
            refreshCache();
        } catch (SQLException e) {
            throw new Exception("Error al actualizar el libro: " + e.getMessage());
        }
    }

    @Override
    public void delete(Object id) throws Exception {
        if (!(id instanceof Long)) throw new IllegalArgumentException("ID must be Long");
        long isbn = (Long) id;
        
        try {
            dbManager.deleteBook(isbn);
            refreshCache();
        } catch (SQLException e) {
            throw new Exception("Error al eliminar el libro: " + e.getMessage());
        }
    }

    public ArrayList<Book> getAvailableBooks() {
        ArrayList<Book> availableBooks = new ArrayList<>();
        for (Book book : findAll()) {
            if (book.isDisponible()) {
                availableBooks.add(book);
            }
        }
        return availableBooks;
    }
    

    private void refreshCache() {
        try {
            bookCache = dbManager.fetchBooks();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean authenticate(Long isbn) {
        String sql = "SELECT ISBN FROM Book WHERE ISBN = ?";
        try (var conn = OracleDBConnection.getInstance().getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, isbn);
            var rs = stmt.executeQuery();
            return rs.next(); 
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public void softDelete(Long isbn) throws Exception {
        String sql = "UPDATE Book SET is_deleted = 1 WHERE ISBN = ?";
        try (var conn = OracleDBConnection.getInstance().getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, isbn);
            stmt.executeUpdate();
            refreshCache(); 
        } catch (Exception e) {
            throw new Exception("Error al eliminar lógicamente el libro: " + e.getMessage());
        }
    }
}