package com.study.board.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReqLoginDto {
    private String email;
    private String password;
    private String username;
}
