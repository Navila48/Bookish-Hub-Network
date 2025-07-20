package com.tasnuva.book.file;

import ch.qos.logback.core.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class FileUtils {

    public static byte[] readFileFromLocation(String fileUrl) {
        if(StringUtil.isNullOrEmpty(fileUrl)) {
            return null;
        }
        try{
            Path filePath = new File(fileUrl).toPath();
            return Files.readAllBytes(filePath);
        }catch(IOException exp){
            log.warn("Error reading file from location", exp);
        }
        return null;
    }
}
