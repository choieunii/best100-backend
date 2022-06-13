package com.best.onstyle.service;

import com.best.onstyle.domain.best.Best;
import com.best.onstyle.domain.best.BestRepository;
import com.best.onstyle.domain.itemInfo.ItemInfo;
import com.best.onstyle.domain.itemInfo.ItemInfoRepository;
import com.best.onstyle.domain.price.PriceRepository;
import com.best.onstyle.dto.BestItemPricesResponseDto;
import com.best.onstyle.dto.BestItemRankingsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PriceService {
    private final ItemInfoRepository itemInfoRepository;
    private final PriceRepository priceRepository;

    @Transactional
    public List<BestItemPricesResponseDto> findBestItemPrices(String itemInfoId) {
        ItemInfo itemInfo = itemInfoRepository.findByItemInfoId(itemInfoId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다." + itemInfoId));

        return priceRepository.findTop5ByItemInfoOrderByCurrentUpdateDesc(itemInfo).stream()
                .map(BestItemPricesResponseDto::new)
                .collect(Collectors.toList());
    }

}
