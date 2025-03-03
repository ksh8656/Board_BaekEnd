package com.study.board.controller;

import com.study.board.dto.request.ReqBoardDto;
import com.study.board.dto.request.ReqBoardUpdateDto;
import com.study.board.dto.response.ResBoardDto;
import com.study.board.dto.response.ResBoardListDto;
import com.study.board.dto.response.ResDetailDto;
import com.study.board.entity.Board;
import com.study.board.security.JwtUtil;
import com.study.board.service.BoardService;
import com.study.board.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController //REST API 컨트롤러(JSON 데이터 반환)
@RequestMapping("/board") //기본 URL 설정
@RequiredArgsConstructor //Lombok이 final이 붙은 필드를 매개변수로 받는 생성자를 자동으로 만들어 줌
@Slf4j
public class BoardController {

    private final BoardService boardService;
    private final FileService fileService;
    private final JwtUtil jwtUtil;

    @GetMapping("/list")
    public ResponseEntity<Page<ResBoardListDto>> boardList(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ResBoardListDto> listDTO = boardService.getAllBoards(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(listDTO);
    }

    @PostMapping("/write")
    public ResponseEntity<?> createBoard(
            @RequestPart("board") ReqBoardDto reqBoardDto,  // ✅ 게시글 정보(JSON)
            @RequestPart(value = "files", required = false) List<MultipartFile> files, // ✅ 파일 리스트
            @RequestHeader(value = "Authorization", required = false) String authHeader) throws IOException {


        // ✅ JWT 토큰이 존재하는지 검증
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
        }

        // ✅ 토큰에서 이메일 추출
        String token = authHeader.replace("Bearer ", "");
        String userEmail = jwtUtil.extractEmail(token);

        if (userEmail == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰 검증 실패.");
        }

        // ✅ 요청 본문에서 writerEmail 가져오기
        String writerEmail = reqBoardDto.getWriterEmail();

        // ✅ JWT에서 추출한 이메일과 요청된 writerEmail이 다르면 오류 반환
        if (!userEmail.equals(writerEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("토큰 정보와 작성자 이메일이 일치하지 않습니다.");
        }

        // ✅ 게시글 + 파일을 함께 저장
        ResBoardDto boardDto = boardService.createBoardWithFiles(reqBoardDto, userEmail, files);


        return ResponseEntity.status(HttpStatus.CREATED).body(boardDto);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ResDetailDto> getBoardDetail(@PathVariable(name = "id") Long boardId) {
        ResDetailDto boardDto = boardService.getBoardById(boardId);
        return ResponseEntity.ok(boardDto);
    }


    // ✅ 좋아요 증가 API 추가
    @PutMapping("/{boardId}/like")
    public ResponseEntity<?> increaseLike(@PathVariable Long boardId) {
        boardService.increaseLike(boardId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{boardId}/update")
    public ResponseEntity<?> updateBoard(
            @PathVariable Long boardId,
            @RequestPart("board") ReqBoardUpdateDto requestDto,  // ✅ JSON 데이터 받음
            @RequestPart(value = "files", required = false) List<MultipartFile> files, // ✅ 파일 추가 가능
            @RequestHeader("Authorization") String authHeader
    ) {
        // ✅ 1. JWT 검증
        String token = authHeader.replace("Bearer ", "");
        String userEmail = jwtUtil.extractEmail(token);

        // ✅ 2. 게시글 존재 여부 확인
        Board board = boardService.getBoardEntityById(boardId);
        if (!board.getWriter().equals(userEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("게시글 수정 권한이 없습니다.");
        }

        // ✅ 3. 게시글 내용 + 파일 수정
        boardService.updateBoardWithFiles(board, requestDto, files);

        return ResponseEntity.ok("게시글이 수정되었습니다.");
    }

    @DeleteMapping("/{boardId}/delete")
    public ResponseEntity<?> deleteBoard(
            @PathVariable Long boardId,
            @RequestHeader("Authorization") String authHeader
    ) {
        // ✅ 1. JWT 검증
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
        }
        String token = authHeader.replace("Bearer ", "");
        String userEmail = jwtUtil.extractEmail(token);

        // ✅ 2. 게시글 존재 여부 확인
        Board board = boardService.getBoardEntityById(boardId);
        if (board == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("게시글을 찾을 수 없습니다.");
        }

        // ✅ 3. 삭제 권한 확인 (작성자만 삭제 가능)
        if (!board.getWriter().equals(userEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("게시글 삭제 권한이 없습니다.");
        }

        // ✅ 4. 게시글 삭제 (서비스 호출)
        boardService.deleteBoard(boardId);

        return ResponseEntity.ok("게시글이 삭제되었습니다.");
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ResBoardListDto>> searchBoards(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "writerName", required = false) String writerName,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<ResBoardListDto> searchResults = boardService.searchBoards(title, content, writerName, pageable);
        return ResponseEntity.ok(searchResults);
    }





}

