package com.example.crudapp.service;

import com.example.crudapp.model.FileEntity;
import com.example.crudapp.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    private final String UPLOAD_DIR = "uploads/";

    // Save file to the system
    public void saveFile(MultipartFile file, String ownerName) throws IOException {
        String fileName = file.getOriginalFilename();
        Path path = Paths.get(UPLOAD_DIR + fileName);
        Files.write(path, file.getBytes());

        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileName(fileName);
        fileEntity.setOwnerName(ownerName);
        fileEntity.setFilePath(path.toString());

        fileRepository.save(fileEntity);
    }

    // Retrieve all files
    public List<FileEntity> getAllFiles() {
        return fileRepository.findAll();
    }

    // Delete a file
    public void deleteFile(Long id) {
        fileRepository.findById(id).ifPresent(file -> {
            new File(file.getFilePath()).delete();
            fileRepository.delete(file);
        });
    }

    // Get file content for viewing
    public byte[] getFileContent(Long id) throws IOException {
        FileEntity fileEntity = fileRepository.findById(id)
            .orElseThrow(() -> new IOException("File not found"));
        Path path = Paths.get(fileEntity.getFilePath());
        return Files.readAllBytes(path);
    }

    // Search files by name
    public List<FileEntity> searchFilesByName(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return getAllFiles(); // Return all files if no search term is provided
        }
        return fileRepository.findByFileNameContainingIgnoreCase(fileName);
    }
}
