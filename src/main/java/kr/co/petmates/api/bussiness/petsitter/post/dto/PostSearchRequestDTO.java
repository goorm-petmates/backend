package kr.co.petmates.api.bussiness.petsitter.post.dto;

import jakarta.validation.constraints.NotNull;
import kr.co.petmates.api.bussiness.petsitter.post.entity.Post;
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

    public PostSearchRequestDTO(Post post) {
        this.careType = post.getCareType();
        this.area1 = post.getArea1();
        this.area2 = post.getArea2();
    }
}
