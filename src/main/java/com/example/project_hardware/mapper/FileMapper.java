package com.example.project_hardware.mapper;

import com.example.project_hardware.dto.UploadFile;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface FileMapper {

    // 이미지 파일 업로드 후 DB에 삽입
    @Insert("INSERT INTO uploadimg(filename_ori, filename_saved) VALUES(#{filenameOri}, #{filenameSaved})")
    void insertImg(UploadFile uploadFileInfo);

    //@Update("UPDATE uploadimg SET board_no = ${boardNo}")
    // 마이바티스의 동적 쿼리를 이용해보자
    // 업로드한 이미지 파일에 게시물번호를 부여함으로써 활성화
    void activeImg(int boardNo, List<String> uploadfiles);

    // 이미지 파일을 삭제 후 DB에서도 삭제
    // 여기서도 마이바티스의 동적 쿼리 이용
    void deleteImg(List<String> uploadfiles);

    // 첨부파일 업로드 후 DB에 삽입
    @Insert("INSERT INTO uploadfile(filename_ori, filename_saved) VALUES(#{filenameOri}, #{filenameSaved})")
    void insertFile(UploadFile uploadFileInfo);

    // 업로드한 첨부파일에 게시물번호를 부여함으로써 활성화
    void activeFile(int boardNo, List<String> uploadfiles);

    // 첨부파일을 삭제 후 DB에서도 삭제
    void deleteGeneralFile(List<String> uploadfiles);

    // 게시글 번호에 맞는 이미지 파일 DB에서 검색
    @Select("SELECT filename_saved FROM uploadimg WHERE board_no = #{boardNo}")
    List<String> selectImgBoardNo(int boardNo);

    // 게시글 번호에 맞는 첨부파일 DB에서 검색
    // 단순히 이미지 파일때와 같이 저장파일명만 검색하면 안되서 DTO를 이용하는 식으로 쿼리를 조금 변경함
    @Select("SELECT filename_ori as filenameOri, filename_saved as filenameSaved FROM uploadfile WHERE board_no = #{boardNo}")
    List<UploadFile> selectFileBoardNo(int boardNo);

}
