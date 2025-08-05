package com.example.project_hardware.controller;

import com.example.project_hardware.dto.Board;
import com.example.project_hardware.dto.BoardWithWriter;
import com.example.project_hardware.dto.Users;
import com.example.project_hardware.service.BoardService;
import com.example.project_hardware.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PageController {

    //설정 파일로부터 저장 경로 참조(현대 웹 개발의 경우는 파일 자체를 배포 폴더안에 넣지 않는다)
    //대신 설정 파일을 통해 파일을 외부에 저장하고(이때 배포시마다 환경에 맞게 저장 경로 설정 파일을 변경한다)
    //URL과 실제 파일 위치를 연결(매핑)하는 식으로 파일을 관리한다.
    // application.properties 에서 설정한 업로드 폴더 경로 주입하여 uploadPath에 넣음
    @Value("${board.imgdir}")
    private String uploadPath;

    @Autowired
    BoardService boardService;

    @Autowired
    UserService userService;

    @GetMapping("/")
    // / => localhost:8080
    public String Home() {
        return "index";
    }

    @GetMapping("/registerPage")
    public String registerPage(HttpServletRequest request, Model model) {
        return "register";
    }

    @GetMapping("/loginPage")
    public String loginPage(HttpServletRequest request, Model model) {
        return "login";
    }

    @GetMapping("/bbs/board")
    public String board() {
//    public String board(Model model) {
//        List<Board> list = new ArrayList<>();
//        list = boardService.list();
//        model.addAttribute("list", list);
        // 기존 SSR 방식의 구현을 RESTAPI 통한 CSR 방식으로 넘겨버림
        return "board";
    }

    @GetMapping("/bbs/insert")
    public String writePage(Model model, Authentication authentication) {
        // 로그인되어 있는 사용자만 접속할 수 있음(이 부분은 Spring Security가 조율함)

        // 사용자 정보 보내기 위해 새로운 변수 정의
        Users user = new Users();
        // 쓰기 페이지로 갈때 로그인된 사용자가 누군지 전송
        user = userService.findWriter(authentication.getName());
        model.addAttribute("user", user);

        // 프론트와 백을 분리할때는 완전히 이 부분의 코드가 없어지고 JWT 토큰을 RESTAPI에서 처리하는 식으로 바뀔 것이다.
        return "write";
    }

    @PostMapping("/bbs/insert")
    public String write() {
        // 게시물 
        return null;
    }

    // 업로드한 이미지 파일 조회
    @GetMapping(value = "/img/{filename}", produces = { MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE })
    @ResponseBody // 이 메서드의 응답만 RestController처럼 처리 Unknown return value type 에러 해결목적
    public byte[] printEditorImage(@PathVariable final String filename) {
        // 업로드된 파일의 전체 경로
        String fileFullPath = Paths.get(uploadPath, filename).toString();

        // 파일이 없는 경우 예외 throw
        File uploadedFile = new File(fileFullPath);
        if (uploadedFile.exists() == false) {
            throw new RuntimeException();
        }

        try {
            // 이미지 파일을 byte[]로 변환 후 반환
            byte[] imageBytes = Files.readAllBytes(uploadedFile.toPath());
            return imageBytes;

        } catch (IOException e) {
            // 예외 처리는 따로 해줘야 함
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/bbs/view/{boardNo}")
    public String viewPage(@PathVariable("boardNo") int boardNo) {
        // 게시물 조회는 프론트에서 RESTAPI 호출하는 형태로 해야 함(서버사이트 렌더링 대신 클라이언트 사이드 렌더링)
        // 따라서 여기서는 그냥 view만을 전달하는 기능
        // 개발은 RestController에 진행
        return "view";
    }

    @GetMapping("/bbs/update/{boardNo}")
    public String updatePage(Model model, Authentication authentication, @PathVariable("boardNo") int boardNo) {
        // 게시물 수정 권한이 있는지 파악
        // 작성자 본인만 수정할 수 있어야함
        System.out.println(boardNo);
        // 스프링 시큐리티의 Authentication을 통해 현재 로그인된 사용자 확인
        // 작성자와 현재 로그인된 사용자가 일치할때만 게시물 수정페이지에 접근 가능하도록 함

        Users user = new Users();
        if(authentication != null) {
            //로그인된 사용자 번호를 조회하여 대입
            user = userService.findUserNum(authentication.getName());
            
            //게시물 정보 조회(작성자를 찾기 위함)
            BoardWithWriter board = boardService.viewDetail(boardNo);
            
            if(user.getUserNum() == board.getUserNum()) {
                //게시물 작성자와 로그인된 사용자가 같음을 확인하면
                
                //게시물 업데이트 페이지로 접근하고
                return "update";
            }
        }
        //그렇지 않으면 접근을 차단한다(게시물 조회 페이지로 돌아감)
        return "view";
    }    

    
    

}

