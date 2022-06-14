package com.best.onstyle.dto;

import com.best.onstyle.domain.itemInfo.ItemInfo;
import com.best.onstyle.domain.price.Price;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ItemResponseDto {
    private Long id;
    private String itemInfoId;
    private Long ranking;
    private String brandName;
    private String itemName;
    private String itemImgUrl;
    private String largeCateName;
    private String middleCateName;
    private String cateName;
    private Long likeCnt;
    private Long replyCnt;
    private Price price;

    private boolean isCurrentUpdateBest;
    @Builder
    public ItemResponseDto(ItemInfo item, boolean isCurrentUpdateBest) {
        this.id = item.getId();
        this.itemInfoId = item.getItemInfoId();
        this.ranking = item.getBestList().get(0).getRanking();
        this.brandName = item.getBrandName();
        this.itemName = item.getItemName();
        this.itemImgUrl = item.getItemImgUrl();
        this.largeCateName = item.getLargeCateName();
        this.middleCateName = item.getMiddleCateName();
        this.cateName = item.getCateName();
        this.likeCnt = item.getLikeCnt();
        this.replyCnt = Long.valueOf(item.getReplyList().size());
        this.price = item.getPriceList().get(0);
        this.isCurrentUpdateBest = isCurrentUpdateBest;
    }
}
