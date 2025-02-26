package com.study.board.dto.response;

import com.study.board.dto.ExerciseLogDto;
import com.study.board.dto.FileDataDto;
import com.study.board.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResDetailDto {
    private Long id;
    private String title;
    private String content;
    private String writer;
    private LocalDateTime createdAt;
    private int likeCount;
    private List<FileDataDto> files;  // ✅ 파일 정보 추가
    private ExerciseLogDto exerciseLog; // ✅ 운동 기록 정보 추가

    public ResDetailDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.writer = board.getWriter();
        this.createdAt = board.getCreatedAt();
        this.likeCount = board.getLikeCount();

        // ✅ 파일 리스트 변환
        this.files = board.getFiles().stream()
                .map(FileDataDto::new)
                .collect(Collectors.toList());

        // ✅ 운동 기록 변환 (운동 기록이 있을 경우만)
        if (board.getExerciseLog() != null) {
            this.exerciseLog = new ExerciseLogDto(board.getExerciseLog());
        }
    }
}


