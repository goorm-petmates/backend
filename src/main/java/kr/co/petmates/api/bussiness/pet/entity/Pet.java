package kr.co.petmates.api.bussiness.pet.entity;

import com.nimbusds.openid.connect.sdk.claims.Gender;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import kr.co.petmates.api.bussiness.members.entity.Members;
import kr.co.petmates.api.bussiness.pet.dto.PetDto;
import kr.co.petmates.api.common.entity.BaseDateTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "PET")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Getter
@Setter
@ToString

public class Pet extends BaseDateTimeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id; // 시퀀스

    @Column(nullable = false, length = 100)
    private String name; // 이름

    @Column(nullable = false)
    private String breed; // 종

    @Column(nullable = false, length = 4)
    private String birthYear; // 출생년도

    @Column(nullable = false)
    private String weight; // 몸무게

    @Column(nullable = false)
    private Gender sex; // 성별

    @Column(nullable = false)
    private boolean isNeutering; // 중성화

    @Column(nullable = false)
    private boolean isAllergy; // 알러지

    @Column(nullable = false)
    private boolean isDisease; // 질병

    @Column(columnDefinition = "LONGTEXT")
    private String ect; // 참고사항

    public static Pet toPetEntity(PetDto petDto) { // dto -> entity
        Pet pet = new Pet();
        pet.setName(petDto.getName());
        pet.setBreed(petDto.getBreed());
        pet.setBirthYear(petDto.getBirthYear());
        pet.setWeight(petDto.getWeight());
        pet.setSex(petDto.getSex());
        pet.setEct(petDto.getEct());
        return pet;
    }

    public Pet(Long id) {
        this.id = id;
    }

    // 펫과 주인 연결
    @Getter
    @ManyToOne
    @JoinColumn(name = "members_id")
    private Members owner;
    public void setOwner(Members owner) {
        this.owner = owner;
    }

    // 펫과 '펫 사진' 연결 (양방향)
    @OneToOne(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private PetPhoto petPhoto;

    // 펫과 '예약된 펫' 연결
    @OneToMany(mappedBy = "pet", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookedPet> bookedPet = new HashSet<>();
}

