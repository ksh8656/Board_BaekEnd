package com.study.board.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false) // ✅ Board와 관계 설정
    private Board board;

    @ManyToOne
    @JoinColumn(name = "user_email", nullable = false) // ✅ User와 관계 설정
    private User writer;

    @Column(nullable = false, length = 500)
    private String content;

    private LocalDateTime createdAt = LocalDateTime.now();

    public void updateContent(String newContent) {
        this.content = newContent;
    }
}

