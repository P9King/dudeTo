package com.dudeto.dudeto.service.impl;

import com.dudeto.dudeto.entity.BoardEntity;
import com.dudeto.dudeto.entity.CommentEntity;
import com.dudeto.dudeto.repository.CommentRepository;
import com.dudeto.dudeto.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    // Repository 생성 및 연결
    private final CommentRepository commentRepository;



    /*------------------------------------------------------*/
    //댓글저장
    @Override
    public void save(CommentEntity commentEntity) {
        commentRepository.save(commentEntity);
    }


    //lookInBoard에서 댓글 불러오기
    @Override
    public List<CommentEntity> commentList(BoardEntity boardEntity , HttpSession session) {
        System.out.println("boardId = " + boardEntity.getBoardId());
        
        //게시글 번호를 댓글테이블에서 boardId로 값을 넘겨주는 과정
        Long boardId = (Long)session.getAttribute("boardId");
        List<CommentEntity> commentList = new ArrayList<>();
        commentList = commentRepository.commentList(boardEntity);
        commentList.stream().forEach(i-> System.out.println(i));

        return commentList;
    }

}