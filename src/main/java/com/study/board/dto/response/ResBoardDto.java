package com.study.board.dto.response;

import com.study.board.entity.Board;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ResBoardDto {
    private Long id;
    private String title;
    private String content;
    private String writer; // 유저 닉네임
    private LocalDateTime createdAt;

    public ResBoardDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.writer = board.getWriter();
        this.createdAt = board.getCreatedAt();
    }

    // 🛠 엔티티 → DTO 변환 메서드 추가
    public static ResBoardDto fromEntity(Board board) {
        return new ResBoardDto(board);
    }
}


