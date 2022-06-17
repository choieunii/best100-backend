package com.best.onstyle.dto;

import com.best.onstyle.domain.reply.Reply;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
public class ReplyResponseDto {
    private Long id;
    private String content;
    private Long likeCnt;
    private Long hateCnt;
    private Date currentUpdate;

    private boolean hasPassword;

    @Builder
    public ReplyResponseDto(Reply reply) {
        this.id = reply.getId();
        this.content = reply.getContent();
        this.likeCnt = reply.getLikeCnt();
        this.hateCnt = reply.getHateCnt();
        this.hasPassword = reply.isHasPassword();
        this.currentUpdate = reply.getCurrentUpdate();
    }
}
