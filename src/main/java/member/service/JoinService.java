package member.service;

import jdbc.connection.ConnectionProvider;
import jdbc.jdbcUtil;
import member.dao.MemberDao;
import member.model.Member;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

public class JoinService {
    private MemberDao memberDao = new MemberDao();

    public void join(JoinRequest joinReq) {
        Connection conn = null;
        try {
            conn = ConnectionProvider.getConnection();
            conn.setAutoCommit(false);

            Member member = memberDao.selectById(conn, joinReq.getId());
            if(member != null) {
                jdbcUtil.rollback(conn);
                throw new DuplicateIdException();
            }

            memberDao.insert(conn, new Member(
                    joinReq.getId(),
                    joinReq.getName(),
                    joinReq.getPassword(),
                    new Date()
            ));
            conn.commit();
        } catch (SQLException e) {
            jdbcUtil.rollback(conn);
            throw new RuntimeException(e);
        } finally {
            jdbcUtil.close(conn);
        }
    }
}
