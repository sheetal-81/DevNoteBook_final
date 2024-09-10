package com.example.crudapp.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.crudapp.service.FileService;

import java.io.IOException;

@Controller
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    // Updated to accept an optional search parameter
    @GetMapping
    public String listFiles(@RequestParam(value = "search", required = false) String search, Model model) {
        model.addAttribute("files", fileService.searchFilesByName(search));
        return "file_list";
    }

    @PostMapping("/upload")
public String uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("ownerName") String ownerName) {
    try {
        fileService.saveFile(file, ownerName);
    } catch (IOException e) {
        // Log the exception and provide feedback to the user
        e.printStackTrace();
        return "error"; // You may want to return an error view or message
    }
    return "redirect:/files";
}


    @PostMapping("/delete")
    public String deleteFile(@RequestParam("id") Long id) {
        fileService.deleteFile(id);
        return "redirect:/files";
    }

    @GetMapping("/view/{id}")
public ResponseEntity<byte[]> viewFile(@PathVariable("id") Long id) throws IOException {
    byte[] fileContent = fileService.getFileContent(id);
    return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_PDF)
            .body(fileContent);
}

}
