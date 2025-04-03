package data;

import java.sql.SQLException;
import java.util.ArrayList;
import model.Article;

public class ArticleDataManager {
    private static ArticleDataManager instance;
    private ArrayList<Article> articleList = new ArrayList<>();
    private static OracleDBConnection dbManager = new OracleDBConnection();

    public static ArticleDataManager getInstance() {
        if (instance == null) {
            instance = new ArticleDataManager();
        }
        return instance;
    }

    public ArrayList<Article> getArticles() {
        if (articleList.isEmpty()) {
            getArticlesFromDatabase();
        }
        return articleList;
    }

    public ArrayList<Article> getAvailableArticles() {
        ArrayList<Article> availableArticles = new ArrayList<>();
        for (Article article : getArticles()) {
            if (article.isAvailable()) {
                availableArticles.add(article);
            }
        }
        return availableArticles;
    }

    private void getArticlesFromDatabase() {
        try {
            ArrayList<Article> articlesFromDb = dbManager.fetchArticles();
            articleList.clear();
            articleList.addAll(articlesFromDb);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addArticle(Article article) throws Exception {

        try {
            articleList.add(article);
            dbManager.insertArticle(article);
            System.out.println("Registro exitoso");
        } catch (SQLException e) {
            throw new Exception("Error al guardar en la base de datos");
        }
    }
}