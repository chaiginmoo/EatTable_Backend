package com.lec.spring.service;

import com.lec.spring.domain.Partner;
import com.lec.spring.domain.PartnerMenu;
import com.lec.spring.repository.PartnerMenuRepository;
import com.lec.spring.repository.PartnerRepository;
import com.lec.spring.util.U;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
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
public class PartnerMenuService {

    @Value("${app.upload_menu.path}")
    private String menuUploadDir;

    private final PartnerMenuRepository partnerMenuRepository;
    private final PartnerRepository partnerRepository;






    // 메뉴 전체리스트 필요할까?
    public List<PartnerMenu> list(){
        return partnerMenuRepository.findAll(Sort.by(Sort.Order.desc("id")));
    }



    // 해당 업체 아이디로 메뉴리스트 찾아오기
    public List<PartnerMenu> getMenuListByPartnerId(Long partnerId){
        return partnerMenuRepository.findByPartnerId(partnerId);
    }


    // 메뉴 저장
    public PartnerMenu saveMenu(Long partnerId, String name, String price, String imageURL) {
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new IllegalArgumentException("Partner not found"));

        PartnerMenu menu = new PartnerMenu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuImageUrl(imageURL); // 이미지 URL 저장
        menu.setPartner(partner); // Partner 객체 설정

        return partnerMenuRepository.save(menu);
    }

    public PartnerMenu updateMenu(Long id, String name, String price, String imageURL) {
        PartnerMenu menu = partnerMenuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Menu not found"));

        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuImageUrl(imageURL); // 이미지 URL 저장

        return partnerMenuRepository.save(menu);
    }

    public void deleteMenu(Long id) {
        partnerMenuRepository.deleteById(id); // 메뉴 삭제
    }



}
