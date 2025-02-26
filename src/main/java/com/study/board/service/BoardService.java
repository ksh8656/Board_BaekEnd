package com.study.board.service;

import com.study.board.dto.request.ReqBoardDto;
import com.study.board.dto.response.ResBoardDto;
import com.study.board.dto.response.ResBoardListDto;
import com.study.board.dto.response.ResDetailDto;
import com.study.board.entity.Board;
import com.study.board.entity.ExerciseLog;
import com.study.board.entity.ExerciseType;
import com.study.board.entity.User;
import com.study.board.repository.BoardRepository;
import com.study.board.repository.ExerciseLogRepository;
import com.study.board.repository.UserRepository;
import com.study.board.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final ExerciseLogRepository exerciseLogRepository;
    private final FileService fileService;

    // 📌 페이징 처리 (한 페이지에 10개씩 기본 설정)
    public Page<ResBoardListDto> getAllBoards(Pageable pageable) {
        Pageable defaultPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize() > 0 ? pageable.getPageSize() : 10,
                Sort.by(Sort.Direction.DESC, "id") // 최신 게시글 순서로 정렬
        );

        // 데이터 가져오기 (Board + rank)
        Page<Object[]> boardPage = boardRepository.findAllWithRank(defaultPageable);

        // DTO 변환
        return boardPage.map(obj -> {
            Board board = (Board) obj[0];
            String rank = (String) obj[1];
            return ResBoardListDto.from(board, rank);
        });
    }

    // ✅ 게시글 저장 (운동 기록 + 파일 업로드 포함)
    @Transactional
    public ResBoardDto createBoardWithFiles(ReqBoardDto reqBoardDto, String userEmail,List<MultipartFile> files) throws IOException {
        // ✅ writerEmail을 이용해 User 조회 (작성자가 없는 경우 예외 발생 방지)
        User user = userRepository.findByEmail(reqBoardDto.getWriterEmail())
                .orElseThrow(() -> new RuntimeException("해당 이메일을 가진 사용자를 찾을 수 없습니다."));

        System.out.println("✅ BoardService - 조회된 User: " + user.getEmail()); // ✅ 디버깅 로그 추가

        // ✅ Board 엔티티 생성 (작성자 정보 포함)
        Board board = reqBoardDto.toEntity(user);
        board = boardRepository.save(board);

        // ✅ 운동 기록도 저장
        ExerciseLog exerciseLog = reqBoardDto.toExerciseLog(user, board);
        exerciseLogRepository.save(exerciseLog);

        // ✅ 파일 저장 (파일이 있는 경우)
        fileService.saveFiles(files, board);

        return ResBoardDto.fromEntity(board);
    }

    // ✅ 게시글 상세 조회 (DTO 변환)
    public ResDetailDto getBoardById(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다. ID: " + id));

        return new ResDetailDto(board); // ✅ ResDetailDto를 반환하도록 수정
    }


    // ✅ 좋아요 증가 메서드 추가
    @Transactional
    public void increaseLike(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다. ID: " + boardId));

        board.increaseLikeCount(); // ✅ 좋아요 증가
        boardRepository.save(board); // ✅ 변경 내용 저장
    }
}



