package com.dudeto.dudeto.dto;

import com.dudeto.dudeto.entity.BoardEntity;
import com.dudeto.dudeto.entity.BoardFileEntity;
import com.dudeto.dudeto.entity.UserEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@ToString
@AllArgsConstructor // 모든필드를 매개변수로 하는 생성자
@NoArgsConstructor //기본 생성자
public class BoardDto {

    private Long boardId;
    private String title;
    private String content;
    private int boardHits; // 게시글 조회수


    /*날짜와 관련된 데이터*/
    private LocalDateTime boardCreatedTime;// 게시글 생성된 시간
    private LocalDateTime boardUpdatedTime;// 게시글이 업데이트 된 시간.


    /*이미지 파일과 관련된 데이터들*/
    private List<MultipartFile> boardFile; // controller에서 사용되어 file을 담는 용도

    
    /*  list<string>인건 여러 파일을 다룰 수 있게 끔 파일이름을 리스트에 넣음*/
    private List<String> originFileName; // 보통 서버에있는것을 쓰지만 사용자가 올린 파일 이름을 슬 경우 필요 
    private List<String> storedFileName; // 서버에 저장될 파일 이름 (겹치지 않아야 하기때문에)
    
    private String filePath; //파일 저장경로
    private int fileAttached; //파일 첨부시 1 , 미첨부시 0;


    /*UserEntity 테이블과 맵핑 된 데이터*/
    private String userNickname; // user 테이블FK 못해서 임시로 작성자로 넣어 둔 값
    private UserDto userDto; // 나중에 user 테이블FK 해야 할 값


    //BoardEntity를 Dto로 변환.
    public static BoardDto toBoardDto(BoardEntity boardEntity) {
        BoardDto boardDto = new BoardDto();
        boardDto.setBoardId(boardEntity.getBoardId());
        boardDto.setTitle(boardEntity.getTitle());
        boardDto.setContent(boardEntity.getContent());
        boardDto.setBoardCreatedTime(boardEntity.getCreateTime());
        boardDto.setBoardUpdatedTime(boardEntity.getUpdateTime());

        return boardDto;
    }

    //임시로 작성자를 넣어 둔 메서드
    public static BoardDto toBoardDto2(BoardEntity boardEntity) {
        BoardDto boardDto = new BoardDto();
        boardDto.setBoardId(boardEntity.getBoardId());
        boardDto.setTitle(boardEntity.getTitle());
        boardDto.setContent(boardEntity.getContent());
        boardDto.setBoardCreatedTime(boardEntity.getCreateTime());
        boardDto.setBoardUpdatedTime(boardEntity.getUpdateTime());
        boardDto.setUserNickname(boardEntity.getUserEntity().getNickname());

        //파일이 첨부 되지 않은 상태.== 0
        if(boardEntity.getFileAttached() == 0){
        boardDto.setFileAttached(0); //boardEntity.getFileAttached()
        }else{
        //파일이 첨부 된 상태 == 1
            List<String> orginalFileNameList = new ArrayList<>();
            List<String> storedFileNameList = new ArrayList<>();
            boardDto.setFileAttached(boardEntity.getFileAttached()); // 1

            for(BoardFileEntity boardFileEntity : boardEntity.getBoardFileEntityList()) {
                orginalFileNameList.add(boardFileEntity.getOriginalFileName());
                storedFileNameList.add(boardFileEntity.getStoredFileName());
            }

            boardDto.setOriginFileName(orginalFileNameList);
            boardDto.setStoredFileName(storedFileNameList);
        }

        return boardDto;
    }



}
