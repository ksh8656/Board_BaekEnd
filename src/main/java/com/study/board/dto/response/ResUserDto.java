package com.study.board.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ResUserDto {
    private String email;
    private String username;
    private int height;
    private int weight;
    private int score;
    private String rank;
}
