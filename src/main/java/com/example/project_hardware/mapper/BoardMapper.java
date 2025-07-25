package com.example.project_hardware.mapper;

import com.example.project_hardware.dto.Board;
import com.example.project_hardware.dto.BoardWithWriter;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BoardMapper {

    // MyBatis 어노테이션 방식으로 mapping할때 DB컬럼과 DTO 차이 고려하여야 한다
    // 이때 DB 컬럼명과 DTO 변수명이 다른데 SQL 쿼리에서 alias 사용하여 맞춰주고 있다.
    // 사실 application.properties에서 설정을 통해 camelcase와 _(언더스코어)설정을 바꿔줄 수도 있긴 하다.
    // mybatis.configuration.map-underscore-to-camel-case=true
    // 이상의 방법들을 안 쓰려면 마이바티스를 어노테이션 방식이 아닌 XML방식으로 써야 한다(학원에서 배운 방법)

    @Select("SELECT board_no as boardNo, user_num as userNum, write_date as writeDate, title, content, hit from board")
    List<Board> viewBoard();

    @Update("UPDATE board set hit = hit + 1 where board_no = #{boardNo}")
    void addBoardCount(int boardNo);

    @Insert("INSERT INTO board(user_num, write_date, title, content, hit) VALUES (#{userNum}, NOW(), #{title}, #{content}, 0)")
    void addBoardList(Board board);

    @Select("SELECT a.board_no as boardNo, a.user_num as userNum, a.write_date as writeDate, a.title, a.content, a.hit, b.id, b.nick FROM board as a LEFT OUTER JOIN users as b on a.user_num = b.user_num WHERE board_no = #{boardNo}")
    BoardWithWriter viewBoardDetail(int boardNo);

    @Update("UPDATE board set title=#{title}, content=#{content} where board_no = #{boardNo}")
    void updateBoardList(Board board);

    @Delete("DELETE from board where board_no = #{boardNo}")
    void boardDelete(int boardNo);
}
