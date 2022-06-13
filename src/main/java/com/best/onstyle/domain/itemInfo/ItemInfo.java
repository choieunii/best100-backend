package com.best.onstyle.domain.itemInfo;

import com.best.onstyle.domain.best.Best;

import com.best.onstyle.domain.price.Price;
import com.best.onstyle.domain.reply.Reply;

import com.best.onstyle.dto.vo.BasicItemInfoVO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "item_info")
public class ItemInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    @Column(name = "item_info_id", nullable = false)
    private String itemInfoId;
    @Column(name = "brand_name", nullable = false)
    private String brandName;
    @Column(name = "item_name", nullable = false)
    private String itemName;
    @Column(name = "item_img_url", nullable = false)
    private String itemImgUrl;
    @Column(name = "large_category_name", nullable = false)
    private String largeCateName;
    @Column(name = "middle_category_name", nullable = false)
    private String middleCateName;
    @Column(name = "category_name", nullable = false)
    private String cateName;

    @Column(name = "like_cnt", nullable = true)
    @ColumnDefault("0")
    private Long likeCnt;
    @Column(name = "current_update", nullable = false)
    private Date currentUpdate;

    @OneToMany(
            mappedBy = "itemInfo",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @OrderBy("current_update desc")
    private List<Best> bestList;

    @OneToMany(
            mappedBy = "itemInfo",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @OrderBy("current_update desc")
    private List<Reply> replyList;

    @OneToMany(
            mappedBy = "itemInfo",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @OrderBy("current_update desc")
    private List<Price> priceList;

    @Builder
    public ItemInfo(String itemInfoId,
                    String brandName,
                    String itemName,
                    String itemImgUrl,
                    BasicItemInfoVO basicItemInfoVO,
                    Date currentUpdate) {
        this.itemInfoId = itemInfoId;
        this.brandName = brandName;
        this.itemName = itemName;
        this.itemImgUrl = itemImgUrl;
        this.largeCateName = basicItemInfoVO.getLargeCategoryName();
        this.middleCateName = basicItemInfoVO.getMiddleCategoryName();
        this.cateName = basicItemInfoVO.getCategoryName();
        this.likeCnt = Long.valueOf(0);
        this.currentUpdate = currentUpdate;
    }

    public void update(String itemInfoId,
                       String itemName,
                       String brandName,
                       String itemImgUrl,
                       BasicItemInfoVO basicItemInfoVO,
                       Date currentUpdate) {
        this.itemInfoId = itemInfoId;
        this.brandName = brandName;
        this.itemName = itemName;
        this.itemImgUrl = itemImgUrl;
        this.largeCateName = basicItemInfoVO.getLargeCategoryName();
        this.middleCateName = basicItemInfoVO.getMiddleCategoryName();
        this.cateName = basicItemInfoVO.getCategoryName();
        this.currentUpdate = currentUpdate;
    }
}
