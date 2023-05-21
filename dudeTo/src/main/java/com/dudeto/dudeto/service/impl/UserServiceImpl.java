package com.dudeto.dudeto.service.impl;

import com.dudeto.dudeto.dto.UserDto;
import com.dudeto.dudeto.entity.UserEntity;
import com.dudeto.dudeto.repository.UserRepository;
import com.dudeto.dudeto.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Optional isAvailableId(UserEntity userEntity){
        System.out.println("Impl userEntity = " + userEntity);
        //옵셔널
        Optional<Integer> optionalUserEntity = Optional.of(Optional.ofNullable(userRepository.findByEmail(userEntity))
                .orElse(0));
       return optionalUserEntity;

    }
    @Override
    public String save(UserEntity userEntity){

        System.out.println("impl " + userEntity.getPassword());
        if(userEntity.getEmail().equals("") || userEntity.getPassword().equals("") || userEntity.getName().equals("") /*|| userEntity.getNickname().equals("")*/){
            return "작성하지 않은 란이 존재합니다.";
        }else if(userRepository.findByEmail(userEntity) == 0) {
             userRepository.save(userEntity);
            return "회원가입 되셨습니다.";
        }
        return "데이터 베이스 오류";
    }
    @Override
    public Optional<Integer> tryLogin(UserDto userDto) {
        UserEntity userEntity =   UserEntity.toUserEntity(userDto);
        System.out.println("user service userEntity = " + userEntity);
        return userRepository.tryLogin(userEntity);
    }

    // 게시글에 writer에 닉네임을 넣기 위해서 nickname을 찾아옴
    @Override
    public String findNickname(UserDto userDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.toUserEntity(userDto);
        String nickname = userRepository.findNicknameByEmail(userEntity);

        return nickname;
    }

    @Override
    public Long findUserNum(UserDto userDto) {
        UserEntity userEntity = UserEntity.toUserEntity(userDto);
        return  userRepository.findIdByEmail(userEntity); //jpa에서 찾아온 user_num
    }

    // 바로 위에서 찾은 user_num으로 모든 객체 정보를 모델에 넣고 필요한 정보만 빼다 쓰려고.
    @Override
    public UserDto findAllById(Long userNum) throws Exception {
        Optional<UserEntity> opt = userRepository.findById(userNum);
        UserEntity userEntity = opt.isPresent() ? opt.get() : opt.orElseThrow(() -> new Exception("값이 없는디 우째"));
        System.out.println("userEntity = " + userEntity);
        return UserDto.toUserDto(userEntity);
    }
}
