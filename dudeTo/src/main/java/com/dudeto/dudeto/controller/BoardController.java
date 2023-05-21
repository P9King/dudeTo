package com.dudeto.dudeto.controller;

import com.dudeto.dudeto.dto.BoardDto;
import com.dudeto.dudeto.dto.UserDto;
import com.dudeto.dudeto.entity.BoardEntity;
import com.dudeto.dudeto.entity.BoardFileEntity;
import com.dudeto.dudeto.entity.CommentEntity;
import com.dudeto.dudeto.paging.Paging;
import com.dudeto.dudeto.service.impl.BoardServiceImpl;
import com.dudeto.dudeto.service.impl.CommentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {
    @PersistenceContext
    EntityManager em;

    private final BoardServiceImpl boardServiceImpl;
    private final CommentServiceImpl commentServiceImpl;


    /************************게시판 관련 컨트롤러 **********************/

    //게시글 등록하러 가기
    @GetMapping("/writeBoard")
    public String GetWriteBoard(Model model, HttpSession session) {
        UserDto loginedUserInfo = (UserDto) session.getAttribute("loginedUserInfo");
        model.addAttribute("nickname", loginedUserInfo.getNickname());
        return "Board/writeBoard";
    }

    //게시글 등록
    @PostMapping("/writeBoard") //@ModelAttribute는 html에서 받은 파라미터 이름이 해당 매개변수 객체에서 같은 이름이 있다면 그 객체로 값을 넘겨준다.
    public String PostWriteBoard(@ModelAttribute BoardDto boardDto, HttpSession session) throws IOException {
        System.out.println("보드 컨트롤러 boardDto = " + boardDto);
        //서비스로 저장할 객체와
        boardServiceImpl.writeBoard(boardDto, session);
        return "redirect:/listBoard";
    }



    /*게시글 목록(글쓰기 이후 post로 넘어옴) + 페이징 처리*/
    @PostMapping("/listBoard")
    public String postListBoard(BoardDto boardDto, Model model, HttpSession session, @RequestParam(value = "page",  defaultValue = "1") int page) {

        //페이징에 필요한 정보를 생성하자
        int limit = 5; // 보여줄 게시글을 몇 개수.
        int pageSize = 5; // 페이지 이동버튼에서 보여줄 페이지 개수.
        Long totalBoardCount = boardServiceImpl.findCountBoard(boardDto); // 총 board 게시글 개수 => Long 타입 반환.
        int curPage = page-1; //현재 페이지(쿼리로 받아올 거임)

        //페이징할 정보를 보냄
        Paging paging = boardServiceImpl.paging(limit, pageSize, totalBoardCount, curPage);

        //db에서 페이징 처리해야 해서 limit, offset 전달
        int offset = page * curPage;

        List<BoardDto> listBoard = boardServiceImpl.listBoard(5,offset ); //페이지를 오프셋으로 사용할 예정 페이지는 1페이지부터 시작
        System.out.println("listBoard = " + listBoard);
        System.out.println("paging = " + paging);

        //모델에 저장
        model.addAttribute("listBoard", listBoard);
        model.addAttribute("paging", paging);

        return "Board/listBoard";
    }


    /*게시글 목록(그냥 리스트 보기 방식으로 넘어옴) + 페이징 처리*/
    @GetMapping("/listBoard")
    public String getListBoard(BoardDto boardDto, Model model, HttpSession session, @RequestParam(value = "page",  defaultValue = "1") int page) {

        //페이징에 필요한 정보를 생성하자
        int limit = 5; // 보여줄 게시글을 몇 개수.
        int pageSize = 5; // 페이지 이동버튼에서 보여줄 페이지 개수.
        Long totalBoardCount = boardServiceImpl.findCountBoard(boardDto); // 총 board 게시글 개수 => Long 타입 반환.
        int curPage = page-1; //현재 페이지(쿼리로 받아올 거임)

        //페이징할 정보를 보냄
        Paging paging = boardServiceImpl.paging(limit, pageSize, totalBoardCount, curPage);

        //db에서 페이징 처리해야 해서 limit, offset 전달
        int offset = page * curPage;

        //페이징 조건에 맞는 게시글을 불러옴
        List<BoardDto>listBoard = boardServiceImpl.listBoard(5,offset ); //페이지를 오프셋으로 사용할 예정 페이지는 1페이지부터 시작
         listBoard.stream().forEach(i-> System.out.println("끼우루룰"+i.getUserNickname()));
        
        //모델에 저장
        model.addAttribute("listBoard", listBoard);
        model.addAttribute("paging", paging);
     /*   model.addAttribute("nickname", nicknameList);*/
        return "Board/listBoard";

    }

    @GetMapping("/lookInBoard")
    public String getLookInBoard(BoardEntity boardEntity, Model model, HttpSession session) {
        BoardEntity board = em.find(BoardEntity.class ,boardEntity.getBoardId());
        System.out.println("보드 컨트롤러 게시글 보기boardEntity = " + board);
/*        System.out.println("----"+board.getUserEntity().getNickname());*/
        //댓글에 사용될 기억할 세션 초기화
        session.removeAttribute("list");
        session.removeAttribute("boardId");

        /*게시글 정보 불러오기*/
        List<BoardEntity> list = new ArrayList<>();
        list = boardServiceImpl.lookInBoard(boardEntity);
        if(list != null){
            model.addAttribute("list", list);
        }
        System.out.println("boardEntity의 유저엔티티 = " + boardEntity.getUserEntity());
        /*  list.stream().filter(x-> x!=null).collect(Collectors.toList());*/

        Long boardId = boardEntity.getBoardId(); // 세션 값에 저장할 변수
        session.setAttribute("list",list);  //페이지 정보값을 가진 세선 값 저장
        session.setAttribute("boardId", boardId);  //댓글에 사용될 게시글 번호 저장

        UserDto loginedUesr = (UserDto)session.getAttribute("loginedUserInfo"); // 이것도 바꿔보자 나중에 보드 컨트롤 영역이니까 유저는 빼자.
        model.addAttribute("loginedUserNickname",loginedUesr.getNickname());
        
        //이미지 불러오기
        List<BoardFileEntity> boardFileEntityList = boardServiceImpl.findImgById(boardId);
        model.addAttribute("boardFileList",boardFileEntityList);
        boardFileEntityList.stream().forEach(i-> System.out.println(i.getStoredFileName()));

        /*댓글불러 오기 (boardId 이용)*/
   /*     List<CommentEntity> commentList =  commentServiceImpl.commentList(boardEntity);
        model.addAttribute("commentList", commentList);*/
        return "Board/lookInBoard";
    }


    /******************댓글 관련 컨트롤러*****************/
    //댓글달기
    @GetMapping("/writreComment")
    public String PostWriteComment(CommentEntity commentEntity, Model model, HttpSession session){
        model.addAttribute("list", session.getAttribute("list"));
        model.addAttribute("loginedUserEmail", session.getAttribute("loginedUserEmail"));

        //댓글저장
        Long boardId = (Long)session.getAttribute("boardId");
        System.out.println("boardId = " + boardId);
        commentEntity.setBoardId(boardId);
        commentServiceImpl.save(commentEntity);
        return "Board/lookInBoard";
    }


}
