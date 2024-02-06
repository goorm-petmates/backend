package kr.co.petmates.api.bussiness.pet.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import kr.co.petmates.api.bussiness.pet.dto.PetPhotoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "PET_PHOTO")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Getter
@Setter
@ToString
public class PetPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String storedFileName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    public static PetPhoto toPetPhotoEntity(PetPhotoDto petPhotoDto) { // dto -> entity
        PetPhoto petPhoto = new PetPhoto();
        petPhoto.setStoredFileName(petPhotoDto.getStoredFileName());

        return petPhoto;
    }
}
