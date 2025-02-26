package com.study.board.service;

import com.study.board.entity.Board;
import com.study.board.entity.Comment;
import com.study.board.entity.User;
import com.study.board.repository.BoardRepository;
import com.study.board.repository.CommentRepository;
import com.study.board.repository.UserRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public Comment saveComment(Long boardId, String userEmail, String content) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Comment comment = new Comment();
        comment.setBoard(board);
        comment.setWriter(user);
        comment.setContent(content);

        return commentRepository.save(comment);
    }


    // ✅ 페이지네이션 적용된 댓글 목록 조회
    public Page<Comment> getCommentsByBoardId(Long boardId, Pageable pageable) {
        return commentRepository.findByBoardId(boardId, pageable);
    }
}


