package com.study.board.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -1122479684L;

    public static final QUser user = new QUser("user");

    public final NumberPath<Integer> benchMax = createNumber("benchMax", Integer.class);

    public final ListPath<Board, QBoard> boards = this.<Board, QBoard>createList("boards", Board.class, QBoard.class, PathInits.DIRECT2);

    public final NumberPath<Integer> deadliftMax = createNumber("deadliftMax", Integer.class);

    public final StringPath email = createString("email");

    public final NumberPath<Integer> height = createNumber("height", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath password = createString("password");

    public final StringPath rank = createString("rank");

    public final NumberPath<Double> score = createNumber("score", Double.class);

    public final NumberPath<Integer> squatMax = createNumber("squatMax", Integer.class);

    public final StringPath username = createString("username");

    public final NumberPath<Integer> weight = createNumber("weight", Integer.class);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

