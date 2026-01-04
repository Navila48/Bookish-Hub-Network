package com.tasnuva.book.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileStorageService {

    @Value("${application.file.upload.cover-photo-path}")
    private String fileUploadPath;
    public String saveFile(MultipartFile sourceFile, Integer userId) {
        String fileOutputSubPath = "users" + File.separator + userId;
        return uploadFile(sourceFile, fileOutputSubPath);
    }

    private String uploadFile(MultipartFile sourceFile, String fileOutputSubPath) {
        final String finalUploadPath = fileUploadPath + File.separator + fileOutputSubPath;
        File targetFolder = new File(finalUploadPath);

        if(!targetFolder.exists()){
            boolean targetFolderCreated = targetFolder.mkdirs();
            if(!targetFolderCreated){
                log.warn("Could not create folder " + targetFolder.getAbsolutePath());
                return null;
            }
        }
        final String fileExtension = getFileExtension(sourceFile.getOriginalFilename());
        String targetFilePath = finalUploadPath + File.separator + System.currentTimeMillis() +"."+ fileExtension;
        Path targetPath = Paths.get(targetFilePath);
        try{
            Files.write(targetPath, sourceFile.getBytes());
            log.info("File saved to " + targetFilePath);
            return targetFilePath;
        }catch(IOException e){
            log.warn("Could not save file " + targetFilePath);
        }
        return null;
    }

    private String getFileExtension(String fileName) {
        if(fileName == null || fileName.isEmpty()){
            return null;
        }
        int lastDotIndex = fileName.lastIndexOf(".");
        if(lastDotIndex == -1){
            return null;
        }
        return fileName.substring(lastDotIndex + 1).toLowerCase();
    }
}
