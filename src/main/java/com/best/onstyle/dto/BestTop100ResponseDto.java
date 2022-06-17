package com.best.onstyle.dto;


import com.best.onstyle.domain.best.Best;
import com.best.onstyle.domain.itemInfo.ItemInfo;
import com.best.onstyle.domain.price.Price;
import com.best.onstyle.domain.reply.Reply;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
public class BestTop100ResponseDto {
    private Long id;
    private String itemInfoId;
    private List<Best> ranking;
    private String brandName;
    private String itemName;
    private String itemImgUrl;
    private String largeCateName;
    private String middleCateName;
    private String cateName;

    private Long likeCnt;
    private ReplyResponseDto likeTopReply;
    private ReplyResponseDto hateTopReply;
    private Long replyCnt;
    private List<Price> price;

    private Date currentUpdate;

    @Builder
    public BestTop100ResponseDto(Best best, ReplyResponseDto likeTopReply, ReplyResponseDto hateTopReply) {
        this.id = best.getId();
        this.itemInfoId = best.getItemInfo().getItemInfoId();
        this.ranking = best.getItemInfo().getBestList();
        this.brandName = best.getItemInfo().getBrandName();
        this.itemName = best.getItemInfo().getItemName();
        this.itemImgUrl = best.getItemInfo().getItemImgUrl();
        this.largeCateName = best.getItemInfo().getLargeCateName();
        this.middleCateName = best.getItemInfo().getMiddleCateName();
        this.cateName = best.getItemInfo().getCateName();
        this.likeCnt = best.getItemInfo().getLikeCnt();
        this.likeTopReply = likeTopReply;
        this.hateTopReply = hateTopReply;
        this.replyCnt = best.getCountOfReplies();
        this.price = best.getItemInfo().getPriceList();
        this.currentUpdate = best.getCurrentUpdate();
    }

    @Builder
    public BestTop100ResponseDto(ItemInfo item, ReplyResponseDto likeTopReply, ReplyResponseDto hateTopReply) {
        this.id = item.getId();
        this.itemInfoId = item.getItemInfoId();
        this.ranking = item.getBestList();
        this.brandName = item.getBrandName();
        this.itemName = item.getItemName();
        this.itemImgUrl = item.getItemImgUrl();
        this.largeCateName = item.getLargeCateName();
        this.middleCateName = item.getMiddleCateName();
        this.cateName = item.getCateName();
        this.likeCnt = item.getLikeCnt();
        this.likeTopReply = likeTopReply;
        this.hateTopReply = hateTopReply;
        this.replyCnt = Long.valueOf(item.getReplyList().size());
        this.price = item.getPriceList();
    }
    @Builder
    public BestTop100ResponseDto(Best best) {
        this.id = best.getId();
        this.itemInfoId = best.getItemInfo().getItemInfoId();
        this.ranking = best.getItemInfo().getBestList();
        this.brandName = best.getItemInfo().getBrandName();
        this.itemName = best.getItemInfo().getItemName();
        this.itemImgUrl = best.getItemInfo().getItemImgUrl();
        this.largeCateName = best.getItemInfo().getLargeCateName();
        this.middleCateName = best.getItemInfo().getMiddleCateName();
        this.cateName = best.getItemInfo().getCateName();
        this.likeCnt = best.getItemInfo().getLikeCnt();
        this.replyCnt = best.getCountOfReplies();
        this.price = best.getItemInfo().getPriceList();
    }
}
