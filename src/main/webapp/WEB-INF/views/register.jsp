<%--
  Created by IntelliJ IDEA.
  User: 12345
  Date: 2025-07-10
  Time: 오후 3:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<%@ include file="/WEB-INF/views/common/header.jsp" %>

회원가입<br>
<div id="register-container-wrapper">
    <div class="register-container">
        <h2>회원가입</h2>
        <form action="${pageContext.request.contextPath}/register" method="post">
            <!-- CSRF 토큰 생략 -->
<%--            <input type="hidden" name="_csrf" value="${_csrf.token }">--%>
            <div class="input-group">
                <label for="id">아이디</label>
                <input type="text" id="id" name="id" required>
            </div>
            <div class="input-group">
                <label for="password">비밀번호</label>
                <input type="password" id="password" name="password" required>
            </div>
            <div class="input-group">
                <label for="nick">닉네임</label>
                <input type="text" id="nick" name="nick" required>
            </div>
            <div class="input-group">
                <label for="email">이메일</label>
                <input type="text" id="email" name="email" required>
            </div>
            <div class="input-group">
                <button type="submit" class="register-button">회원가입</button>
            </div>
            <div class="login-link">
                <a href="${pageContext.request.contextPath}/loginPage">이미 계정이 있으신가요?</a>
            </div>
        </form>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>

</body>
</html>
