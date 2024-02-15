package kr.co.petmates.api.bussiness.petsitter.service;

import java.util.List;
import java.util.Optional;
import kr.co.petmates.api.bussiness.petsitter.dto.PetsitterDto;
import kr.co.petmates.api.bussiness.petsitter.dto.PetsitterDto.PetsitterProjection;
import kr.co.petmates.api.bussiness.petsitter.entity.Petsitter;
import kr.co.petmates.api.bussiness.petsitter.repository.PetsitterPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor


public class PetsitterPostService {
    private final PetsitterPostRepository petsitterPostRepository;

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

    // 펫시터 게시글 리스트 조회
    public List<PetsitterProjection> findAllPetsittersWithMemberId() {
        return petsitterPostRepository.findAllProjectedBy();
    }
}
