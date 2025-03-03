package com.study.board.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReqBoardUpdateDto {
    private String title;
    private String content;
    private String exerciseType;
    private Integer weight;
}


