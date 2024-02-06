package kr.co.petmates.api.common.fileupload;

import kr.co.petmates.api.enums.UserInterfaceMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileUploadService {

    private static final long MAX_FILE_SIZE = 1 * 1024 * 1024; // 1MB 제한
    private static final String[] ALLOWED_EXT = {"png", "jpg", "jpeg"}; // 허용된 파일 확장자

    private final Path rootLocation;

    @Autowired
    public FileUploadService(FileStorageConfig fileStorageConfig) {
        this.rootLocation = fileStorageConfig.getRootLocation();
    }

    public String storeFile(MultipartFile file) {
        // 파일 유효성 검사
        validateFile(file);

        String extension = getExtension(file.getOriginalFilename());
        String storedFileName = UUID.randomUUID() + "." + extension; // 고유한 파일 이름 생성

        try {
            Files.createDirectories(this.rootLocation); // 디렉토리가 없으면 생성
            Path destinationFile = this.rootLocation.resolve(Paths.get(storedFileName)).normalize().toAbsolutePath();
            file.transferTo(destinationFile); // 파일 저장

            return storedFileName; // 저장된 파일 이름 반환
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, UserInterfaceMsg.ERR_UPLOAD_FAILED.getValue());
        }
    }

    private void validateFile(MultipartFile file) {
        String extension = getExtension(file.getOriginalFilename());
        if (!isAllowedExtension(extension)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, UserInterfaceMsg.ERR_UPLOAD_NOT_VALID_EXT.getValue());
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, UserInterfaceMsg.ERR_UPLOAD_OVER_SIZE.getValue());
        }
    }

    private boolean isAllowedExtension(String extension) {
        for (String ext : ALLOWED_EXT) {
            if (ext.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    private String getExtension(String storedFileName) {
        return storedFileName.substring(storedFileName.lastIndexOf(".") + 1);
    }
}
