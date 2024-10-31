package com.leadway_pensure.statement_generator.Services;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

@Service
public class FileDownloadService {

    public void DownloadFile(String fileName,String username,String base64String) {
        // Download file from network path
        String path = "//flsv/public/Statement_Folder/" + username + "/" + fileName;
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64String);
            Files.write(Paths.get(path), decodedBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
