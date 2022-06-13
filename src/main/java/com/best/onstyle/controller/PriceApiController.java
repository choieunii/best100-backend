package com.best.onstyle.controller;

import com.best.onstyle.dto.BestItemPricesResponseDto;
import com.best.onstyle.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
public class PriceApiController {
    private final PriceService priceService;

    @GetMapping("/prices/{itemInfoId}")
    public List<BestItemPricesResponseDto> findBestItemRankings(@PathVariable String itemInfoId) {
        return priceService.findBestItemPrices(itemInfoId);
    }

}
