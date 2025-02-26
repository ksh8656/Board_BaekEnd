package com.study.board.repository;

import com.study.board.entity.ExerciseLog;
import com.study.board.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseLogRepository extends JpaRepository<ExerciseLog, Long> {

    // ✅ 특정 사용자의 운동 기록 조회
    List<ExerciseLog> findByUser(User user);
}

