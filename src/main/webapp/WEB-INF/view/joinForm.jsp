<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>가입</title>
</head>
<body>
<form action="join.do" method="post" enctype="multipart/form-data">
<%--<form action="join.do" method="post">--%>
    <p>
        아이디 : <br><input type="text" name="id" value="${param.id}">
        <c:if test="${errors.id}">ID를 입력하세요.</c:if>
        <c:if test="${errors.duplicateId}">이미 사용중인 아이디입니다.</c:if>
    </p>
    <p>
        이름 : <br><input type="text" name="name" value="${param.name}">
        <c:if test="${errors.name}">이름을 입력하세요.</c:if>
    </p>
    <p>
        암호 : <br><input type="password" name="password" value="${param.password}">
        <c:if test="${errors.password}">암호를 입력하세요.</c:if>
    </p>
    <p>
        확인 : <br><input type="password" name="confirmPassword" value="${param.confirmPassword}">
        <c:if test="${errors.confirmPassword}">확인을 입력하세요.</c:if>
        <c:if test="${errors.notMatch}">암호와 확인이 일치하지 않습니다.</c:if>
    </p>
    <p>
        <input type="checkbox" name="vehicle" value="Bike" checked> I have a bike<br>
        <input type="checkbox" name="vehicle" value="Car" checked> I have a car<br>
    </p>
    <input type="submit" value="가입">
</form>
</body>
</html>
