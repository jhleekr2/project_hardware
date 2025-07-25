 //DOM 객체 연결(html혹은 jsp파일안에 있는 태그들.
    //즉 객체들을 자바스크립트와 연결시키는 과정
    const container = document.getElementById("container");
    const boardItem = document.getElementById("boardItem");

    function fetchDetail() {
    fetch("/api/v1/boardView/{boardNo}").then(response => response.json())
        .then(detail => {
            boardItem.innerHTML = '';//기존메뉴목록을 초기화
            detail =>{
                //게시물 세부사항 아이템을 생성하여 페이지에 추가
                const boardItem = document.createElement('br');
                boardItem.className='board-item';
                boardItem.innerHTML=`
                        <input type="hidden"  id="userNum" name="userNum" value="${board.userNum}" readonly>
                        <label for="id">회원아이디</label>
                        <input type="text" id="id" name="id" maxlength="20" value="${user.id}" readonly>
                        <br>
                        <label for="title">제목</label>
                        <input type="text" id="title" name="title" maxlength="10" value="${board.title}" readonly>
                        <br>
                        <label for="content">내용</label>
                        <input type="text" id="content" name="content" maxlength="30" value="${board.content}" readonly>
                        <br>
                        <label for="nick">닉네임</label>
                        <input type="text" id="nick" name="nick" maxlength="10" value="${user.nick }" readonly>
                        <br>

                        <input type="text" id="writeDate" name="writeDate" value="${board.writeDate}" readonly>
                        <input type="text" id="hit" name="hit" value="${board.hit}" readonly>
                    `
                boardItem.appendChild(boardDetailElement);
                };
            })
        }
    //백엔드단에서 프론트단 데이터 가져온다

 //메인페이지가 열리면 자동실행됨
 window.addEventListener('load',fetchDetail);

