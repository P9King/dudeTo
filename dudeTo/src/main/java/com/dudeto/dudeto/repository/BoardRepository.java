package com.dudeto.dudeto.repository;

import com.dudeto.dudeto.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.PrePersist;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

    //특정 게시글을 보기 위한 쿼리
    @Query(value = "select * from board where board_id =:#{#board.boardId} ", nativeQuery = true)
    List<BoardEntity> lookInBoard(@Param("board") BoardEntity boardEntity);

    //페이징 조건에 맞게 데이터 베이스 꺼내오는 쿼리
    @Transactional
    @Query(value = "select * from board order by create_time desc limit :limit offset :offset", nativeQuery = true)//SELECT * FROM board LIMIT 5 OFFSET 0
    List<BoardEntity> findPagedBoard(@Param("limit") int limit, @Param("offset") int offset);

    @Transactional
    @Query(value = "select * from board order by create_time desc limit 1" ,nativeQuery = true)
    BoardEntity boardIdTheLatestOne(BoardEntity boardEntity);
}
