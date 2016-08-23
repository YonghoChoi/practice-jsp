<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="u" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>회원제 게시판 예제</title>
</head>
<body>
<u:isLogin>
    ${authUser.name}님, 안녕하세요.
    <a href="logout.do">[로그아웃하기]</a>
    <a href="changePwd.do">[암호변경하기]</a>
</u:isLogin>
<u:notLogin>
    <a href="join.do">[회원가입하기]</a>
    <a href="login.do">[로그인하기]</a>
</u:notLogin>
</body>
</html>