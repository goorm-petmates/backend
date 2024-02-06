package kr.co.petmates.api.bussiness.pet.service;

import kr.co.petmates.api.bussiness.pet.entity.Pet;
import kr.co.petmates.api.bussiness.pet.entity.PetPhoto;
import kr.co.petmates.api.bussiness.pet.repository.PetPhotoRepository;
import kr.co.petmates.api.common.fileupload.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PetPhotoService {

    private final PetPhotoRepository petPhotoRepository;
    private final FileUploadService fileUploadService;

    public ResponseEntity<?> addPhoto(Long petId, MultipartFile photo) {
        String storedFileName = fileUploadService.storeFile(photo); // 파일 저장

        PetPhoto petPhoto = new PetPhoto();
        petPhoto.setStoredFileName(storedFileName);
        petPhoto.setPet(new Pet(petId)); // Pet 엔티티 설정

        petPhotoRepository.save(petPhoto); // 변환된 PetPhoto 엔티티를 데이터베이스에 저장

        String fileDownloadUri = "/uploads/" + storedFileName; // 파일 다운로드 URI 생성

        return ResponseEntity.ok().body(fileDownloadUri);
    }

}
