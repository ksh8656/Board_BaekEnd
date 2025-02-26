package com.study.board.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReqUserDto {
    private String email;
    private String password;
    private String username;
    private int height;
    private int weight;
    private int benchMax;
    private int deadliftMax;
    private int squatMax;
}