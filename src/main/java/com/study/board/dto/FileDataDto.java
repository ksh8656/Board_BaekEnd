package com.study.board.dto;

import com.study.board.entity.FileData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FileDataDto {
    private Long id;
    private String fileName;
    private String fileUrl;

    public FileDataDto(FileData fileData) {
        this.id = fileData.getId();
        this.fileName = fileData.getFileName();
        this.fileUrl = "/files/" + fileData.getId(); // ✅ 파일 다운로드 URL
    }
}

