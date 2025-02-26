package com.study.board.repository;

import com.study.board.dto.response.ResBoardListDto;
import com.study.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("SELECT b, u.rank FROM Board b JOIN User u ON b.writer = u.email")
    Page<Object[]> findAllWithRank(Pageable pageable);
}




