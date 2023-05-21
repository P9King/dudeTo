package com.dudeto.dudeto.dto;

import com.dudeto.dudeto.entity.UserEntity;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor // 모든필드를 매개변수로 하는 생성자
@NoArgsConstructor //기본 생성자
public class UserDto {

    private Long userNum;
    private String email;
    private String password;
    private String name;
    private String nickname;

    private Long boardId; // board 테이블FK

    //Entity를 UserDto로 변환
    public static UserDto toUserDto(UserEntity userEntity) {
        UserDto userDto = new UserDto();
        userDto.setUserNum(userEntity.getUserNum());
        userDto.setEmail(userEntity.getEmail());
        userDto.setPassword(userEntity.getPassword());
        userDto.setName(userEntity.getName());
        userDto.setNickname(userEntity.getNickname());
        return userDto;

    }

}
