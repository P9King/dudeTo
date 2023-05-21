package com.dudeto.dudeto.repository;

import com.dudeto.dudeto.dto.UserDto;
import com.dudeto.dudeto.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    //회원가입시 이메일 중복되는지 체크
    @Query(value = "select count(*) from user where email = :#{#user.email}", nativeQuery = true)
    int findByEmail(@Param("user") UserEntity userEntity);

    //로그인시 옳바른 로그인정보인지 확인
    @Query(value = "select count(*) from user where email = :#{#user.email} and password = :#{#user.password}", nativeQuery = true)
    Optional<Integer> tryLogin(@Param("user") UserEntity userEntity);

    @Query(value = "select nickname from user where  email = :#{#user.email}")
    String findNicknameByEmail(@Param("user") UserEntity userEntity);

    @Query(value = "select user_num from user where email = :#{#user.email}", nativeQuery = true)
    Long findIdByEmail(@Param("user") UserEntity userEntity);
}
