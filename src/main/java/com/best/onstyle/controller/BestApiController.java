package com.best.onstyle.controller;

import com.best.onstyle.domain.best.Best;
import com.best.onstyle.dto.BestItemRankingsResponseDto;

import com.best.onstyle.dto.BestTop100ResponseDto;
import com.best.onstyle.service.BestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BestApiController {

    private final BestService bestService;

    @GetMapping("/best/top100")
    public List<BestTop100ResponseDto> findBest(@RequestParam(required = false) String sort) {
        return bestService.findBestTop100(sort);
    }

    @GetMapping("/best/rankings/{itemInfoId}")
    public List<BestItemRankingsResponseDto> findBestItemRankings(@PathVariable String itemInfoId) {
        return bestService.findBestItemRankings(itemInfoId);
    }
}
