package com.best.onstyle.dto;

import com.best.onstyle.domain.best.Best;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
public class BestByRankingResponseDto {
    private Long id;
    private BestTop100ResponseDto bestItemInfo;
    private Date currentUpdate;

    @Builder
    public BestByRankingResponseDto(BestTop100ResponseDto dto, Best best){
        this.id = best.getId();
        this.bestItemInfo = dto;
        this.currentUpdate = best.getCurrentUpdate();
    }
}
