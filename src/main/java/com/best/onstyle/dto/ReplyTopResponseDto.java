package com.best.onstyle.dto;

import com.best.onstyle.domain.reply.Reply;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ReplyTopResponseDto {

    private ReplyResponseDto likeTopReply;
    private ReplyResponseDto hateTopReply;

    @Builder
    public ReplyTopResponseDto(ReplyResponseDto likeTopReply, ReplyResponseDto hateTopReply) {
        this.likeTopReply = likeTopReply;
        this.hateTopReply = hateTopReply;
    }

}
