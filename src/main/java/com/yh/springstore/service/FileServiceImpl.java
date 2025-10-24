package com.yh.springstore.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        // Get current file name and extension
        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.')); // eg. ".png"

        // Generate new filename using UUID
        String randomUUID = UUID.randomUUID().toString();
        String fileName = randomUUID + fileExtension;
        String filePath = path + File.separator + fileName; // Use of "File.separator" for OS compatibility

        // Check if path exists and create
        File folder = new File(path);
        if (!folder.exists())
            folder.mkdirs();

        // Upload file to server on the created Path
        Files.copy(file.getInputStream(), Path.of(filePath));

        return fileName;
    }
}
