package com.dudeto.dudeto.entity;


import com.dudeto.dudeto.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
@Entity(name = "user")
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userNum;
    private String email;
    private String password;
    private String name;
    private String nickname;
    @ToString.Exclude //print시 순환참조 발생 => ToString에서 제외
    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.REMOVE, orphanRemoval = true) // 엔티티가 하나면 egaer 사용, 컬렉션이면 lazy 사용 => manyToOne, oneToOne 은 eager. 나머지는 lazy
    private List<BoardEntity> boardEntity = new ArrayList<BoardEntity>();

    //Dto를 Entity로 변환
    public static UserEntity toUserEntity(UserDto userDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserNum(userDto.getUserNum());
        userEntity.setEmail(userDto.getEmail());
        userEntity.setPassword(userDto.getPassword());
        userEntity.setName(userDto.getName());
        /*   userEntity.setNickname(userDto.getNickname());*/

        return userEntity;
    }


}
