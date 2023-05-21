package com.dudeto.dudeto.entity;

import com.dudeto.dudeto.dto.BoardDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Entity(name = "board")
public class BoardEntity extends BaseEntity //board테이블에 시간정보도 같이 생성된다
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;
    private String title;
    private String content;
    private int boardHits; // 게시글 조회수


    @ToString.Exclude //print시 순환참조 발생 => ToString에서 제외
    @JoinColumn(name = "user")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE) // 엔티티가 하나면 egaer 사용, 컬렉션이면 lazy 사용 => manyToOne, oneToOne 은 eager. 나머지는 lazy
    private UserEntity userEntity; // setter가 있으므로 따로 setter생성하지 않음.
    public void setUserEntity(UserEntity userEntity){
        this.userEntity = userEntity;
        userEntity.getBoardEntity().add(this);
    }


    /*board : comment = 1 : N*/
    //Dto를 Entity로 변환.
    /*file과 관련된 데이터*/
    @ToString.Exclude
    @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BoardFileEntity> boardFileEntityList = new ArrayList<BoardFileEntity>();

    private int fileAttached; // 파일첨부시 1, 파일 미첨부시 0


    //파일이 미첨부 시 저장하는 메서드 
    public static BoardEntity toBoardEntity(BoardDto boardDto) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setBoardId(boardDto.getBoardId());
        boardEntity.setTitle(boardDto.getTitle());
        boardEntity.setContent(boardDto.getContent());
        boardEntity.setBoardHits(0); //
        boardEntity.setFileAttached(0);//파일 미첨부

        return boardEntity;

    }

    //게시글에 첨부파일 존재시 저장하는 메서드
    public static BoardEntity toBoardFileEntity(BoardDto boardDto) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setBoardId(boardDto.getBoardId());
        boardEntity.setTitle(boardDto.getTitle());
        boardEntity.setContent(boardDto.getContent());
        boardEntity.setBoardHits(0); //
        boardEntity.setFileAttached(1);//파일 첨부됨

        return boardEntity;
    }

}
