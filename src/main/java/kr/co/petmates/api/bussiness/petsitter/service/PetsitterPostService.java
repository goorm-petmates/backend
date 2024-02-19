package kr.co.petmates.api.bussiness.petsitter.service;

import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kr.co.petmates.api.bussiness.members.entity.Members;
import kr.co.petmates.api.bussiness.members.repository.MembersRepository;
import kr.co.petmates.api.bussiness.pet.entity.Pet;
import kr.co.petmates.api.bussiness.petsitter.dto.PetsitterDto;
import kr.co.petmates.api.bussiness.petsitter.dto.PetsitterListDto;
import kr.co.petmates.api.bussiness.petsitter.entity.Petsitter;
import kr.co.petmates.api.bussiness.petsitter.repository.PetsitterPostRepository;
import kr.co.petmates.api.enums.CareType;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class PetsitterPostService {

    @Autowired
    private final PetsitterPostRepository petsitterPostRepository;

    @Autowired
    private final MembersRepository membersRepository;

    public boolean checkPostExistenceByMemberId(Long membersId) {
        return petsitterPostRepository.existsByMembersId(membersId);
    }

    // 펫시터 게시글의 존재 여부 및 펫시터 ID 확인
    public Optional<Long> findPetsitterIdByMembersId(Long membersId) {
        return petsitterPostRepository.findByMembersId(membersId)
                .map(Petsitter::getId); // 존재하는 경우, Petsitter의 ID 반환
    }

    // 펫시터 ID를 사용하여 펫시터 정보를 조회
    @Transactional(readOnly = true)
    public Optional<PetsitterDto> getContentsByPetsitterId(Long petsitterId) {
        return petsitterPostRepository.findById(petsitterId)
                .map(PetsitterDto::toPetsitterDto); // PetsitterDto로 변환

    }

    public List<PetsitterDto> findPetsittersWithMembers() {

        List<Petsitter> petsitters = petsitterPostRepository.findAll();
        return petsitters.stream()
                .map(PetsitterDto::toPetsitterDto)
                .collect(Collectors.toList());
    }

    public List<PetsitterDto> findSearch(CareType careType, String area1, String area2) {
        List<Petsitter> petsitters = petsitterPostRepository.findByCareTypeAndArea1AndArea2(careType, area1, area2);

        return petsitters.stream()
                .map(PetsitterDto::toPetsitterDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public Optional<PetsitterDto> getPetsitterById(Long id) {
        try {
//            Petsitter petsitter = petsitterPostRepository.findById(id).orElseThrow(
//                    () -> new EntityNotFoundException("Petsitter with id " + id + "not found"));
//
//            // 조회수 증가
//            petsitter.addVeiwCnt();
//
//            // 조회수 업데이트
//            return petsitterPostRepository.save(petsitter);
            Optional<Petsitter> optionalPetsitter = petsitterPostRepository.findById(id);

            if (optionalPetsitter.isPresent()) {
                Petsitter petsitter = optionalPetsitter.get();
                Hibernate.initialize((petsitter.getMembers()));

                // 조회수 증가
                petsitter.addViewCnt();

                return optionalPetsitter.map(PetsitterDto::toPetsitterDto);
            } else {
                return null;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public Petsitter applyAsPetsitter(PetsitterDto petsitterDto) {
        try {
            // PetsitterDto에서 필요한 정보 추출
            String title = petsitterDto.getTitle();
            String content = petsitterDto.getContent();
            BigDecimal standardPrice = petsitterDto.getStandardPrice();
            BigDecimal addPrice = petsitterDto.getAddPrice();
            BigDecimal nightPrice = petsitterDto.getNightPrice();
            CareType careType = petsitterDto.getCareType();
            String area1 = petsitterDto.getArea1();
            String area2 = petsitterDto.getArea2();
            // 삭제하고 기능을 추가해야함
            Integer averageRating = petsitterDto.getAverageRating();
            Boolean isBringUp = petsitterDto.getIsBringUp();
            Boolean isKakaoProfile = petsitterDto.getIsKakaoProfile();
            Integer viewCnt = petsitterDto.getViewCnt();
            Integer reviewCnt = petsitterDto.getReviewCnt();

            // 현재 로그인한 회원 정보를 가져와서 Petsitter 엔티티에 저장
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            Members members = membersRepository.findByEmail(email);

            Petsitter petsitter = new Petsitter();
            petsitter.setTitle(title);
            petsitter.setContent(content);
            petsitter.setStandardPrice(standardPrice);
            petsitter.setAddPrice(addPrice);
            petsitter.setNightPrice(nightPrice);
            petsitter.setCareType(careType);
            petsitter.setArea1(area1);
            petsitter.setArea2(area2);
            petsitter.setAverageRating(averageRating);
            petsitter.setIsBringUp(isBringUp);
            petsitter.setIsKakaoProfile(isKakaoProfile);
            petsitter.setViewCnt(viewCnt);
            petsitter.setReviewCnt(reviewCnt);

            petsitter.setMembers(members);

            // Petsitter 엔티티를 저장
            return petsitterPostRepository.save(petsitter);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("지원하기에 실패했습니다.", e);
        }
    }
//
//    public List<PetsitterListDto> getPetsitterWithMembers() {
//        return petsitterPostRepository.findPetsitterWithMembers();
//    }

}
