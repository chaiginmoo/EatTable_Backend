package com.lec.spring.service;

import com.lec.spring.domain.Partner;
import com.lec.spring.domain.PartnerAttachment;
import com.lec.spring.domain.PartnerReqState;
import com.lec.spring.domain.User;
import com.lec.spring.repository.PartnerAttachmentRepository;
import com.lec.spring.repository.PartnerRepository;
import com.lec.spring.util.U;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class PartnerService {


    private final PartnerAttachmentRepository partnerAttachmentRepository;
    private final PartnerRepository partnerRepository;
    private final S3Service s3Service;



    //매장리스트
    @Transactional
    public List<Partner> totallist() {

            return partnerRepository.findAll();

    }


    //매장리스트
    @Transactional
    public Page<Partner> list(String keyword, Pageable pageable) {
        if (keyword != null && !keyword.isEmpty()) {
            return partnerRepository.findByKeyword(keyword, pageable);
        }else {
            return partnerRepository.findAll(pageable);
        }
    }

    //매장등록

    @Transactional
    public Partner write(Partner partner) {
        return partnerRepository.save(partner);
    }

    //매장디테일 정보
    @Transactional
    public Partner detail(Long id){
        return partnerRepository.findById(id).orElse(null);
    }


    //매장정보 수정  ( 이미지추후 추가, 고민중)
    @Transactional
    public Partner update(Partner partner, List<MultipartFile> files) {
        // 파트너 ID로 해당 파트너를 찾아옴
        Partner partnerUpdate = partnerRepository.findById(partner.getId())
                .orElseThrow(() -> new IllegalArgumentException("Partner not found with id: " + partner.getId()));

        // 파일 저장 로직 추가
        for (MultipartFile file : files) {
            PartnerAttachment partnerAttachment = new PartnerAttachment();
            String s3StoragePath = s3Service.uploadFile(file);
            partnerAttachment.setImageUrl(s3StoragePath);
            partnerAttachment.setPartner(partner);
            partnerAttachment.setFilename(file.getOriginalFilename());
            partnerAttachment.setImage(true);
            partnerAttachmentRepository.save(partnerAttachment);
        }

        // 파트너 정보 업데이트
        partnerUpdate.setStoreName(partner.getStoreName());
        partnerUpdate.setPartnerName(partner.getPartnerName());
        partnerUpdate.setPartnerPhone(partner.getPartnerPhone());
        partnerUpdate.setStorePhone(partner.getStorePhone());
        partnerUpdate.setParking(partner.getParking());
        partnerUpdate.setReserveInfo(partner.getReserveInfo());
        partnerUpdate.setStoreInfo(partner.getStoreInfo());
        partnerUpdate.setDog(partner.getDog());
        partnerUpdate.setFavorite(partner.getFavorite());
        partnerUpdate.setCorkCharge(partner.getCorkCharge());
        partnerUpdate.setTableCnt(partner.getTableCnt());
        partnerUpdate.setOpenTime(partner.getOpenTime());

        // 업데이트된 파트너 정보를 반환
        return partnerUpdate;
    }


    //직접 삭제불사  삭제신청만가능 삭제신청은 partnerReq 에서
    @Transactional
    public String delete(Long id){
        Partner partnerDelete = partnerRepository.findById(id).orElse(null);
        if(partnerDelete != null){
            partnerRepository.deleteById(id);
            return "1";
        }
        return "0";
    }



}
