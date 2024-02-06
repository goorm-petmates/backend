package kr.co.petmates.api.bussiness.pet.controller;

import kr.co.petmates.api.bussiness.pet.service.PetPhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/my-page/pet")
@RequiredArgsConstructor
public class PetPhotoController {
    private final PetPhotoService petPhotoService;

    @PostMapping("/{petId}/photo")
    public ResponseEntity<?> addPhoto(@PathVariable("petId") Long petId,
                                      @RequestParam("photo") MultipartFile photo) {
        return petPhotoService.addPhoto(petId, photo);
    }
}
