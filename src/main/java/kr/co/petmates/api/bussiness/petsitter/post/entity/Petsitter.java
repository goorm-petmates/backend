package kr.co.petmates.api.bussiness.petsitter.post.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kr.co.petmates.api.bussiness.members.entity.Members;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Builder
@AllArgsConstructor
@DynamicUpdate
@JsonIgnoreProperties({ "pwd" })
@Getter
@Setter
@ToString
public class Petsitter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private double standardPrice;

    @Column(nullable = false)
    private double addPrice;

    @Column(nullable = false)
    private double nightPrice;

    @Column
    private String photo1;

    @Column
    private String photo2;

    @Column
    private String photo3;

    @Column
    private String photo4;

    @Column
    private String photo5;

    public Petsitter() {
    }
}
