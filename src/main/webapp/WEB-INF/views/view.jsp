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
        <input type="hidden" id="userNum" name="userNum" value="\${board.userNum}" readonly>
        <label for="id">회원아이디</label>
        <input type="text" id="id" name="id" maxlength="20" value="\${board.id}" readonly>
        <br>
        <label for="title">제목</label>
        <input type="text" id="title" name="title" maxlength="10" value="\${board.title}" readonly>
        <br>
        <label for="content">내용</label>
        <div id="contentDisplay">

        </div>
        <br>
        <label for="nick">닉네임</label>
        <input type="text" id="nick" name="nick" maxlength="10" value="\${board.nick}" readonly>
        <br>
        <label for="writeDate">작성일</label>
        <input type="text" id="writeDate" name="writeDate" value="\${board.writeDate}" readonly>
        <br>
        <label for="hit">조회수</label>
        <input type="text" id="hit" name="hit" value="\${board.hit}" readonly>
    </div>
</form>
<div id="downloadfile">

</div>
<div id="boardEdit">
<button id="modify">게시물 수정</button>
<button id="delete">삭제</button>
</div>

<div id="comment">
</div>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>

<script type="text/javascript">
    //DOM 객체 연결(html혹은 jsp파일안에 있는 태그들.
    //즉 객체들을 자바스크립트와 연결시키는 과정
    const container = document.getElementById("container");
    const boardItem = document.getElementById("boardItem");
    const downloadfile = document.getElementById("downloadfile");
    const boardNo = window.location.pathname.split('/').pop();

    function fetchDetail() {
        // 폼 요소를 먼저 가져옵니다.
        const menuForm = document.getElementById('menuForm');

        fetch(`/api/v1/boardView/${boardNo}`).then(response => response.json())
            .then(board => { // 백엔드에서 받은 Board 객체가 board로 들어옴.
                // 기존 내용 초기화
                // boardItem.innerHTML = '';
                console.log(board);
                // 백엔드에서 받은 Board 객체
                // // 탈퇴한 회원의
                // if(board.id == null) {
                //     board.id = "탈퇴한 회원입니다";
                // }
                // if(board.nick == null) {
                //     board.nick = "탈퇴한 회원입니다";
                // }
                const memberId = board.id || "탈퇴한 회원입니다.";
                const memberNick = board.nick || "탈퇴한 회원입니다.";

                // 폼 내부에 값 할당
                menuForm.querySelector('#userNum').value = board.userNum;
                menuForm.querySelector('#id').value = memberId;
                menuForm.querySelector('#title').value = board.title;

                // 내용은 div에 innerHTML로 채웁니다.
                menuForm.querySelector('#contentDisplay').innerHTML = board.content;

                menuForm.querySelector('#nick').value = memberNick;
                menuForm.querySelector('#writeDate').value = board.writeDate;
                menuForm.querySelector('#hit').value = board.hit;

                // 모든 input 필드를 읽기 전용으로 설정
                menuForm.querySelectorAll('input').forEach(input => {
                    input.readOnly = true;
                });
                // 생성된 요소를 페이지에 추가
                // boardItem.appendChild(detailElement);
                });

        fetch(`/api/v1/boardFileView/${boardNo}`).then(response => response.json())
            .then(file => { // 백엔드로부터 받은 객체가 file로 들어옴
                //기존 내용 초기화
                downloadfile.innerHTML = '';
                console.log(file);

                // files 배열을 순회하며 각 파일 객체를 처리
                file.forEach(file => {
                    // 게시물 상세 내용을 담을 컨테이너 요소 생성 (div 사용)
                    const fileElement = document.createElement('div');
                    fileElement.className = 'board-detail-item'; // CSS 클래스명 지정
                    const listItem = document.createElement('li');  // <li> 태그 생성

                    const fileLink = document.createElement('a');  // <a> 태그 생성

                    fileLink.href = '/download/' + file.filenameSaved; // <a> 태그의 href 속성에 다운로드 URL 설정
                    fileLink.textContent = file.filenameOri; // 파일 객체 안의 filenameOri에 접근

                    listItem.appendChild(fileLink); // <li> 태그 안에 <a> 태그 추가

                    fileElement.appendChild(listItem); // <div> 태그 안에 <li> 태그 추가

                    // downloadfile 요소에 추가
                    downloadfile.appendChild(fileElement);
                });
            })

        fetch(`/api/v1/comment/view/${boardNo}`).then(response => response.json())
            .then(commentList => {
                console.log(commentList);
                const commentArea = document.getElementById("comment");

                // 테이블 틀 만들기
                commentArea.innerHTML = `
            <table style="border-collapse:collapse; border:1px solid black; width:100%; text-align:center;">
                <thead>
                    <tr>
                        <th>번호</th>
                        <th>유저번호</th>
                        <th>아이디</th>
                        <th>닉네임</th>
                        <th>내용</th>
                        <th>작성일</th>
                    </tr>
                </thead>
                <tbody id="commentBody"></tbody>
            </table>
        `;

                const commentBody = document.getElementById("commentBody");

                // 반복문을 돌며 tbody 안에 한 줄씩 쌓기
                commentList.forEach(comment => {
                    const tr = document.createElement('tr'); // 줄 생성

                    // 생성한 tr 안에 내용을 채우기
                    tr.innerHTML = `
                <td>\${comment.commentNo}</td>
                <td>\${comment.userNum}</td>
                <td>\${comment.id}</td>
                <td>\${comment.nick}</td>
                <td>\${comment.content}</td>
                <td>\${comment.writeDate}</td>
            `;

                    // 완성된 줄을 테이블 몸통(tbody)에 붙인다.
                    commentBody.appendChild(tr);
                });
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
