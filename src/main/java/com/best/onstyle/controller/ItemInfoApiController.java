package com.best.onstyle.controller;

import com.best.onstyle.domain.itemInfo.ItemInfo;
import com.best.onstyle.dto.BestTop100ResponseDto;
import com.best.onstyle.dto.ItemResponseDto;
import com.best.onstyle.service.BestService;
import com.best.onstyle.service.ItemInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ItemInfoApiController {

    private final BestService bestService;
    private final ItemInfoService itemInfoService;

    @GetMapping("/item/data")
    public boolean saveItemInfos() {
        return itemInfoService.saveItemInfos();
    }

    @GetMapping("/item/{itemInfoId}")
    public ItemResponseDto findItemByItemInfoId(@PathVariable String itemInfoId) {
        return itemInfoService.findItemByItemInfoId(itemInfoId);
    }

    @GetMapping("/item/search")
    public List<BestTop100ResponseDto> findItemByTitleContaining(@RequestParam String itemName) {
        return itemInfoService.findItemByTitleContaining(itemName);
    }

    @PostMapping("/item/like/{itemInfoId}")
    public Long updateItemInfoLikeCnt(@PathVariable String itemInfoId) {
        return itemInfoService.updateItemInfoLikeCnt(itemInfoId);
    }

}
