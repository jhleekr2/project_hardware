package com.example.project_hardware.service;

import com.example.project_hardware.dto.FileRole;
import com.example.project_hardware.dto.UploadFile;
import com.example.project_hardware.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import static com.example.project_hardware.dto.FileRole.FILE;

@Service
public class FileServiceImpl implements FileService{

    @Autowired
    private FileMapper fileMapper;

    @Override
    public String uploadFile(FileRole fileRole, String path, MultipartFile file) {

        //저장 폴더 객체 생성
        //final String uploadDir = uploadPathImg;
        File uploaddir = new File(path);

        //저장할 폴더가 없으면 생성한다
        if (!uploaddir.exists()) {
            uploaddir.mkdirs(); //상위 폴더까지 모두 생성
            // mkdir()은 부모 없으면 실패, mkdirs()는 부모까지 생성
        }

        // 원본 파일명
        String orgFilename = file.getOriginalFilename();
        // 확장자
        String extension = orgFilename.substring(orgFilename.lastIndexOf(".") + 1);
        String uuid;
        String saveFilename;
        String uploadFile;
        File dest = null;

        try {

            // UUID 중복파일이 있을때 처리하는 로직
            // 이미 파일이 존재하는지 확인하고 존재한다면 uuid를 새로 만든다
            do {
                // 32자리 랜덤 문자열
                uuid = UUID.randomUUID().toString().replaceAll("-", "");
                // 디스크에 저장할 파일명
                saveFilename = uuid + "." + extension;
                // 저장되는 파일명
                uploadFile = Paths.get(path, saveFilename).toString();
                // 저장 파일 객체 생성
                dest = new File(uploadFile);
            } while (dest.exists());

            // DB에 업로드한 이미지 파일 기록
            // 먼저 DTO를 이용하여 이미지 파일 객체정보 객체 생성
            UploadFile uploadFileInfo = new UploadFile();
            // 기존 파일명과 저장되는 파일명을 DTO에 대입
            uploadFileInfo.setFilenameOri(orgFilename);
            uploadFileInfo.setFilenameSaved(saveFilename);
            // DB에 파일 기록하고 DB기록이 실패하면 파일 업로드가 더이상 진행되지 않음

            // 일반 파일 업로드인지, 이미지 파일 업로드인지에 따라 저장하는 DB가 달라지는 로직 추가 예정
            if(fileRole == FILE) {
                this.insertfileDB(uploadFileInfo);
            } else {
                this.insertimgDB(uploadFileInfo);
            }
            // 목적지에 파일 저장
            file.transferTo(dest);
            // API로서 저장된 파일이름을 반환하고 클라이언트에서 서버에 저장된 파일명을 그대로 받음
            return saveFilename;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void insertimgDB(UploadFile uploadFileInfo) {
        fileMapper.insertImg(uploadFileInfo);
    }

    @Override
    public void validateimgDB(int boardNo, List<String> uploadfiles) {
        fileMapper.activeImg(boardNo, uploadfiles);

    }

    @Override
    @Transactional
    public void deleteFile(FileRole fileRole, String path, List<String> deletedfiles) {
        //deletedfiles가 null이 들어올때 많은 예외를 일으키므로 예외를 일으키지 않도록 회피
        //모든 로직들을 null이 아닐때만 실행되도록 조건문으로 감싸주었다.
        if(deletedfiles != null) {
            //foreach 문을 통해 삭제할 파일마다 파일 객체 생성한다.
            for (String s : deletedfiles) {
                //전체 파일 경로 생성 - 환경설정에 따른 파일 저장 경로 + 프론트로부터 전달받은 삭제할 파일명
                String dest = path + s;
                File file = new File(dest);
                ;
                //System.out.println(dest);
                dest = path;
                // 저장공간에 있는 파일 삭제
                file.delete();
            }
        //마이바티스가 파일이 없을때 자꾸 에러를 띄워서 확인해본 결과 파일이 없을때 List<String> deletedfiles는
        //null(기댓값)이 아닌 [](실측값)이 리턴됨.
        //따라서 마이바티스의 테스트 조건 비정상 작동을 부름
        //System.out.println("삭제할 파일의 이름은 "+ deletedfiles +"입니다.");
        // DB에서도 파일 정보 삭제
        if(fileRole == FILE) {
            fileMapper.deleteGeneralFile(deletedfiles);
        } else {
            fileMapper.deleteImg(deletedfiles);
        }
        // 일반 파일 삭제인지, 이미지 파일 삭제인지에 따라 저장하는 DB가 달라지는 로직 추가 예정
        }

    }

    @Override
    public void insertfileDB(UploadFile uploadFileInfo) {
        fileMapper.insertFile(uploadFileInfo);
    }

    @Override
    public void validatefileDB(int boardNo, List<String> uploadfiles) {
        fileMapper.activeFile(boardNo, uploadfiles);

    }

    @Override
    public List<String> selectUploadImgBoardNo(int boardNo) {
        return fileMapper.selectImgBoardNo(boardNo);
    }

    @Override
    public List<UploadFile> selectUploadFileBoardNo(int boardNo) {
        return fileMapper.selectFileBoardNo(boardNo);
    }
}
