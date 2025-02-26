package com.study.board.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 2000)
    private String content;

    // ✅ User의 email을 FK로 참조하도록 수정
    @Column(nullable = false)
    private String writer;

    @CreationTimestamp
    private LocalDateTime createdAt;

    // ✅ 게시글 하나당 여러 개의 파일이 첨부될 수 있음
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FileData> files = new ArrayList<>();

    // ✅ 게시글 하나당 운동 기록 1개를 가지도록 설정
    @OneToOne(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private ExerciseLog exerciseLog;

    // ✅ 좋아요 수 추가 (기본값: 0)
    @Column(nullable = false)
    private int likeCount = 0;

    // ✅ 좋아요 증가 메서드 추가
    public void increaseLikeCount() {
        this.likeCount++;
    }
}



