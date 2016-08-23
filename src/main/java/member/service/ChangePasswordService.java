package member.service;

import jdbc.connection.ConnectionProvider;
import jdbc.jdbcUtil;
import member.dao.MemberDao;
import member.model.Member;

import java.sql.Connection;
import java.sql.SQLException;

public class ChangePasswordService {
    private MemberDao memberDao = new MemberDao();

    public void changePassword(String userId, String curPwd, String newPwd) {
        Connection conn = null;
        try {
            conn = ConnectionProvider.getConnection();
            conn.setAutoCommit(false);

            Member member = memberDao.selectById(conn, userId);
            if(member == null) {
                throw new MemberNotFoundException();
            }

            if(!member.matchPassword(curPwd)) {
                throw new InvalidPasswordException();
            }

            member.changePassword(newPwd);
            memberDao.update(conn, member);
            conn.commit();
        } catch (SQLException e) {
            jdbcUtil.rollback(conn);
            throw new RuntimeException(e);
        } finally {
            jdbcUtil.close(conn);
        }
    }
}
