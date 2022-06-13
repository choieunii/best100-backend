package com.best.onstyle.dto.vo;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class BasicItemInfoVO {
    private String largeCategoryName;
    private String middleCategoryName;
    private String categoryName;
    public BasicItemInfoVO(String largeCategoryName, String middleCategoryName, String categoryName) {
        this.largeCategoryName = largeCategoryName;
        this.middleCategoryName = middleCategoryName;
        this.categoryName = categoryName;
    }

}
