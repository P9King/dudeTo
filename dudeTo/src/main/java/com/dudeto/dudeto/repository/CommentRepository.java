package com.dudeto.dudeto.repository;

import com.dudeto.dudeto.entity.BoardEntity;
import com.dudeto.dudeto.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    @Query(value="select comment from comment where board_id =:#{#comment.boardId}", nativeQuery = true)
    List<CommentEntity> commentList(@Param("comment") BoardEntity boardEntity);


}
