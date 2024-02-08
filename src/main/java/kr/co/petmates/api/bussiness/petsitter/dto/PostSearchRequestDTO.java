package kr.co.petmates.api.bussiness.petsitter.dto;

import kr.co.petmates.api.bussiness.petsitter.entity.Petsitter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostSearchRequestDTO {

    private String careType;
    private String area1;
    private String area2;

    public PostSearchRequestDTO(Petsitter petsitter) {
        this.careType = String.valueOf(petsitter.getCareType());
        this.area1 = petsitter.getArea1();
        this.area2 = petsitter.getArea2();
    }
}
