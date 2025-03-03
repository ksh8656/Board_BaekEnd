package com.study.board.controller;

import com.study.board.dto.AvgExerciseLogDto;
import com.study.board.service.ExerciseLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/exercise")
@RequiredArgsConstructor
public class ExerciseLogController {

    private final ExerciseLogService exerciseLogService;

    @GetMapping("/average-rank")
    public ResponseEntity<List<AvgExerciseLogDto>> getAverageWeightByRank(@RequestParam String userEmail) {
        List<AvgExerciseLogDto> averages = exerciseLogService.getAverageWeightByRank(userEmail);
        return ResponseEntity.ok(averages);
    }
}

