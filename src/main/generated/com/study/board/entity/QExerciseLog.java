package com.study.board.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QExerciseLog is a Querydsl query type for ExerciseLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QExerciseLog extends EntityPathBase<ExerciseLog> {

    private static final long serialVersionUID = 106230587L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QExerciseLog exerciseLog = new QExerciseLog("exerciseLog");

    public final QBoard board;

    public final EnumPath<ExerciseType> exerciseType = createEnum("exerciseType", ExerciseType.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath user = createString("user");

    public final NumberPath<Integer> weight = createNumber("weight", Integer.class);

    public QExerciseLog(String variable) {
        this(ExerciseLog.class, forVariable(variable), INITS);
    }

    public QExerciseLog(Path<? extends ExerciseLog> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QExerciseLog(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QExerciseLog(PathMetadata metadata, PathInits inits) {
        this(ExerciseLog.class, metadata, inits);
    }

    public QExerciseLog(Class<? extends ExerciseLog> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new QBoard(forProperty("board"), inits.get("board")) : null;
    }

}

