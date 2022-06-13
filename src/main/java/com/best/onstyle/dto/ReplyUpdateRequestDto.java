package com.best.onstyle.dto;

import com.best.onstyle.domain.reply.Reply;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
public class ReplyUpdateRequestDto {

    private String content;
    private String password;

    @Builder
    public ReplyUpdateRequestDto(String content, String password) {
        this.content = content;
        this.password = password;
    }

}
