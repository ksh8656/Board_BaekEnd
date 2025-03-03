package com.study.board.service;

import com.study.board.dto.request.ReqBoardDto;
import com.study.board.dto.request.ReqBoardUpdateDto;
import com.study.board.dto.response.ResBoardDto;
import com.study.board.dto.response.ResBoardListDto;
import com.study.board.dto.response.ResDetailDto;
import com.study.board.entity.Board;
import com.study.board.entity.ExerciseLog;
import com.study.board.entity.ExerciseType;
import com.study.board.entity.User;
import com.study.board.repository.*;
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
    private final FileRepository fileRepository;
    private final CommentRepository commentRepository;

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

    @Transactional
    public void updateBoardWithFiles(Board board, ReqBoardUpdateDto requestDto, List<MultipartFile> files) {
        // ✅ 1. 제목 & 내용 업데이트
        board.setTitle(requestDto.getTitle());
        board.setContent(requestDto.getContent());

        // ✅ 2. 운동 기록 업데이트
        if (!ExerciseType.NOT.name().equals(requestDto.getExerciseType())) {  // "NOT"이 아닐 경우만 저장
            ExerciseLog exerciseLog = board.getExerciseLog();
            if (exerciseLog == null) {
                exerciseLog = new ExerciseLog();
                exerciseLog.setBoard(board);
            }
            exerciseLog.setExerciseType(ExerciseType.fromString(requestDto.getExerciseType()));  // String을 Enum으로 변환
            exerciseLog.setWeight(requestDto.getWeight());
            board.setExerciseLog(exerciseLog);
        } else {
            board.setExerciseLog(null);
        }

        // ✅ 3. 기존 파일 삭제
        fileRepository.deleteByBoardId(board.getId());

        // ✅ 4. 새 파일 저장 (FileService 방식 적용)
        if (files != null && !files.isEmpty()) {
            try {
                fileService.saveFiles(files, board);  // ✅ 변경된 파일 저장 방식 적용
            } catch (IOException e) {
                throw new RuntimeException("파일 저장 중 오류 발생", e);
            }
        }

        boardRepository.save(board);
    }



    public Board getBoardEntityById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
    }

    @Transactional
    public void deleteBoard(Long boardId) {
        // ✅ 1. 게시글 조회
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // ✅ 2. 게시글과 연관된 파일 삭제
        if (board.getFiles() != null) {
            fileRepository.deleteByBoardId(boardId);
        }

        // ✅ 3. 댓글 삭제 (연관된 댓글이 있다면 함께 삭제)
        commentRepository.deleteByBoardId(boardId);

        // ✅ 4. 게시글 삭제
        boardRepository.delete(board);
    }

    @Transactional(readOnly = true)
    public Page<ResBoardListDto> searchBoards(String title, String content, String writerName, Pageable pageable) {
        Page<Board> boardPage = boardRepository.searchBoards(title, content, writerName, pageable);

        // ✅ 검색된 결과를 DTO로 변환 (rank 값을 DB에서 조회하여 설정)
        return boardPage.map(board -> {
            String rank = getRankForUser(board.getWriter());
            return ResBoardListDto.from(board, rank);
        });
    }

    private String getRankForUser(String writerEmail) {
        if (writerEmail == null) {
            return "알 수 없음";
        }

        return userRepository.findByEmail(writerEmail)
                .map(User::getRank)
                .orElse("알 수 없음");  // rank가 없으면 기본값 반환
    }






}



