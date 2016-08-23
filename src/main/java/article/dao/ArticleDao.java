package article.dao;

import article.model.Article;
import article.model.Writer;
import jdbc.jdbcUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ArticleDao {
    public Article insert(Connection conn, Article article) throws SQLException {
        PreparedStatement pstmt = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement("insert into article (writer_id, writer_name, title, regdate, moddate, read_cnt)" +
                                            "values(?,?,?,?,?,0)");
            pstmt.setString(1, article.getWriter().getId());
            pstmt.setString(2, article.getWriter().getName());
            pstmt.setString(3, article.getTitle());
            pstmt.setTimestamp(4, toTimestamp(article.getRegDate()));
            pstmt.setTimestamp(5, toTimestamp(article.getModifiedDate()));
            int insertedCount = pstmt.executeUpdate();
            if(insertedCount >0) {
                stmt = conn.createStatement();
                rs = stmt.executeQuery("select last_insert_id() from article");
                if(rs.next()) {
                    Integer newNum = rs.getInt(1);
                    return new Article(newNum,
                            article.getWriter(),
                            article.getTitle(),
                            article.getRegDate(),
                            article.getModifiedDate(),
                            0);
                }
            }
            return null;
        } finally {
            jdbcUtil.close(rs);
            jdbcUtil.close(stmt);
            jdbcUtil.close(pstmt);
        }
    }

    public List<Article> select(Connection conn, int startRow, int size) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement("select * from article order by article_no desc limit ?, ?")){
            pstmt.setInt(1, startRow);
            pstmt.setInt(2, size);
            try(ResultSet rs = pstmt.executeQuery()) {
                List<Article> result = new ArrayList<>();
                while(rs.next()) {
                    result.add(convertArticle(rs));
                }
                return result;
            }
        }
    }

    private Article convertArticle(ResultSet rs) throws SQLException {
        return new Article(rs.getInt("article_no"),
                new Writer(
                        rs.getString("writer_id"),
                        rs.getString("writer_name")),
                rs.getString("title"),
                toDate(rs.getTimestamp("regdate")),
                toDate(rs.getTimestamp("moddate")),
                rs.getInt("read_cnt")
                );
    }

    public Article selectById(Connection conn, int no) throws SQLException {
        try(PreparedStatement pstmt = conn.prepareStatement("select * from article where article_no = ?")) {
            pstmt.setInt(1, no);
            try(ResultSet rs = pstmt.executeQuery()) {
                Article article = null;
                if(rs.next()) {
                    article = convertArticle(rs);
                }
                return article;
            }
        }
    }

    public void increaseReadCount(Connection conn, int no) throws SQLException {
        try(PreparedStatement pstmt = conn.prepareStatement("update article set read_cnt = read_cnt + 1 where article_no = ?")) {
            pstmt.setInt(1, no);
            pstmt.executeUpdate();
        }
    }

    private Date toDate(Timestamp datetime) {
        return new Date(datetime.getTime());
    }

    private Timestamp toTimestamp(Date date) {
        return new Timestamp(date.getTime());
    }

    public int selectCount(Connection conn) throws SQLException {
        try(Statement stmt = conn.createStatement()) {
            try(ResultSet rs = stmt.executeQuery("select count(*) from article")) {
                if(rs.next()) {
                    return rs.getInt(1);
                }
            }
            return 0;
        }
    }
}
