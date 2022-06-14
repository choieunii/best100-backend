package com.best.onstyle.dto;

import com.best.onstyle.domain.price.Price;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
@Getter
public class BestItemPricesResponseDto {
    private Long id;
    private Long price;
    private Long discountRate;
    private Long customerPrice;
    private Date currentUpdate;

    @Builder
    public BestItemPricesResponseDto(Price price){
        this.id = price.getId();
        this.price = price.getPrice();
        this.discountRate = price.getDiscountRate();
        this.customerPrice = price.getCustomerPrice();
        this.currentUpdate = price.getCurrentUpdate();
    }
}
