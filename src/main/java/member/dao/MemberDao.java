package member.dao;

import jdbc.jdbcUtil;
import member.model.Member;

import java.sql.*;

public class MemberDao {
    public Member selectById(Connection conn, String id) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement("select * from member where memberid = ?");
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();

            Member member = null;
            if(rs.next()) {
                member = new Member(
                        rs.getString("memberid"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getTimestamp("regdate")

                );
            }
            return member;
        } finally {
            jdbcUtil.close(rs);
            jdbcUtil.close(pstmt);
        }
    }

    private Date toDate(Timestamp date) {
        return date == null ? null : new Date(date.getTime());
    }

    public void insert(Connection conn, Member mem) throws SQLException {
        try(PreparedStatement pstmt
                    = conn.prepareStatement("insert into member values(?,?,?,?)")) {
            pstmt.setString(1, mem.getId());
            pstmt.setString(2, mem.getName());
            pstmt.setString(3, mem.getPassword());
            pstmt.setTimestamp(4, new Timestamp(mem.getRegDate().getTime()));
            pstmt.executeUpdate();
        }
    }
}