package com.dudeto.dudeto.dto;


import com.dudeto.dudeto.entity.CommentEntity;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor // 모든필드를 매개변수로 하는 생성자
@NoArgsConstructor //기본 생성자
public class CommentDto {
    private Long commentId;
    private String nickname;
    private String comment;
    private String date;
    private String email;
    private Long boardId;


    //Entity를 DTO로 변환
    public CommentDto toCommentDto(CommentEntity commentEntity){
        CommentDto commentDto = new CommentDto();
        commentDto.setCommentId(commentEntity.getCommentId());
        commentDto.setNickname(commentEntity.getNickname());
        commentDto.setDate(commentEntity.getDate());
        commentDto.setEmail(commentEntity.getEmail());
/*        commentDto.setBoardId(commentEntity.getBoardId());*/
        commentDto.setComment(commentEntity.getComment());

        return commentDto;
    }


}
