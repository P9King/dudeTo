package com.dudeto.dudeto.service;

import com.dudeto.dudeto.dto.UserDto;
import com.dudeto.dudeto.entity.UserEntity;

import java.util.Optional;

public interface UserService {

    public Optional isAvailableId(UserEntity userEntity);

    String save(UserEntity userEntity);

    Optional<Integer> tryLogin(UserDto userDto);

    // 게시글에 writer에 닉네임을 넣기 위해서 nickname을 찾아옴
    String findNickname(UserDto userDto);

    Long findUserNum(UserDto userDto);

    // 바로 위에서 찾은 user_num으로 모든 객체 정보를 모델에 넣고 필요한 정보만 빼다 쓰려고.
    UserDto findAllById(Long userNum) throws Exception;
}
