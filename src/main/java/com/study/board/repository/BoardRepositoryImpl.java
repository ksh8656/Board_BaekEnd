package com.study.board.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.board.entity.Board;
import com.study.board.entity.QBoard;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository  // ✅ 스프링이 인식할 수 있도록 추가
@RequiredArgsConstructor  // ✅ JPAQueryFactory 자동 주입
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;  // ✅ 자동으로 주입됨

    @Override
    public Page<Board> searchBoards(String title, String content, String writerName, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        QBoard board = QBoard.board;

        if (title != null && !title.isEmpty()) {
            builder.and(board.title.containsIgnoreCase(title));
        }
        if (content != null && !content.isEmpty()) {
            builder.and(board.content.containsIgnoreCase(content));
        }
        if (writerName != null && !writerName.isEmpty()) {
            builder.and(board.writer.containsIgnoreCase(writerName));
        }

        // QueryDSL 검색 쿼리 실행
        List<Board> results = queryFactory
                .selectFrom(board)
                .where(builder)
                .orderBy(board.id.desc())  // ✅ 오류 없이 id 필드 사용 가능
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(board)
                .where(builder)
                .fetchCount();

        return PageableExecutionUtils.getPage(results, pageable, () -> total);
    }
}


