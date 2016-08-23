package article.service;

import article.dao.ArticleContentDao;
import article.dao.ArticleDao;
import article.model.Article;
import article.model.ArticleContent;
import jdbc.connection.ConnectionProvider;
import jdbc.jdbcUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

public class WriteArticleService {
    private ArticleDao articleDao = new ArticleDao();
    private ArticleContentDao contentDao = new ArticleContentDao();

    public Integer write(WriteRequest req) throws SQLException {
        Connection conn = null;
        try {
            conn = ConnectionProvider.getConnection();
            conn.setAutoCommit(false);

            Article article = toArticle(req);
            Article savedArticle = articleDao.insert(conn, article);
            if(savedArticle == null) {
                throw new RuntimeException("fail to insert article");
            }
            ArticleContent content = new ArticleContent(savedArticle.getNumber(), req.getContent());
            ArticleContent savedContent = contentDao.insert(conn, content);
            if(savedContent == null) {
                throw new RuntimeException("fail to insert article_content");
            }

            conn.commit();
            return savedArticle.getNumber();
        } catch(RuntimeException e) {
            jdbcUtil.rollback(conn);
            throw e;
        } finally {
            jdbcUtil.close(conn);
        }
    }

    private Article toArticle(WriteRequest req) {
        Date now = new Date();
        return new Article(null, req.getWriter(), req.getTitle(), now, now, 0);
    }
}
