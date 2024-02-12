package com.lec.spring.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder
public class Partner extends BaseEntity{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String storeName;
    @Column(nullable = false)
    private String partnerName;
    @Column(nullable = false)
    private String partnerPhone;
    @Column(nullable = false)
    private String storePhone;

    @Embedded
    private Address address;

    @ColumnDefault(value = "0")
    private String tableCnt;

    private String openTime;

    private String storeInfo;

    private String reserveInfo;

    private String favorite;

    private String viewCnt;


//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private LocalDateTime regDate;

    @Enumerated(value = EnumType.STRING)
    private TrueFalse parking;


    @Enumerated(value = EnumType.STRING)
    private TrueFalse corkCharge;

    @Enumerated(value = EnumType.STRING)
    private TrueFalse  dog;

    @Enumerated(value = EnumType.STRING)
    private TrueFalse partnerState;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "partner")
    private List<PartnerAttachment> fileList = new ArrayList<>();



    @ManyToOne
    @ToString.Exclude
    @JsonIgnore
    @JoinColumn(name = "userId")
    private User user;



    @PrePersist
    public void prePersist() {
        if (partnerState == null) {
            partnerState = TrueFalse.TRUE;
        }
        if (fileList.isEmpty()) {
            PartnerAttachment defaultAttachment = new PartnerAttachment();
            defaultAttachment.setImageUrl("https://eatablebucket.s3.ap-northeast-2.amazonaws.com/1707717950973-eatabel-1.png");
            defaultAttachment.setFilename("default-image.png"); // 파일 이름 설정
            defaultAttachment.setPartner(this); // PartnerAttachment 객체에 현재 Partner 객체를 참조하도록 설정

            // 이미지 파일로 설정
            defaultAttachment.setImage(true);

            fileList.add(defaultAttachment);
        }
    }




}
