package com.best.onstyle.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BestResponseDto {
    private Long id;
    private String itemInfoId;
    private Long ranking;
    private String storeName;
    private String itemName;
    private String itemImgUrl;
    private String category;
    private String largeCateName;
    private String middleCateName;
    private String cateName;

    private Long likeCnt;
    private Long price;

    @Builder
    public BestResponseDto(Long id, String itemInfoId, Long ranking, String storeName, String itemName, String itemImgUrl, String category, String largeCateName, String middleCateName, String cateName, Long likeCnt, Long price) {
        this.id = id;
        this.itemInfoId = itemInfoId;
        this.ranking = ranking;
        this.storeName = storeName;
        this.itemName = itemName;
        this.itemImgUrl = itemImgUrl;
        this.category = category;
        this.largeCateName = largeCateName;
        this.middleCateName = middleCateName;
        this.cateName = cateName;
        this.likeCnt = likeCnt;
        this.price = price;
    }
}
