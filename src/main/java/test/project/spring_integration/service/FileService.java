package test.project.spring_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import test.project.spring_integration.config.AppConfigProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service
@RequiredArgsConstructor
public class FileService {

    private final AppConfigProperties properties;

    public void upload(MultipartFile multipartFile) {
        Path filePath = Paths.get("C:\\test");
        try {
            Files.write(filePath, multipartFile.getBytes(),StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
