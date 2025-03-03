package com.study.board.entity;

public enum ExerciseType {
    SQUAT, DEADLIFT, BENCHPRESS, NOT;

    public static ExerciseType fromString(String value) {
        for (ExerciseType type : ExerciseType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        return NOT; // 기본값
    }
}

