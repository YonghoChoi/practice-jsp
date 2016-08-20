<%@ page import="java.sql.Connection" %>
<%@ page import="jdbc.connection.ConnectionProvider" %>
<%@ page import="java.sql.SQLException" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>DB 연결 테스트</title>
</head>
<body>
<%
    try (Connection conn = ConnectionProvider.getConnection()){
        out.println("커넥션 성공");
    } catch (SQLException e) {
        out.println("커넥션 연결 실패 : " + e.getMessage());
        application.log("커넥션 연결 실패", e);
    }
%>
</body>
</html>
