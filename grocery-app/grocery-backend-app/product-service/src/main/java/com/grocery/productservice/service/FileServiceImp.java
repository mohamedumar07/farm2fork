package com.grocery.productservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileServiceImp implements FileService {

    @Override
    public String uploadFile(String path, MultipartFile file) throws IOException {

        //name of the file from the multipart
        String fileName = file.getOriginalFilename();

        // to get the file path
        String filePath = path + File.separator + fileName;

        //create file object
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
        }

        //upload the file in the access
        try (InputStream is = file.getInputStream()) {
                Files.copy(is, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
        }

        return fileName;
    }

    @Override
    public InputStream getResourceFile(String path, String fileName) throws FileNotFoundException {

        String filePath = path + File.separator + fileName;

        return new FileInputStream(filePath);
    }
}
