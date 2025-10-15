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

        <label for="fileInput">첨부 파일</label>
        <input type="file" id="fileInput" name="fileInput" multiple>
        <br>
        <ul id="fileList"></ul> <br>

        <button type="button" id="buttonSubmit">수정된 게시물 적용</button>  <p> <button type="button" id="buttonCancel">취소</button>
    </div>
</form>

<div id="downloadfile">
</div>

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

    // 업로드된 이미지 URL 저장 배열
    let uploadedImageUrls = [];
    // 에디터에서 최종적으로 사용된 이미지 URL 저장 배열
    let usedImageUrls = [];
    // let usedImageUrls = new Set();
    // 중복을 불가하게 Set을 사용함.

    // 에디터에서 삭제한 이미지 URL 저장 배열
    let deletedImageUrls = [];

    // 롤백할때 사용할 이미지 URL 저장 배열(삭제 여부와 상관없이 나중에 업로드한 이미지 URL 저장 배열
    let rollbackImageUrls = [];

    // 업로드된 일반 파일명 저장 배열
    let uploadedFileIds = [];

    // 최종적으로 사용된 일반 파일명 저장 배열
    let usedFileIds = [];

    // 업로드 후 삭제된 일반 파일명 저장 배열
    let deletedFileIds = [];

    // 롤백할때 사용할 일반 파일명 저장 배열
    let rollbackFileIds = [];

    function fetchDetail() {
        fetch(`/api/v1/boardView/${boardNo}`).then(response => response.json())
            .then(board => { // 백엔드에서 받은 Board 객체가 board로 들어옴.

                console.log(board);

                const memberId = board.id || "탈퇴한 회원입니다.";
                const memberNick = board.nick || "탈퇴한 회원입니다.";

                // 폼 내부에 값 할당
                menuForm.querySelector('#userNum').value = board.userNum;
                menuForm.querySelector('#id').value = memberId;
                menuForm.querySelector('#title').value = board.title;

                // 내용은 div에 innerHTML로 채웁니다.
                //menuForm.querySelector('#contentDisplay').innerHTML = board.content;

                menuForm.querySelector('#nick').value = memberNick;
                menuForm.querySelector('#writeDate').value = board.writeDate;

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

                                // 5. 프론트엔드에 업로드된 이미지의 URL을 저장
                                uploadedImageUrls.push(filename);

                                // 6. 게시글 수정할때는 롤백을 대비해서 나중에 업로드된 이미지의 URL만을 따로 저장한다.
                                // 이후 롤백을 할때 나중에 업로드된 이미지 URL 저장 변수를 전송하도록 한다.
                                rollbackImageUrls.push(filename);
                            } catch (error) {
                                console.error('업로드 실패 : ', error);
                            }
                        }
                    }
                    /* end of hooks */

                });

                // 최초 로딩때 HTML 코드 파싱
                // 최조에 사용되어 있는 이미지 파일들을 uploadedImageUrls에 넣음
                // 이는 최초 로딩때 해당 이미지 파일들은 이미 업로드 되어 있기 때문임

                const editorContent = editor.getHTML();

                const tempDiv = document.createElement('div');
                tempDiv.innerHTML = editorContent;
                const images = tempDiv.querySelectorAll('img');

                // <img> 태그의 src 속성에서 파일명만 추출
                images.forEach(img => {
                    const src = img.getAttribute('src');
                    if (src && src.startsWith('/img/')) {
                        const filename = src.substring(src.lastIndexOf('/') + 1);
                        uploadedImageUrls.push(filename);
                    }
                });

                // 파일 업로드 프론트 코드
                document.getElementById('fileInput').addEventListener('change', async (e) => {
                    const files = e.target.files;
                    const fileListElement = document.getElementById('fileList');

                    for (const file of files) {
                        const formData = new FormData();
                        formData.append('file', file);

                        try {
                            const response = await fetch('/api/v1/uploadfile', {
                                method: 'POST',
                                body: formData,
                            });

                            if (!response.ok) {
                                throw new Error('파일 업로드 실패');
                            }

                            const filename = await response.text();

                            // 성공적으로 업로드된 파일명 저장
                            uploadedFileIds.push(filename);
                            // 롤백을 대비해서 나중에 업로드된 파일명을 따로 저장
                            rollbackFileIds.push(filename);

                            // 사용자에게 파일이 업로드되었음을 알림
                            const listItem = document.createElement('li');
                            <%--listItem.textContent = `[업로드됨] ${file.name}`;--%>
                            listItem.textContent = file.name;
                            fileListElement.appendChild(listItem);

                            // 취소 버튼 역할을 할 링크를 생성
                            const cancelButton = document.createElement('a');
                            cancelButton.href = '#'; // 클릭 가능한 링크로 만들기 위해 '#'를 사용합니다.
                            cancelButton.textContent = '(업로드 취소)';
                            cancelButton.style.marginLeft = '10px';
                            cancelButton.onclick = async (event) => {
                                event.preventDefault(); // 기본 링크 동작을 막음

                                // deletedFileIds 변수에 업로드한 파일 이름을 추가하고
                                deletedFileIds.push(filename);
                                // 프론트에서 추가한 업로드 파일 이름 목록에서도 삭제
                                // document.removeChild('li')
                                // 최종적으로 DOM에서도 리스트 아이템을 제거
                                listItem.remove();

                                // const index = uploadedFileIds.indexOf(filename);
                                // if (index > -1) {
                                //     uploadedFileIds.splice(index, 1);
                                // }

                            }

                            // 리스트 아이템에 취소 버튼을 추가합니다.
                            listItem.appendChild(cancelButton);

                            // 파일 리스트에 리스트 아이템을 추가합니다.
                            fileListElement.appendChild(listItem);

                        } catch (error) {
                            console.error('파일 업로드 실패:', error);
                            const listItem = document.createElement('li');
                            listItem.textContent = `[실패] ${file.name}`;
                            // fileListElement.appendChild(listItem);
                            //파일 업로드 실패했을때는 appendChild가 작동하지 않도록 함
                        }
                    }
                });


                //게시물 수정 비동기 처리
                document.getElementById("buttonSubmit").addEventListener("click", function() {
                    const editorContent = editor.getHTML(); // HTML 형식으로 가져옴

                    // 여기서 HTML코드를 파싱하여 최종적으로 업로드되는 이미지 파일만 찾는다.
                    // 그리하여 최종적으로 업로드되는 이미지 파일만을 유효화하는 절차를 밟는다.
                    // 유효화되지 않은 이미지 파일은 정기적인 스케줄러 관리에 의해 삭제한다.

                    const tempDiv = document.createElement('div');
                    tempDiv.innerHTML = editorContent;
                    const images = tempDiv.querySelectorAll('img');

                    // <img> 태그의 src 속성에서 파일명만 추출
                    images.forEach(img => {
                        const src = img.getAttribute('src');
                        if (src && src.startsWith('/img/')) {
                            const filename = src.substring(src.lastIndexOf('/') + 1);
                            usedImageUrls.push(filename);
                        }
                    });

                    // 업로드된 전체 이미지 목록에서 최종적으로 업로드되지 않은 목록만 필터링
                    deletedImageUrls = uploadedImageUrls.filter(filename => !usedImageUrls.includes(filename));

                    // 어차피 DB와는 게시물 생성할때 게시글 내용과 DB의 업로드 이미지 파일이 동기화되어 있기 때문에
                    // 게시글 수정할때의 이미지 파일 생명주기 관리도 게시글 생성할때와 동일하게 관리하면 된다.

                    const formData = {
                        title:document.getElementById("title").value,
                        content:editorContent,
                        //업로드한 파일 이름을 다시 전송
                        //이때 불필요한 이미지파일(초기 업로드했으나, 편집에서 나중에 지운 이미지파일)을 지우는 로직을 추가하기 위해
                        uploadfile:uploadedImageUrls,
                        deletedfile:deletedImageUrls,
                        uploadgeneralfile:uploadedFileIds,
                        deletedgeneralfile:deletedFileIds
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
                        // 사이트에서 빠져나갈 때 동작 이벤트를 제거한다. 이렇게 해서 페이지 빠져나갈때 beforeunload 이벤트의 업로드 취소 로직이
                        // 실행되지 않도록 한다.
                        window.removeEventListener('beforeunload', rollbackUploadedImages);
                        window.location.href="/bbs/board";
                        //localhost:8080/bbs/board 로 페이지가 이동
                    }).catch(error=>{
                        console.log("Error가 발생",error);
                    });
                });

                document.getElementById("buttonCancel").addEventListener("click", function() {
                    // 취소 버튼을 눌렀을때 동작 - 이때는 업로드 시도한 모든 이미지 파일을 삭제하는 로직으로 작동한다.
                    // 이미 DB에 기록된 내용은 파일부분만 있기 때문에 그 부분만을 formdata로 해서 전송을 하자.

                    const formData = {
                        uploadfile: rollbackImageUrls,
                        uploadgeneralfile: rollbackFileIds
                    }

                    fetch("/api/v1/rollback",{
                        method:"POST",
                        headers:{
                            'Content-Type':'application/json',
                        },
                        body: JSON.stringify(formData)
                    }).then(response =>{
                        if(!response.ok){
                            throw new Error("게시물 롤백 실패.")
                        }
                        return response.text();
                    }).then(_=> {
                        console.log("Success");
                        window.location.href="/bbs/board" // 이전 페이지로 이동
                    }).catch(error=>{
                        console.log("Error가 발생",error);
                    });
                });

            });

    }

    // 백엔드로부터 기존에 업로드된 파일목록을 가져오고 uploadFileIdes에 추가함
    function filelistdisplay() {
        fetch(`/api/v1/boardFileView/${boardNo}`).then(response => response.json())
            .then(file => { // 백엔드로부터 받은 객체가 file로 들어옴
                //기존 내용 초기화
                downloadfile.innerHTML = '';
                console.log(file);
                //uploadedFileIds.push(file.filenameSaved);

                // files 배열을 순회하며 각 파일 객체를 처리
                file.forEach(file => {
                    // 게시물 상세 내용을 담을 컨테이너 요소 생성 (div 사용)
                    const fileElement = document.createElement('div');
                    fileElement.className = 'board-detail-item'; // CSS 클래스명 지정
                    const listItem = document.createElement('li');  // <li> 태그 생성

                    const fileLink = document.createElement('a');  // <a> 태그 생성

                    fileLink.href = '/download/' + file.filenameSaved; // <a> 태그의 href 속성에 다운로드 URL 설정
                    fileLink.textContent = file.filenameOri; // 파일 객체 안의 filenameOri에 접근

                    // 기존 업로드 파일 삭제버튼 만들기
                    const deleteButton = document.createElement('a');
                    deleteButton.href = '#';
                    deleteButton.textContent = ' (기존 파일 삭제)';
                    deleteButton.style.marginLeft = '10px';
                    deleteButton.style.color = 'red';

                    //  삭제 버튼 클릭 이벤트
                    deleteButton.onclick = (event) => {
                        event.preventDefault();

                        // 삭제 목록에 추가 (서버에서 지울 파일)
                        deletedFileIds.push(file.filenameSaved);

                        // 화면에서 파일 목록 항목 제거
                        fileElement.remove();

                        // 참고: uploadedFileIds에서는 제거하지 않아도 됩니다.
                        // 서버에서 uploadedFileIds와 deletedFileIds를 비교하여 최종적으로 남아있는 파일을 처리하거나,
                        // 이 파일을 '삭제 목록'으로만 활용하면 됩니다.
                    }


                    listItem.appendChild(fileLink); // <li> 태그 안에 <a> 태그 추가
                    listItem.appendChild(deleteButton); // <li> 태그 안에 파일삭제 <a> 태그 추가

                    fileElement.appendChild(listItem); // <div> 태그 안에 <li> 태그 추가

                    // downloadfile 요소에 추가
                    downloadfile.appendChild(fileElement);

                    // 업로드된 파일 명단에 추가(나중에 파일 생명주기 관리하는데 필요)
                    uploadedFileIds.push(file.filenameSaved);
                });
            })
    }
    //백엔드단에서 프론트단 데이터 가져온다

    //데이터를 가져온 후 게시글 작성자와 현재 로그인된 사용자를 비교하여

    //메인페이지가 열리면 자동실행됨
    window.addEventListener('load',fetchDetail);

    window.addEventListener('load',filelistdisplay);


    //기본적으로 사이트에서 빠져나갈때 동작을 추가
    window.addEventListener('beforeunload', rollbackUploadedImages)

    function rollbackUploadedImages() {
        // 사이트에서 빠져나갈 때 동작 - 이때도 업로드 시도한 모든 이미지 파일을 삭제하는 로직으로 작동한다.
        // 이미 DB에 기록된 내용은 파일부분만 있기 때문에 그 부분만을 formdata로 해서 전송을 하자.

        const formData = {
            uploadfile: rollbackImageUrls,
            uploadgeneralfile: rollbackFileIds
        }

        fetch("/api/v1/rollback",{
            method:"POST",
            headers:{
                'Content-Type':'application/json',
            },
            body: JSON.stringify(formData)
        }).then(response =>{
            if(!response.ok){
                throw new Error("게시물 롤백 실패.")
            }
            return response.text();
        }).then(_=> {
            console.log("Success");
            //window.location.href="/bbs/board" // 사이트 납치 태그가 될수 있기에 주석처리
        }).catch(error=>{
            console.log("Error가 발생",error);
        });
    }



</script>

</body>
</html>
