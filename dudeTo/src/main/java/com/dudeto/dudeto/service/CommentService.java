package com.dudeto.dudeto.service;

import com.dudeto.dudeto.entity.BoardEntity;
import com.dudeto.dudeto.entity.CommentEntity;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface CommentService {


    /*------------------------------------------------------*/
    //댓글저장
    void save(CommentEntity commentEntity);

    //lookInBoard에서 댓글 불러오기
    List<CommentEntity> commentList(BoardEntity boardEntity, HttpSession session);
}
