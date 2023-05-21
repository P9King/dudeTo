package com.dudeto.dudeto.service;

import com.dudeto.dudeto.dto.BoardDto;
import com.dudeto.dudeto.entity.BoardEntity;
import com.dudeto.dudeto.entity.BoardFileEntity;
import com.dudeto.dudeto.paging.Paging;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

public interface BoardService {

    //게시글 저장
    @Transactional
    // 맵핑관계 이용시 붙여줘야하는 어노테이션
    void writeBoard(BoardDto boardDto, HttpSession session) throws RuntimeException, IOException;

    //게시글 리스트 불러오기
    @Transactional
    List<BoardDto> listBoard(Integer limit, Integer offset);

    // 특정 게시글 보기(boardId를 이용)
    List<BoardEntity> lookInBoard(BoardEntity boardEntity);

    Long findCountBoard(BoardDto boardDto);

    Paging paging(int showListCount, int pageSize, Long totalBoardCount, int curPage);

    List<BoardFileEntity> findImgById(Long boardId);
}
