package com.example.project_hardware.service;

import com.example.project_hardware.dto.FileRole;
import com.example.project_hardware.dto.UploadFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// 파일 관련 DB수정 관련 서비스
public interface FileService {

    // 파일 업로드 서비스 모듈(코드 재사용성을 위해 공통된 모듈로 구성)
    public String uploadFile(FileRole fileRole, String path, MultipartFile file);

    // 업로드한 이미지 파일을 DB에 삽입
    public void insertimgDB(UploadFile uploadFileInfo);

    // DB에 삽입한 이미지 파일을 유호화
    public void validateimgDB(int boardNo, List<String> uploadfiles);

    // 파일 삭제 서비스 모듈(코드 재사용성을 위해 공통된 모듈로 구성)
    public void deleteFile(FileRole fileRole, String path, List<String> deletedfiles);

    public void insertfileDB(UploadFile uploadFileInfo);

    public void validatefileDB(int boardNo, List<String> uploadfiles);

    public List<String> selectUploadImgBoardNo(int boardNo);

    public List<String> selectUploadFileBoardNo(int boardNo);
}
