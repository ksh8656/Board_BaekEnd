package com.study.board.repository;

import com.study.board.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // ✅ 특정 게시글에 해당하는 댓글 목록 가져오기
    Page<Comment> findByBoardId(Long boardId, Pageable pageable);

    void deleteByBoardId(Long boardId);
}
