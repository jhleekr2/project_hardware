<%--
  Created by IntelliJ IDEA.
  User: 12345
  Date: 2025-07-11
  Time: 오후 5:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>게시판</title>
</head>

<body>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<div id="container">
    <table>
        <thead>
        <tr>
            <th></th>
            <th>글번호</th>
            <th>사용자번호</th>
            <th>작성일</th>
            <th>제목</th>
            <th>조회수</th>
        </tr>
        </thead>
        <tbody id="boardList">
<%--        <c:forEach var="boardList" items="${list }">--%>
<%--        <tr>--%>
<%--            <th></th>--%>
<%--            <th>${boardList.boardNo}</th>--%>
<%--            <th>${boardList.userNum}</th>--%>
<%--            <th>${boardList.writeDate}</th>--%>
<%--            <th>${boardList.title}</th>--%>
<%--            <th>${boardList.hit}</th>--%>
<%--        </tr>--%>
<%--        </c:forEach>--%>
        </tbody>
    </table>

</div>
<c:if test="${isAuthenticated != null && isAuthenticated == true}">
<a href="/bbs/insert">글쓰기</a><br>
</c:if>
<script src="${pageContext.request.contextPath }/resources/js/board.js"></script>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>

</body>
</html>
