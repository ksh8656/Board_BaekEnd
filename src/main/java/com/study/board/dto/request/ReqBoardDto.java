package com.study.board.dto.request;

import com.study.board.entity.Board;
import com.study.board.entity.ExerciseLog;
import com.study.board.entity.ExerciseType;
import com.study.board.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ReqBoardDto {
    private String title;
    private String content;
    private ExerciseType exerciseType;
    private int weight;
    private String writerEmail; // ✅ 추가: 프론트에서 작성자 이메일을 받음

    // 🛠 DTO → Board 엔티티 변환 메서드 수정
    public Board toEntity(User user) {
        Board board = Board.builder()
                .title(this.title)
                .content(this.content)
                .writer(this.writerEmail) // ✅ writer 설정 (여기서 확인 필요)
                .createdAt(LocalDateTime.now())
                .build();


        return board;
    }

    // 🛠 DTO → ExerciseLog 엔티티 변환 메서드 수정
    public ExerciseLog toExerciseLog(User user, Board board) {
        return ExerciseLog.builder()
                .exerciseType(this.exerciseType) // ✅ 필드명 수정
                .weight(this.weight)
                .user(user.getEmail())
                .board(board)
                .build();
    }

}
