<%--
  Created by IntelliJ IDEA.
  User: 12345
  Date: 2025-07-10
  Time: 오후 4:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
    <title>Title</title>
</head>
<body>
헤더 파일<br>

<div id="header">
<c:choose>
    <c:when test="${isAuthenticated != null && isAuthenticated == true}">
        <div style="float:right;">
            <!-- localhost:8080/logout -->
            <a href="${pageContext.request.contextPath}/logout" style="color:blue;margin-right:15px;text-decoration:none;font-size:15px;">로그아웃</a>
        </div>
    </c:when>
    <c:otherwise>
        <div style="float:right">
            <!-- localhost:8080/loginPage -->
            <a href="${pageContext.request.contextPath}/loginPage" style="color:blue;margin-right:15px;text-decoration:none;font-size:15px;">로그인</a>
        </div>
    </c:otherwise>
</c:choose>
</div>

<div id="context">
    <p>${pageContext.request.contextPath }</p>
    <a href="${pageContext.request.contextPath}/bbs/board" style="color:blue;margin-right:15px;text-decoration:none;font-size:15px;">게시판</a>
</div>


</body>
</html>
