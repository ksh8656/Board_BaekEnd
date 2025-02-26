package com.study.board.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExerciseType exerciseType; // ✅ 운동 종류

    @Column(nullable = false)
    private int weight; // ✅ 운동 무게 (kg)

    @Column(nullable = false)
    private String user; // ✅ 사용자 이메일을 저장

    @OneToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;
}





