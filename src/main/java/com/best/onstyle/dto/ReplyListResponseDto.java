package com.best.onstyle.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ReplyListResponseDto {
    private int totalReplyCnt;
    private List<ReplyResponseDto> replyList;

    @Builder
    public ReplyListResponseDto(int totalReplyCnt, List<ReplyResponseDto> replyResponseDtos) {
        this.totalReplyCnt = totalReplyCnt;
        this.replyList = replyResponseDtos;
    }
}
