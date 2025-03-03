package com.study.board.dto;

import com.study.board.entity.ExerciseType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AvgExerciseLogDto {
    private ExerciseType exerciseType;
    private double averageWeight; // ✅ 평균 무게

}

