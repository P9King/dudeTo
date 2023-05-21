package com.dudeto.dudeto.controller;

import com.dudeto.dudeto.dto.UserDto;
import com.dudeto.dudeto.entity.UserEntity;

import com.dudeto.dudeto.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Controller
public class MainController {

    /*객체 생성 및 연결*/
    private final UserServiceImpl userServiceImpl;



    /*------------------------------------------맵핑 시작------------------------------*/
    @GetMapping("/")
    public String GetMain(HttpSession session) {
        session.removeAttribute("loginedUserInfo");
        return "main";
    }



    // 겟 조인
    @GetMapping("/join")
    public String GetJoin() {
        return "join";
    }


    //포스트 조인 : 아이디 중복체크 및 회원가입
    @PostMapping("/idCheck")
    @ResponseBody
    public String postIdCheck(UserEntity userEntity) {
        // view에서 받아온 값 확인
        System.out.println("깐츄롤러에서 받는 엔티티" + userEntity);

        // 중복체크를 위해 값을 서비스에 넘김
        int map = (int) userServiceImpl.isAvailableId(userEntity).orElseThrow();
        System.out.println("깐츄롤러 멥 " + map);
        if (map == 1) {
            return "이미 사용중인 email입니다.";
        } else if (map == 0) {

            return "사용가능한 email입니다.";
        }
        return "데이터베이스 오류";

    }

    //회원가입
    @PostMapping("/join")
    @ResponseBody
    public String postJoin(UserEntity userEntity) {
        System.out.println("컨트룰러 " + userEntity);
        String isAvaliableJoin = userServiceImpl.save(userEntity);
        return isAvaliableJoin;
    }


    //로그인 => 메인페이지에서 로그인 시도
    @PostMapping("/tryLogin")
    @ResponseBody
    public ConcurrentHashMap<String, String> PostAfterLogin(@ModelAttribute UserDto userDto, HttpSession session) throws Exception {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        System.out.println("userDto = " + userDto);
        //로그인 이메일과 비밀번호 맞게 존재하는지 체크
        int tryLogin = userServiceImpl.tryLogin(userDto).get();
        if (tryLogin == 1) {
            Long userNum = userServiceImpl.findUserNum(userDto); // userNum을 찾기 => 게시판에 쓰일 foreign key
            UserDto loginedUserInfo = userServiceImpl.findAllById(userNum);// userNum으로 모든 정보를 찾아서 모델에 넣어줌. 편하게 쓰기위해서 좋은 행동인지는 아직모름
            session.setAttribute("loginedUserInfo", loginedUserInfo); // userNum로 찾은 모든 정보 넘겨줌
            System.out.println("loginedUserInfo from controller = " + loginedUserInfo);
            map.put("result", "로그인 성공");
            return map;
        } else {
            map.put("result", "옳바른 정보가 아닙니다. 다시 입력해 주세요.");
            return map;
        }
    }
    /************************로그인 이후 **********************/
    @GetMapping("/afterLogin")
    public String getAfterLogin() {
        return "afterLogin";
    }

}
