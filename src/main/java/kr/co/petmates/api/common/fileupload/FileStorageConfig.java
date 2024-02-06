package kr.co.petmates.api.common.fileupload;

import jakarta.annotation.PostConstruct;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;


@Component
public class FileStorageConfig {

    @Value("${spring.servlet.multipart.location}")
    private String uploadDir;


    @Getter
    private Path rootLocation;

    @PostConstruct
    public void init() {
        rootLocation = Paths.get(uploadDir);
    }
}
