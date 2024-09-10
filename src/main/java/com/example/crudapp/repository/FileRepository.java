package com.example.crudapp.repository;

import com.example.crudapp.model.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    // Method to find files by name containing the search string, case-insensitive
    List<FileEntity> findByFileNameContainingIgnoreCase(String fileName);
}
