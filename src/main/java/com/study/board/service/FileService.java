package com.study.board.service;

import com.study.board.entity.Board;
import com.study.board.entity.FileData;
import com.study.board.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;

    // ✅ 파일을 DB(BLOB)로 저장하는 메서드
    public void saveFiles(List<MultipartFile> files, Board board) throws IOException {
        if (files == null || files.isEmpty()) {
            System.out.println("🚨 [FileService] 저장할 파일이 없습니다!");
            return;
        }

        System.out.println("✅ [FileService] 저장할 파일 개수: " + files.size());

        for (MultipartFile file : files) {
            System.out.println("📂 [FileService] 저장 중: " + file.getOriginalFilename());
            System.out.println("📏 [FileService] 파일 크기: " + file.getSize() + " bytes");

            // ✅ 파일 크기 제한 (5MB 초과 시 예외 발생)
            if (file.getSize() > 5 * 1024 * 1024) {
                System.out.println("❌ [FileService] 파일 크기 초과: " + file.getOriginalFilename());
                throw new IllegalArgumentException("파일 크기가 5MB를 초과할 수 없습니다.");
            }

            // ✅ 파일을 BLOB 필드에 저장
            FileData fileData = FileData.builder()
                    .fileName(file.getOriginalFilename())
                    .fileData(file.getBytes()) // ✅ 파일 내용을 BLOB으로 저장
                    .board(board)
                    .build();

            FileData savedFile = fileRepository.save(fileData);
            System.out.println("✅ [FileService] 파일 저장 완료! 파일 ID: " + savedFile.getId());
        }

        System.out.println("✅ [FileService] 모든 파일 저장이 완료되었습니다!");
    }
}






