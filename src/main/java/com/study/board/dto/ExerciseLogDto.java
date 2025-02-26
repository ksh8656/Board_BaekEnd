package com.study.board.dto;

import com.study.board.entity.ExerciseLog;
import com.study.board.entity.ExerciseType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseLogDto {
    private ExerciseType exerciseType;
    private int weight;

    public ExerciseLogDto(ExerciseLog exerciseLog) {
        this.exerciseType = exerciseLog.getExerciseType();
        this.weight = exerciseLog.getWeight();
    }
}


