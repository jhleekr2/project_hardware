document.getElementById("buttonSubmit").addEventListener("click", function() {
    const formData = {
        userNum:document.getElementById("userNum").value,
        id:document.getElementById("id").value,
        title:document.getElementById("title").value,
        content:document.getElementById("content").value,
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
        window.location.href="/";
        //localhost:8080 로 페이지가 이동됩니다.
    }).catch(error=>{
        console.log("Error가 발생",error);
    });
});