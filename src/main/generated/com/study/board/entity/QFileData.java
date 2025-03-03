package com.study.board.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFileData is a Querydsl query type for FileData
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFileData extends EntityPathBase<FileData> {

    private static final long serialVersionUID = -720617289L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFileData fileData1 = new QFileData("fileData1");

    public final QBoard board;

    public final ArrayPath<byte[], Byte> fileData = createArray("fileData", byte[].class);

    public final StringPath fileName = createString("fileName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QFileData(String variable) {
        this(FileData.class, forVariable(variable), INITS);
    }

    public QFileData(Path<? extends FileData> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFileData(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFileData(PathMetadata metadata, PathInits inits) {
        this(FileData.class, metadata, inits);
    }

    public QFileData(Class<? extends FileData> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new QBoard(forProperty("board"), inits.get("board")) : null;
    }

}

