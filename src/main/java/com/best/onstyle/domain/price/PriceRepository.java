package com.best.onstyle.domain.price;

import com.best.onstyle.domain.itemInfo.ItemInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceRepository extends JpaRepository<Price,Long> {
    List<Price> findTop5ByItemInfoOrderByCurrentUpdateDesc(ItemInfo itemInfo);
    List<Price> findAllByDiscountRateOrderByCurrentUpdateDesc(Long discountRate);
}
