package com.study.board.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName; // ✅ 원본 파일명

    @Lob
    @Column(nullable = false, columnDefinition = "LONGBLOB")
    private byte[] fileData; // ✅ 파일 데이터 저장 (BLOB 필드)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;
}




