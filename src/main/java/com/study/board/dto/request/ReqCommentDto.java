package com.study.board.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReqCommentDto {
    private String content; // ✅ 프론트에서 보낸 댓글 내용만 포함
}
