package com.study.board.controller;

import com.study.board.entity.FileData;
import com.study.board.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {
    private final FileRepository fileRepository;

    // ✅ 파일 다운로드 API
    @GetMapping("/{fileId}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long fileId) {
        FileData fileData = fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("파일을 찾을 수 없습니다."));

        // ✅ 파일 확장자에 따라 Content-Type 설정
        MediaType mediaType = getMediaTypeForFileName(fileData.getFileName());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, isImage(fileData.getFileName()) ?
                        "inline; filename=\"" + fileData.getFileName() + "\"" :
                        "attachment; filename=\"" + fileData.getFileName() + "\"") // ✅ 이미지라면 inline, 아니면 다운로드
                .contentType(mediaType)
                .body(fileData.getFileData());
    }

    // ✅ 이미지 파일인지 확인하는 메서드
    private boolean isImage(String fileName) {
        return fileName.matches(".*\\.(jpeg|jpg|png|gif)$");
    }

    // ✅ 파일 확장자에 맞는 MIME 타입 반환
    private MediaType getMediaTypeForFileName(String fileName) {
        if (fileName.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG;
        } else if (fileName.endsWith(".gif")) {
            return MediaType.IMAGE_GIF;
        } else {
            return MediaType.APPLICATION_OCTET_STREAM; // 기타 파일은 다운로드됨
        }
    }



}
