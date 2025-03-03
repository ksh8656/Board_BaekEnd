package com.study.board.controller;

import com.study.board.dto.request.ReqCommentDto;
import com.study.board.dto.response.ResCommentDto;
import com.study.board.entity.Comment;
import com.study.board.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.security.Principal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;


@RestController
@RequestMapping("/board/{boardId}/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // ✅ 댓글 작성 (로그인한 사용자만 가능)
    @PostMapping("/write")
    @PreAuthorize("isAuthenticated()") // ✅ 로그인한 사용자만 댓글 작성 가능
    public ResponseEntity<ResCommentDto> createComment(
            @PathVariable Long boardId,
            @RequestBody ReqCommentDto reqCommentDto,
            Principal principal) {

        String userEmail = principal.getName(); // ✅ 현재 로그인한 사용자 이메일 가져오기
        Comment savedComment = commentService.saveComment(boardId, userEmail, reqCommentDto.getContent());

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResCommentDto(savedComment));
    }

    // ✅ 댓글 목록 조회 (로그인한 사용자만 가능)
    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<ResCommentDto>> getComments(
            @PathVariable Long boardId, Pageable pageable) {

        Page<ResCommentDto> commentPage = commentService.getCommentsByBoardId(boardId, pageable)
                .map(ResCommentDto::new); // ✅ DTO 변환 적용

        return ResponseEntity.ok(commentPage);
    }

    // ✅ 댓글 수정 (로그인한 사용자만 가능, 본인 댓글만 수정 가능)
    @PatchMapping("/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> updateComment(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            @RequestBody ReqCommentDto reqDto,
            Principal principal) {

        String userEmail = principal.getName();
        commentService.updateComment(commentId, reqDto.getContent(), userEmail);
        return ResponseEntity.ok("댓글이 수정되었습니다!");
    }

    // ✅ 댓글 삭제 (로그인한 사용자만 가능, 본인 댓글만 삭제 가능)
    @DeleteMapping("/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteComment(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            Principal principal) {

        String userEmail = principal.getName();
        commentService.deleteComment(commentId, userEmail);
        return ResponseEntity.ok("댓글이 삭제되었습니다!");
    }
}



