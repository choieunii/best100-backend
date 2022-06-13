package com.best.onstyle.dto;

import com.best.onstyle.domain.best.Best;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
@Getter
public class BestItemRankingsResponseDto {
    private Long id;
    private Long ranking;
    private Date currentUpdate;

    @Builder
    public BestItemRankingsResponseDto(Best best){
        this.id = best.getId();
        this.ranking = best.getRanking();
        this.currentUpdate = best.getCurrentUpdate();
    }
}
