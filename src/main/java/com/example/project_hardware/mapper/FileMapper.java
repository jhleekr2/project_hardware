package com.example.project_hardware.mapper;

import com.example.project_hardware.dto.UploadFile;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FileMapper {

    // 이미지 파일 업로드 후 DB에 삽입
    @Insert("INSERT INTO uploadimg(filename_ori, filename_saved) VALUES(#{filenameOri}, #{filenameSav})")
    void insertImg(UploadFile uploadFileInfo);

    //@Update("UPDATE uploadimg SET board_no = ${boardNo}")
    // 마이바티스의 동적 쿼리를 이용해보자
    void activeImg(int boardNo, List<String> uploadfiles);

    // 이미지 파일을 삭제 후 DB에서도 삭제
    // 여기서도 마이바티스의 동적 쿼리 이용
    void deleteImg(List<String> uploadfiles);

    // 첨부파일 업로드 후 DB에 삽입
    @Insert("INSERT INTO uploadfile(filename_ori, filename_saved) VALUES(#{filenameOri}, #{filenameSav})")
    void insertFile(UploadFile uploadFileInfo);

    void activeFile(int boardNo, List<String> uploadfiles);

    void deleteGeneralFile(List<String> uploadfiles);

}
