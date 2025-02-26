package com.study.board.repository;

import com.study.board.entity.FileData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileData, Long> {
}
