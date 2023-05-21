package com.dudeto.dudeto.entity;

import com.dudeto.dudeto.dto.CommentDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@RequiredArgsConstructor
@Getter
@Setter
@ToString
@Entity(name="comment")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;
    private String nickname;
    private String comment;
    private String date;
    private String email;

    /*board : comment = 1 : N 관계*/

    private Long boardId;
/*    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardId")
    private BoardEntity boardEntity;*/


    
    
    //Dto를 Entity로 변환
    public CommentEntity toCommentEntity(CommentDto commentDto) {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setCommentId(commentDto.getCommentId());
        commentEntity.setNickname(commentDto.getNickname());
        commentEntity.setComment(commentDto.getComment());
        commentEntity.setDate(commentDto.getDate());
        commentEntity.setEmail(commentDto.getEmail());
        commentEntity.setBoardId(commentDto.getBoardId());

        return commentEntity;
    }

    

}
