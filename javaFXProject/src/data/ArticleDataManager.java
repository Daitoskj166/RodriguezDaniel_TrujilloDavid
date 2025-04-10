package data;

import java.sql.SQLException;
import java.util.ArrayList;
import model.Article;

public class ArticleDataManager implements CRUD_Operation<Article> {
    private static ArticleDataManager instance;
    private ArrayList<Article> articleCache = new ArrayList<>();
    private OracleDBConnection dbManager = OracleDBConnection.getInstance();

    private ArticleDataManager() {}

    public static synchronized ArticleDataManager getInstance() {
        if (instance == null) {
            instance = new ArticleDataManager();
        }
        return instance;
    }

    @Override
    public ArrayList<Article> findAll() {
        if (articleCache.isEmpty()) {
            refreshCache();
        }
        return new ArrayList<>(articleCache);
    }

    @Override
    public Article findById(Object id) {
        if (!(id instanceof String)) throw new IllegalArgumentException("ID must be String (ISSN)");
        String issn = (String) id;

        return articleCache.stream()
                .filter(article -> article.getISSN().equals(issn))
                .findFirst()
                .orElseGet(() -> {
                    try {
                        return dbManager.fetchArticleByISSN(issn);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        return null;
                    }
                });
    }

    @Override
    public void save(Article article) throws Exception {
        try {
            dbManager.insertArticle(article);
            refreshCache();
        } catch (SQLException e) {
            throw new Exception("Error al guardar el artículo: " + e.getMessage());
        }
    }

    @Override
    public void update(Article article) throws Exception {
        try {
            dbManager.updateArticle(article);
            refreshCache();
        } catch (SQLException e) {
            throw new Exception("Error al actualizar el artículo: " + e.getMessage());
        }
    }

    @Override
    public void delete(Object id) throws Exception {
        if (!(id instanceof String)) throw new IllegalArgumentException("ID must be String (ISSN)");
        String issn = (String) id;

        try {
            dbManager.deleteArticle(issn);
            refreshCache();
        } catch (SQLException e) {
            throw new Exception("Error al eliminar el artículo: " + e.getMessage());
        }
    }

    public ArrayList<Article> getAvailableArticles() {
        ArrayList<Article> availableArticles = new ArrayList<>();
        for (Article article : findAll()) {
            if (article.isAvailable()) {
                availableArticles.add(article);
            }
        }
        return availableArticles;
    }

    private void refreshCache() {
        try {
            articleCache = dbManager.fetchArticles();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean authenticate(String issn) {
        String sql = "SELECT ISSN FROM Article WHERE ISSN = ?";
        try (var conn = dbManager.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, issn);
            var rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void softDelete(String issn) throws Exception {
        String sql = "UPDATE Article SET is_deleted = 1 WHERE ISSN = ?";
        try (var conn = dbManager.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, issn);
            stmt.executeUpdate();
            refreshCache();
        } catch (Exception e) {
            throw new Exception("Error al eliminar lógicamente el artículo: " + e.getMessage());
        }
    }
}
