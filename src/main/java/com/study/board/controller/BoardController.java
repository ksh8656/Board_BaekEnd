package com.study.board.controller;

import com.study.board.dto.request.ReqBoardDto;
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

        System.out.println("Received JWT Token: " + authHeader);

        // ✅ JWT 토큰이 존재하는지 검증
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
        }

        // ✅ 토큰에서 이메일 추출
        String token = authHeader.replace("Bearer ", "");
        String userEmail = jwtUtil.extractEmail(token);
        System.out.println("Extracted User Email: " + userEmail);

        if (userEmail == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰 검증 실패.");
        }

        // ✅ 요청 본문에서 writerEmail 가져오기
        String writerEmail = reqBoardDto.getWriterEmail();
        System.out.println("Request Body Writer Email: " + writerEmail);

        // ✅ JWT에서 추출한 이메일과 요청된 writerEmail이 다르면 오류 반환
        if (!userEmail.equals(writerEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("토큰 정보와 작성자 이메일이 일치하지 않습니다.");
        }

        // ✅ 게시글 + 파일을 함께 저장
        ResBoardDto boardDto = boardService.createBoardWithFiles(reqBoardDto, userEmail, files);

        System.out.println("📌 생성된 boardDto의 boardId: " + boardDto.getId());

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

}

