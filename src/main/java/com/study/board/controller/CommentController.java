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
import java.util.Map;
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
            @RequestBody ReqCommentDto reqCommentDto, // ✅ 프론트에서 보낸 요청 데이터 받기
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

}


