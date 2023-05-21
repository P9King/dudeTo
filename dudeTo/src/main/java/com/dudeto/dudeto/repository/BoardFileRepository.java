package com.dudeto.dudeto.repository;

import com.dudeto.dudeto.entity.BoardFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;


public interface BoardFileRepository extends JpaRepository<BoardFileEntity, Long> {

    @Transactional
    @Query(value = "select * from board_file where board_id =:boardId" , nativeQuery = true)
    List<BoardFileEntity> findImgById( @Param("boardId") Long boardId);

}
