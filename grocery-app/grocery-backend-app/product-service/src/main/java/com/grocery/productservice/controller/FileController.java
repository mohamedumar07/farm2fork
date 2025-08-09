package com.grocery.productservice.controller;

import com.grocery.productservice.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/v1/file")
public class FileController {

    @Value("${project.poster}")
    private String filePath;

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String uploadFileName = fileService.uploadFile(filePath,file);
        return new ResponseEntity<>(uploadFileName, HttpStatus.OK);
    }

    @GetMapping("/{fileName}")
    public void getFile(@PathVariable("fileName") String fileName,
                                     HttpServletResponse response) throws IOException {
        InputStream resourceFile = fileService.getResourceFile(filePath,fileName);
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        StreamUtils.copy(resourceFile,response.getOutputStream());
    }
}
