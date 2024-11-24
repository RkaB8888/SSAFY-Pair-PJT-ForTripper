package com.travel.demo.database;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class TempFileStorageService {
    @Value("${app.temp-dir}")
    private String tempDir;

    public void saveFile(MultipartFile file) throws IOException {
        Path tempFile = Paths.get(tempDir, file.getOriginalFilename());
        Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("tempDir: " + tempDir);
    }
}