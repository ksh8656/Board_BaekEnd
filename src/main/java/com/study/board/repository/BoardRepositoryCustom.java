package com.study.board.repository;

import com.study.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {
    Page<Board> searchBoards(String title, String content, String writerName, Pageable pageable);
}

