 //DOM 객체 연결(html혹은 jsp파일안에 있는 태그들.
    //즉 객체들을 자바스크립트와 연결시키는 과정
    const container = document.getElementById("container");
    const boardList = document.getElementById("boardList");
    const paging = document.getElementById("paging");

    function fetchBoard(page= 0) {
    fetch(`/api/v1/board?page=${page}`).then(response => response.json())
        .then(list => {
            boardList.innerHTML = '';//기존메뉴목록을 초기화
            paging.innerHTML = '';//기존페이지목록을 초기화
            console.log(list);
            console.log(list.content);
            console.log(list.page);
            // list.forEach(item =>{
            //     //각 메뉴 아이템을 생성해서 리스트에 추가
            //     const boardItem = document.createElement('tr');
            //     boardItem.className='board-item';
            //     boardItem.innerHTML=`
            //             <th></th>
            //             <th>${item.boardNo}</th>
            //             <th>${item.userNum}</th>
            //             <th>${item.writeDate}</th>
            //             <th><a href="#" class="board-link" style="text-decoration:none;color:black;">${item.title}</a></th>
            //             <th>${item.hit}</th>
            //         `
            //     //게시글을 메인페이지에서 하나씩 클릭할때
            //     boardItem.querySelector(".board-link").addEventListener('click',(event)=>{
            //         event.preventDefault();
            //         console.log(`event:${event}`);
            //         //EL JSTL 쓸때 반드시 tab키 위의 따옴표로 둘러싸자(ES6)
            //         incrementCount(item.boardNo).then(() =>window.location.href=`/bbs/view/${item.boardNo}`)
            //     });
            //     boardList.appendChild(boardItem);
            // })
            list.content.forEach(content =>{
                //각 메뉴 아이템을 생성해서 리스트에 추가
                const boardItem = document.createElement('tr');
                boardItem.className='board-item';
                boardItem.innerHTML=`
                        <th></th>
                        <th>${content.boardNo}</th>
                        <th>${content.userNum}</th>
                        <th>${content.writeDate}</th>
                        <th><a href="#" class="board-link" style="text-decoration:none;color:black;">${content.title}</a></th>
                        <th>${content.hit}</th>
                    `
                //게시글을 메인페이지에서 하나씩 클릭할때
                boardItem.querySelector(".board-link").addEventListener('click',(event)=>{
                    event.preventDefault();
                    console.log(`event:${event}`);
                    //EL JSTL 쓸때 반드시 tab키 위의 따옴표로 둘러싸자(ES6)
                    incrementCount(content.boardNo).then(() =>window.location.href=`/bbs/view/${content.boardNo}`)
                });
                boardList.appendChild(boardItem);
            })

            // 페이지네이션 구현 코드(SSR대신 CSR방식 페이지네이션 구현)
            const totalPages = list.page.totalPages;
            const numberPage = list.page.number;

            let startPage = Math.floor(numberPage / 10) * 10;
            let endPage = Math.min(totalPages, startPage + 10)

            if(startPage >= 10) {
                const PrevPage = document.createElement('li');
                PrevPage.className = 'paging-item';
                PrevPage.innerHTML = '<a href="#" style="margin:5px; text-decoration:none;"><<</a>'
                PrevPage.querySelector('a').addEventListener('click', (e) => {
                    e.preventDefault();
                    fetchBoard(startPage - 10);
                });
                paging.appendChild(PrevPage);
            }

            for (let i = startPage ; i < endPage ; i++) {
                const pageItem = document.createElement('li');
                pageItem.className = 'paging-item';
                pageItem.innerHTML = `<a href="#" style="margin:5px; text-decoration:none;">${i + 1}</a>`;

                pageItem.querySelector('a').addEventListener('click', (e) => {
                    e.preventDefault();
                    console.log(`${i}번 페이지로 이동합니다.`);

                    // 페이지 클릭하면 fetchBoard를 재호출함
                    fetchBoard(i);
                });
                paging.appendChild(pageItem);
            }

            if (endPage < totalPages) {
                const NextPage = document.createElement('li');
                NextPage.className = 'paging-item';
                NextPage.innerHTML = '<a href="#" style="margin:5px; text-decoration:none;">>></a>';
                NextPage.querySelector('a').addEventListener('click', (e) => {
                    e.preventDefault();
                    fetchBoard(endPage); // 다음 블록의 시작 페이지(i.e. 현재 블록의 끝)로 이동
                });
                paging.appendChild(NextPage);
            }
        })
    //백엔드단에서 프론트단 데이터 가져온다
}

    function incrementCount(boardNo) {
        console.log(`fetch boardno - ${boardNo}`);
    return fetch(`/api/v1/count/${boardNo}`,{
    method:'PUT',
    // CSRF 방어 생략으로 헤더 전체 생략
}).then(response=>{
    if(!response.ok){
    console.log("데이터가 프론트서버에서 백엔드서버로 넘어가는데 실패.");
}
}).catch(error=>{
    console.error(`Error:${error}`);
})
}
    //메인페이지가 열리면 자동실행됨
    window.addEventListener('load', () => {
        fetchBoard(0);
    });