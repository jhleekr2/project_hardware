<%--
  Created by IntelliJ IDEA.
  User: 12345
  Date: 2025-07-10
  Time: 오후 6:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>로그인 페이지</title>
</head>
<body>

<%@ include file="/WEB-INF/views/common/header.jsp" %>

<div id="login-container-wrapper">
    <div id="login-container">
        <h2>로그인</h2>

        <!-- 로그인 실패시 오류 메시지 표시 -->
        <c:if test="${not empty param.error}">
            <p style="color:red;">아이디 또는 비밀번호가 잘못되었습니다.</p>
        </c:if>
        <!-- localhost:8080/login으로 username, password 넘어감 -->
        <form action="${pageContext.request.contextPath}/login" method="post">
            <!-- 이후 로그인 과정은 스프링 시큐리티 이용해서 개발 진행할 것임 -->
            <!-- CSRF 토큰은 비활성화 -->
<%--            <input type="hidden" name="_csrf" value="${_csrf.token }">--%>
            <div class="input-group">
                <label for="id">아이디</label>
                <input type="text" id="id" name="id" required/>
            </div>
            <div class="input-group">
                <label for="password">비밀번호</label>
                <input type="password" id="password" name="password" required/>
            </div>
            <button type="submit" id="login-button">로그인</button>
            <!-- 백엔드 영역으로 데이터 넘기는 태그 -->
        </form>
        <div id="register-link">
            <!-- localhost:8080/ -->
            <a href="${pageContext.request.contextPath }/registerPage">회원가입</a>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>

</body>
</html>
