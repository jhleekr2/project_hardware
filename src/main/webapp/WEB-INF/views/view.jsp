<%--
  Created by IntelliJ IDEA.
  User: 12345
  Date: 2025-07-12
  Time: 오전 4:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>조회</title>
</head>
<body>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

글쓴이 : ${writer}
<form id="menuForm">
    <h2 id="menuAdminH2">게시물 조회</h2>
    <div id="boardItem">

    </div>
</form>

<div id="boardEdit">
<button id="modify">게시물 수정</button>
<button id="delete">삭제</button>
</div>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>

<script type="text/javascript">
    //DOM 객체 연결(html혹은 jsp파일안에 있는 태그들.
    //즉 객체들을 자바스크립트와 연결시키는 과정
    const container = document.getElementById("container");
    const boardItem = document.getElementById("boardItem");
    const boardNo = window.location.pathname.split('/').pop();

    function fetchDetail() {
        fetch(`/api/v1/boardView/${boardNo}`).then(response => response.json())
            .then(board => { // 백엔드에서 받은 Board 객체가 board로 들어옴.
                // 기존 내용 초기화
                boardItem.innerHTML = '';
                console.log(board);
                // 백엔드에서 받은 Board 객체
                // 탈퇴한 회원의
                if(board.id == null) {
                    board.id = "탈퇴한 회원입니다";
                }
                if(board.nick == null) {
                    board.nick = "탈퇴한 회원입니다";
                }

                // 게시물 상세 내용을 담을 컨테이너 요소 생성 (div 사용)
                const detailElement = document.createElement('div');
                detailElement.className = 'board-detail-item'; // CSS 클래스명 지정

                // HTML 내용을 innerHTML로 설정
                detailElement.innerHTML = `
                <input type="hidden" id="userNum" name="userNum" value="\${board.userNum}" readonly>
                <label for="id">회원아이디</label>
                <input type="text" id="id" name="id" maxlength="20" value="\${board.id}" readonly>
                <br>
                <label for="title">제목</label>
                <input type="text" id="title" name="title" maxlength="10" value="\${board.title}" readonly>
                <br>
                <label for="content">내용</label>`
                    + board.content + `
                <br>
                <label for="nick">닉네임</label>
                <input type="text" id="nick" name="nick" maxlength="10" value="\${board.nick}" readonly>
                <br>
                <label for="writeDate">작성일</label>
                <input type="text" id="writeDate" name="writeDate" value="\${board.writeDate}" readonly>
                <br>
                <label for="hit">조회수</label>
                <input type="text" id="hit" name="hit" value="\${board.hit}" readonly>
            `;
                // 생성된 요소를 페이지에 추가
                boardItem.appendChild(detailElement);
                });
    }
    //백엔드단에서 프론트단 데이터 가져온다

    //데이터를 가져온 후 게시글 작성자와 현재 로그인된 사용자를 비교하여

    //메인페이지가 열리면 자동실행됨
    window.addEventListener('load',fetchDetail);

    document.getElementById('modify').addEventListener('click', function() {
        location.href = `/bbs/update/${boardNo}`;
    });

    document.getElementById('delete').addEventListener('click', function() {

        fetch(`/api/v1/delete/${boardNo}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(errorMsg => {
                        throw new Error(errorMsg || "서버가 응답하지 않습니다.");
                    });
                }
                // 서버에서 ok 메시지 보냈을때
                return response.text();
            })
            .then(successMsg => {
                alert(`삭제 완료!`);
                window.location.href = '/bbs/board';
            })
            .catch(error => {
                //error객체에 백엔드로부터 온 에러 메시지가 들어있는데 error를 떼려면 error.message쓰면 된다.
                alert(error.message);
            });
    });
</script>

</body>
</html>
