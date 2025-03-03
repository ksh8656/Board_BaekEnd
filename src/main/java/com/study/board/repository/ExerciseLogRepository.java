package com.study.board.repository;

import com.study.board.entity.ExerciseLog;
import com.study.board.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseLogRepository extends JpaRepository<ExerciseLog, Long> {

    // ✅ 특정 사용자의 운동 기록 조회
    List<ExerciseLog> findByUser(User user);

    @Query("SELECT e.exerciseType, AVG(e.weight) " +
            "FROM ExerciseLog e " +
            "JOIN User u ON e.user = u.email " +  // ✅ User 테이블과 JOIN
            "WHERE u.rank = :userRank " +  // ✅ 같은 rank를 가진 사용자만 조회
            "GROUP BY e.exerciseType")
    List<Object[]> findAverageWeightByRank(@Param("userRank") String userRank);
}

