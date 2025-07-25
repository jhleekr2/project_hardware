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
    <title>게시글 수정</title>
    <!-- Editor's Style -->
    <link rel="stylesheet" href="https://uicdn.toast.com/editor/latest/toastui-editor.min.css" />

</head>
<body>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

글쓴이 : ${writer}
<form id="menuForm">
    <h2 id="menuAdminH2">게시물 수정</h2>
    <div id="boardItem">

    </div>
</form>

<div id="boardEdit">
<button id="modify" onclick="location.href='/api/v1/bbs/update/${boardNo}'">수정</button>
<button id="delete" onclick="location.href=`/api/v1/delete/${boardNo}`">삭제</button>
</div>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>

<!-- TUI 에디터 JS CDN -->
<script src="https://uicdn.toast.com/editor/latest/toastui-editor-all.min.js"></script>

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
                <input type="text" id="title" name="title" maxlength="50" value="\${board.title}">
                <br>
                <label for="content">내용</label>
                <!-- 에디터를 적용할 요소 (컨테이너) -->
                <div id="content">

                </div>
                <br>
                <label for="nick">닉네임</label>
                <input type="text" id="nick" name="nick" maxlength="10" value="\${board.nick}" readonly>
                <br>
                <label for="writeDate">작성일</label>
                <input type="text" id="writeDate" name="writeDate" value="\${board.writeDate}" readonly>
                <br>
                <label for="hit">조회수</label>
                <br>
                <button type="button" id="buttonSubmit">수정된 게시물 적용</button>
            `;
                // 생성된 요소를 페이지에 추가
                boardItem.appendChild(detailElement);

                const editor = new toastui.Editor({
                    el: document.querySelector('#content'), // 에디터를 적용할 요소 (컨테이너)
                    height: '500px',                        // 에디터 영역의 높이 값 (OOOpx || auto)
                    initialEditType: 'wysiwyg',            // 최초로 보여줄 에디터 타입 (markdown || wysiwyg)
                    initialValue: board.content,      // 초기값은 기존 게시물의 내용
                    previewStyle: 'vertical',               // 마크다운 프리뷰 스타일 (tab || vertical)
                    placeholder: '내용을 입력해 주세요.',
                    /* start of hooks */
                    hooks: {
                        async addImageBlobHook(blob, callback) { // 이미지 업로드 로직 커스텀
                            try {
                                /*
                                 * 1. 에디터에 업로드한 이미지를 FormData 객체에 저장
                                 *    (이때, 컨트롤러 uploadImg 메서드의 파라미터인 'image'와 formData에 append 하는 key('image')값은 동일해야 함)
                                 */
                                const formData = new FormData();
                                formData.append('image', blob);

                                // 2. BoardRestController - uploadImg 메서드 호출
                                const response = await fetch('/api/v1/uploadimg', {
                                    method : 'POST',
                                    body : formData,
                                });

                                // 3. 컨트롤러에서 전달받은 디스크에 저장된 파일명
                                const filename = await response.text();
                                console.log('서버에 저장된 파일명 : ', filename);

                                // 4. addImageBlobHook의 callback 함수를 통해, 디스크에 저장된 이미지를 에디터에 렌더링
                                const imageUrl = `/img/\${filename}`;
                                callback(imageUrl, 'image alt attribute');

                            } catch (error) {
                                console.error('업로드 실패 : ', error);
                            }
                        }
                    }
                    /* end of hooks */

                });

                //게시물 수정 비동기 처리
                document.getElementById("buttonSubmit").addEventListener("click", function() {
                    const editorContent = editor.getHTML(); // HTML 형식으로 가져옴

                    const formData = {
                        title:document.getElementById("title").value,
                        content:editorContent
                    }

                    fetch(`/api/v1/modify/${boardNo}`,{
                        method:"PUT",
                        headers:{
                            'Content-Type':'application/json',
                        },
                        body: JSON.stringify(formData)
                    }).then(response =>{
                        if(!response.ok){
                            throw new Error("게시물 수정 실패.")
                        }
                        return response.text(); //백단에서 return 한 게시글 잘 작성됐다는 메시지 받음
                    }).then(_=> {
                        console.log("Success");
                        window.location.href="/bbs/board";
                        //localhost:8080/bbs/board 로 페이지가 이동
                    }).catch(error=>{
                        console.log("Error가 발생",error);
                    });
                });
                });
    }
    //백엔드단에서 프론트단 데이터 가져온다

    //데이터를 가져온 후 게시글 작성자와 현재 로그인된 사용자를 비교하여

    //메인페이지가 열리면 자동실행됨
    window.addEventListener('load',fetchDetail);

</script>

</body>
</html>
