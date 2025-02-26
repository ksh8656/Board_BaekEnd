package com.study.board.dto.response;

import com.study.board.entity.Comment;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class ResCommentDto {
    private Long id;
    private String content;
    private String writerEmail;
    private LocalDateTime createdAt;
    private String rank;

    public ResCommentDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.writerEmail = comment.getWriter().getEmail();
        this.createdAt = comment.getCreatedAt();
        this.rank = comment.getWriter().getRank();
    }
}
