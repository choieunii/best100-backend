package com.best.onstyle.dto;

import com.best.onstyle.domain.itemInfo.ItemInfo;
import com.best.onstyle.domain.reply.Reply;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.Date;
@NoArgsConstructor
@Getter
public class ReplySaveRequestDto {

    private String itemInfoId;
    private String content;
    private String password;
    private boolean hasPassword;

    @Builder
    public ReplySaveRequestDto(String itemInfoId, String content, String password, boolean hasPassword) {
        this.itemInfoId = itemInfoId;
        this.content = content;
        this.password = password;
        this.hasPassword = hasPassword;
    }

    public Reply toSaveEntity(ItemInfo itemInfo) {
        return Reply.builder()
                .itemInfo(itemInfo)
                .content(content)
                .likeCnt(0L)
                .hateCnt(0L)
                .password(password)
                .hasPassword(hasPassword)
                .currentUpdate(new Date())
                .build();
    }

}
