package com.best.onstyle.controller;

import com.best.onstyle.dto.BestByRankingResponseDto;
import com.best.onstyle.dto.BestItemRankingsResponseDto;

import com.best.onstyle.dto.BestTop100ResponseDto;
import com.best.onstyle.service.BestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class BestApiController {

    private final BestService bestService;

    @GetMapping("/best/top100")
    public List<BestTop100ResponseDto> findBest(@RequestParam(required = false) String sort,
                                                @RequestParam(required = false) String filter) {
        return bestService.findBestTop100(Optional.ofNullable(sort), Optional.ofNullable(filter));
    }

    @GetMapping("/best/category")
    public List<BestTop100ResponseDto> findBestTop100ByCateName(@RequestParam String type, @RequestParam String category) {
        return bestService.findBestTop100ByCateName(type, category);
    }

    @GetMapping("/best/ranking")
    public List<BestByRankingResponseDto> findBestByRanking(@RequestParam Long ranking) {
        return bestService.findBestByRanking(ranking);
    }

    @GetMapping("/best/rankings/top5")
    public List<BestTop100ResponseDto> findCurrent3DaysBestTop5(){
        return bestService.findCurrent5DaysBestTop5();
    }

    @GetMapping("/best/rankings/{itemInfoId}")
    public List<BestItemRankingsResponseDto> findBestItemRankings(@PathVariable String itemInfoId) {
        return bestService.findBestItemRankings(itemInfoId);
    }
}
