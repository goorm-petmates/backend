package kr.co.petmates.api.bussiness.petsitter.controller;

import java.util.List;
import java.util.Optional;
import kr.co.petmates.api.bussiness.petsitter.dto.PostSearchRequestDTO;
import kr.co.petmates.api.bussiness.petsitter.entity.Petsitter;
import kr.co.petmates.api.bussiness.petsitter.repository.PetsitterPostRepository;
import kr.co.petmates.api.bussiness.review.entity.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/petsitter")
public class PostController {

    @Autowired
    private PetsitterPostRepository petsitterPostRepository;

    @GetMapping("/posting-list")
    public List<Petsitter> getPostingList() {
        return petsitterPostRepository.findAll();
    }

    @PostMapping("/search")
    public List<Petsitter> searchPetSitters(@RequestBody PostSearchRequestDTO searchRequestDTO) {

        // careType, area1, area2 값을 사용하여 검색 로직 수행
        return petsitterPostRepository.findByCareTypeAndArea1AndArea2(
                searchRequestDTO.getCareType(),
                searchRequestDTO.getArea1(),
                searchRequestDTO.getArea2()
        );
    }

    @GetMapping("/apply")
    public String clickApply(Model model) {



        return "apply";
    }

    @PostMapping("/apply")
    public String saveApply() {
        // 파일 업로드 로직 구현해야함.


        // PetSitter 엔티티에 데이터 저장

        return "redirect:/apply";
    }

    // 파일 저장 로직을 구현하는 메서드
    private String savePhoto(MultipartFile file) {
        // 실제로는 파일 저장 로직을 여기에 구현 해야 합니다.
        // 파일을 서버에 저장하고, 저장된 파일의 경로 또는 파일명을 반환합니다.
        // 예: file.transferTo(new File("경로/파일명"));
        return "경로/파일명";
    }

    @PostMapping("/apply/apply-photo")
    public String savePhoto() {

        return "Apply with photo successful";
    }

    @PostMapping("/posting/{id}/bringup")
    public ResponseEntity<String> bringUp(@PathVariable Long id) {
        petsitterPostRepository.bringUp(id);
        return ResponseEntity.ok("끌어올리기 성공 하였습니다.");
    }

    @GetMapping("/posting/{id}")
    public Optional<Petsitter> getPostById(@PathVariable(name = "id") Long id) {
        return petsitterPostRepository.findById(id);
    }

    @GetMapping("/reviews")
    public ResponseEntity<List<Review>> getReviews(@RequestParam Long petsitter) {
        List<Review> reviews = petsitterPostRepository.findByPetsitter(petsitter);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/select-pet")
    public String checkPet() {

        return "select-pet";
    }

    @PostMapping("/reserve")
    public String reserve() {

        return "reserve";
    }

}
