package jdbc.connection;

import java.sql.*;

public class ConnectionWrap implements AutoCloseable {
    Connection conn;
    boolean dirty;

    public ConnectionWrap(Connection conn) {
        this.conn = conn;
        this.dirty = false;
    }

    @Override
    public void close() throws SQLException {
        if(isRollback()) {
            rollback();
        }

        conn.close();
    }

    public void commit() throws SQLException {
        conn.commit();
        this.dirty = false;
    }

    public ResultSet executeQuery(PreparedStatement pstmt) throws SQLException {
        this.dirty = true;
        return pstmt.executeQuery();
    }

    public int executeUpdate(PreparedStatement pstmt) throws SQLException {
        this.dirty = true;
        return pstmt.executeUpdate();
    }

    public ResultSet executeQuery(Statement stmt, String query) throws SQLException {
        this.dirty = true;
        return stmt.executeQuery(query);
    }

    public int executeUpdate(Statement stmt, String query) throws SQLException {
        this.dirty = true;
        return stmt.executeUpdate(query);
    }

    private void rollback() throws SQLException {
        conn.rollback();
    }

    private boolean isRollback() {
        try {
            if(!conn.getAutoCommit() && this.dirty) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
