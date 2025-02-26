package com.study.board.dto.response;

import com.study.board.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResBoardListDto {
    private Long id;
    private String title;
    private String writer;
    private String rank;  // ✅ rank 필드 추가
    private LocalDateTime createdAt;
    private int likeCount;

    public static ResBoardListDto from(Board board, String rank) {
        return new ResBoardListDto(
                board.getId(),
                board.getTitle(),
                board.getWriter() != null ? board.getWriter() : "알 수 없음", // ✅ 작성자 null 체크
                rank != null ? rank : "알 수 없음", // ✅ rank null 체크
                board.getCreatedAt(),
                board.getLikeCount() // ✅ 좋아요 수 추가
        );
    }
}



