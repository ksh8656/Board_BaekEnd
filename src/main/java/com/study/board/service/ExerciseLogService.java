package com.study.board.service;

import com.study.board.dto.AvgExerciseLogDto;
import com.study.board.entity.ExerciseType;
import com.study.board.entity.User;
import com.study.board.repository.ExerciseLogRepository;
import com.study.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExerciseLogService {

    private final ExerciseLogRepository exerciseLogRepository;
    private final UserRepository userRepository;

    public List<AvgExerciseLogDto> getAverageWeightByRank(String userEmail) {
        // ✅ 현재 로그인한 사용자의 rank 가져오기
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        String userRank = user.getRank();  // ✅ 현재 사용자의 rank

        // ✅ 같은 rank를 가진 사람들의 운동 평균 무게 조회
        List<Object[]> results = exerciseLogRepository.findAverageWeightByRank(userRank);

        // ✅ DTO로 변환하여 반환
        return results.stream()
                .map(row -> new AvgExerciseLogDto((ExerciseType) row[0], (Double) row[1]))
                .collect(Collectors.toList());
    }
}


