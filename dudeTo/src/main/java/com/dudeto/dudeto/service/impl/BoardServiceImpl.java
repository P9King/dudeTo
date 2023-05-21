package com.dudeto.dudeto.service.impl;

import com.dudeto.dudeto.dto.BoardDto;
import com.dudeto.dudeto.dto.UserDto;
import com.dudeto.dudeto.entity.BoardEntity;

import com.dudeto.dudeto.entity.BoardFileEntity;
import com.dudeto.dudeto.entity.UserEntity;
import com.dudeto.dudeto.paging.Paging;
import com.dudeto.dudeto.repository.BoardFileRepository;
import com.dudeto.dudeto.repository.BoardRepository;
import com.dudeto.dudeto.service.BoardService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    @PersistenceContext
    EntityManager em;

    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;


    //게시글 저장
    @Override
    @Transactional // 맵핑관계 이용시 붙여줘야하는 어노테이션
    public void writeBoard(BoardDto boardDto, HttpSession session) throws RuntimeException, IOException {
        System.out.println("보드서비스 boardDto dadsdaas = " + boardDto.getBoardFile());
        //첨부파일 없이 저장될 경우

        if (boardDto.getBoardFile().get(0).isEmpty() || boardDto.getBoardFile() == null || boardDto.getBoardFile().equals("")) {
            BoardEntity boardEntity = BoardEntity.toBoardEntity(boardDto); //Entity를 dto로 변환
            UserDto userDto = (UserDto) session.getAttribute("loginedUserInfo"); //로그인한 유저의 모든 정보를 넣음.
            System.out.println("userDto 닉넴 = " + userDto.getNickname());
            UserEntity userEntity = UserEntity.toUserEntity(userDto); //boardEntity에서 외래키로 쓰이는 useEntity값을 넣기위해 dto를 entity로 변환
            boardEntity.setUserEntity(userEntity);//entity로 변환시킨 값을 boardEntity의 외래키값에 넣어줌
            System.out.println("em.contains(boardEntity) = " + em.contains(boardEntity));
            boardRepository.save(boardEntity);//저장 시 board테이블의 외래키 user에 값이 들어감.


        } else { // 첨부파일을 포함하여 저장될 경우
            System.out.println("else boardDto dasdsa = " + boardDto.getBoardFile());
            //boardFileEntity보다 부모인 BoardEntity가 더 먼저 나와야한다.
            BoardEntity boardEntity = BoardEntity.toBoardFileEntity(boardDto);//Entity로 변환
            UserDto userDto = (UserDto) session.getAttribute("loginedUserInfo"); //로그인한 유저의 모든 정보를 넣음.
            UserEntity userEntity = UserEntity.toUserEntity(userDto); //boardEntity에서 외래키로 쓰이는 useEntity값을 넣기위해 dto를 entity로 변환
            boardEntity.setUserEntity(userEntity);//entity로 변환시킨 값을 boardEntity의 외래키값에 넣어줌
            boardRepository.save(boardEntity);//저장 시 board테이블의 외래키 user에 값이 들어감.


            // Long savedId = boardRepository.save(boardEntity).getBoardId(); // ***중요 : 저장을하면서 아이디 값을 가져온다***
            boardRepository.save(boardEntity); //위에 저장하고 아이디 값 가져오는게 안돼 지금 그래서 따로 분리 실행
            BoardEntity boardIdTheLatestOne = boardRepository.boardIdTheLatestOne(boardEntity); //
            BoardEntity board = boardRepository.findById(boardIdTheLatestOne.getBoardId()).get(); //옵셔널이기 때문에 .get()으로 값을 빼다 쓴다.

            //여러개 파일을 하나씩 꺼내서 쓴다
            for (MultipartFile boardFile : boardDto.getBoardFile()) {
                String originFileName = boardFile.getOriginalFilename(); //파일의 진짜 이름
                System.out.println("originFileName aa = " + originFileName);
                String storedFileName = System.currentTimeMillis() + "_" + originFileName; // 서버에 저장할 이름 => 중복된 값을 제외 하여 값을 넣음

                //파일을 저장 할 위치를 정하기 위해 내 프로잭트에서 static 안에 files 라는 경로에 저장
                String savePath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";

                //참고!!new File(String parent, String child) parent 디렉토리에 child 이름의 디렉토리나 파일을 나타내는 File 객체 생성, File(String pathName) pathName이 나타내는 File 객체 생성
                File file = new File(savePath + storedFileName); // savePath에 File객체를 생성하겠지.
                boardFile.transferTo(file); // 파일을 우리가 지정한 곳으로 전송
                //boardFile.transferTo(new File(savePath)); 한 번에 작성하는 거
                BoardFileEntity boardFileEntity = BoardFileEntity.toBoardFileEntity(board, originFileName, storedFileName);//Entity로 바꾼 board 안에는 board_id가 존재함 근데 객채로 넘겨준거임

                boardFileRepository.save(boardFileEntity);
            }
        }
    }

    //게시글 리스트 불러오기
    @Override
    @Transactional
    public List<BoardDto> listBoard(Integer limit, Integer offset) {
        List<BoardEntity> list = boardRepository.findPagedBoard(limit, offset); // Entity로 받아옴 => dto로 변환
        List<BoardDto> returnList = new ArrayList<>(); // return에 값을 넣어줄 리스트생성
/*
        BoardEntity aa =   em.find(BoardEntity.class, 2L);
        System.out.println("aa = " + aa);
        System.out.println("aa.getUserEntity().getNickname() = " + aa.getUserEntity().getNickname());*/

        for (int i = 0; i < list.size(); i++) {
            BoardEntity boardEntity = list.get(i);
            boardEntity = em.find(BoardEntity.class, list.get(i).getBoardId());
            System.out.println("list qwe= " + boardEntity.getUserEntity());
            System.out.println("list.get(i).getUserEntity().getNickname() = " + boardEntity.getUserEntity().getNickname());
            BoardDto boardDto = BoardDto.toBoardDto2(list.get(i));
            System.out.println("boardDto 서비스 리스트보드 = " + boardDto);
            returnList.add(boardDto);
        }

        return returnList;
    }


    // 특정 게시글 보기(boardId를 이용)
    @Override
    public List<BoardEntity> lookInBoard(BoardEntity boardEntity) {
        List<BoardEntity> list = new ArrayList<>();
        list = boardRepository.lookInBoard(boardEntity);
        list.stream().forEach(i -> System.out.println("해당 글에 관한 모든 정보" + i));

        return list;
    }

/* 이거는 스프링 기능을 이용한 페이징
    public Page<BoardDto> paging(Pageable pageable) {
        int page = pageable.getPageNumber()-1;//page는 0부터 시작 즉 사용자가 원하는 페이지 -1이 우리가 불러올 페이지
        int pageLimit = 5; //한 페이지에 보여줄 개수
        //boardId를 기준으로 5개씩 내림차순이며 아래 page는 0부터 시작
        Page<BoardEntity> boardEntityPage = //얘도 앤티티 객체임
            boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC,"boardId" )));
        Page<BoardDto> boardDtoPage = boardEntityPage.map(boardEntity -> new BoardDto(boardEntity.getBoardId(), boardEntity.getTitle(), boardEntity.getContent(), boardEntity.getEmail(), boardEntity.getDate()));
        return boardDtoPage;
    }
*/

    @Override
    public Long findCountBoard(BoardDto boardDto) {
        return boardRepository.count();
    }

    @Override
    public Paging paging(int showListCount, int pageSize, Long totalBoardCount, int curPage) {
        //각각 보여줄 게시글 개수:5, 페이지를 몇 개씩 보이게 할건지 5개, 우리가 구한 총 게시글수(Long타입), 현재 페이지 = 0
        Paging paging = new Paging(showListCount, pageSize, totalBoardCount, curPage);

        return paging;
    }

    @Override
    public List<BoardFileEntity> findImgById(Long boardId) {
        List<BoardFileEntity> boardImgFiles = boardFileRepository.findImgById(boardId);
        System.out.println("boardImgFiles = " + boardImgFiles);
        return boardImgFiles;
    }
}
