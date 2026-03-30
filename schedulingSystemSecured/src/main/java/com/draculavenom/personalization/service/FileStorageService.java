package com.draculavenom.personalization.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${application.storage.location}")
    private String storageLocation;

    public String saveFile(MultipartFile file, String oldLogo) throws IOException {
        if (oldLogo !=null && !oldLogo.isEmpty() && !oldLogo.equals("SYSTEM_DEFAULT_CREAR_LOGO")) {
            try{
                String oldFileName = oldLogo.replace("/logos/", ""); 
                Path oldFilePath = Paths.get(storageLocation).toAbsolutePath().resolve(oldFileName).normalize();

                if (Files.exists(oldFilePath)) {
                    Files.delete(oldFilePath);
                }
            } catch (IOException e) {
                System.err.println("Error al eliminar el archivo antiguo: " + e.getMessage());
            }
        }

        String uniqueID = UUID.randomUUID().toString().substring(0, 8); 
        String extension = getFileExtension(file.getOriginalFilename());
        String fileName = uniqueID + extension;

        Path root = Paths.get(storageLocation).toAbsolutePath().normalize();
        if (!Files.exists(root)) {
            Files.createDirectories(root);
        }
        Files.copy(file.getInputStream(), root.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

        return "/logos/" + fileName;
        


    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) return ".png";
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
