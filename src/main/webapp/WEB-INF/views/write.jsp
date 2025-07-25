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
    <title>글쓰기</title>
    <!-- Editor's Style -->
    <link rel="stylesheet" href="https://uicdn.toast.com/editor/latest/toastui-editor.min.css" />
</head>
<body>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

글쓴이 : ${writer}
<form id="menuForm">
    <div id="container">
        <div id="menuAdmin">
            <h2 id="menuAdminH2">게시물 작성</h2>
            <br>
            <input type="hidden" id="userNum" name="userNum" value="${user.userNum}" readonly>
            <label for="id">회원아이디</label>
            <input type="text" id="id" name="id" placeholder="회원아이디" maxlength="20" value="${user.id}" readonly>
            <br>
            <label for="title">제목</label>
            <input type="text" id="title" name="title" placeholder="제목" maxlength="50" required>
            <br>
            <label for="content">내용</label>
            <!-- 에디터를 적용할 요소 (컨테이너) -->
            <div id="content">

            </div>
            <br>
            <label for="nick">닉네임</label>
            <input type="text" id="nick" name="nick" placeholder="작성자" maxlength="10" value="${user.nick }" readonly>
            <br>

            <input type="hidden" id="writeDate" name="writeDate">
            <input type="hidden" id="hit" name="hit">
            <button type="button" id="buttonSubmit">확인</button>
        </div>
    </div>
</form>



<!-- TUI 에디터 JS CDN -->
<script src="https://uicdn.toast.com/editor/latest/toastui-editor-all.min.js"></script>
<script>
    const editor = new toastui.Editor({
        el: document.querySelector('#content'), // 에디터를 적용할 요소 (컨테이너)
        height: '500px',                        // 에디터 영역의 높이 값 (OOOpx || auto)
        initialEditType: 'wysiwyg',            // 최초로 보여줄 에디터 타입 (markdown || wysiwyg)
        initialValue: '',                       // 내용의 초기 값으로, 반드시 마크다운 문자열 형태여야 함
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

    document.getElementById("buttonSubmit").addEventListener("click", function() {
        const editorContent = editor.getHTML(); // HTML 형식으로 가져옴

        const formData = {
            userNum:document.getElementById("userNum").value,
            id:document.getElementById("id").value,
            title:document.getElementById("title").value,
            //content:document.getElementById("content").value,
            content:editorContent,
            nick:document.getElementById("nick").value
        }
        //indate:new Date().toISOString().split("T")[0], 의 의미를 알 필요가 있다.
        //index.jsp파일에서 만들 메타 CSRF 태그 두개를 js파일로 가져온다.
        //const csrfToken = document.querySelector("meta[name='_csrf']").getAttribute("content");
        //const csrfHeader = document.querySelector("meta[name='_csrf_header']").getAttribute("content");
        //개발 과정에서 csrf 토큰 비활성화

        fetch("/api/v1/write",{
            method:"POST",
            headers:{
                'Content-Type':'application/json',
            },
            body: JSON.stringify(formData)
        }).then(response =>{
            if(!response.ok){
                throw new Error("게시물 작성 실패.")
            }
            return response.text(); //백단에서 return 한 게시글 잘 작성됐다는 메시지 받음
        }).then(_=> {
            console.log("Success");
            //window.location.href="/"; //개발을 위하여 이 부분을 비활성화
            //localhost:8080 로 페이지가 이동
        }).catch(error=>{
            console.log("Error가 발생",error);
        });
    });


</script>

<%--<script src="${pageContext.request.contextPath }/resources/js/write.js"></script>--%>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>

</body>
</html>
