package com.best.onstyle.controller;

import com.best.onstyle.dto.ReplyResponseDto;
import com.best.onstyle.dto.ReplySaveRequestDto;
import com.best.onstyle.dto.ReplyTopResponseDto;
import com.best.onstyle.dto.ReplyUpdateRequestDto;
import com.best.onstyle.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
public class ReplyApiController {
    private final ReplyService replyService;

    @GetMapping("/reply/{itemInfoId}")
    public List<ReplyResponseDto> findPagingReplyList(@PathVariable String itemInfoId,
                                                      @PageableDefault(sort = "currentUpdate", direction = Sort.Direction.DESC, size = 10) Pageable pageable) {
        return replyService.findPagingReplyList(itemInfoId, pageable).getContent();
    }

    @PostMapping("/reply")
    public Long saveReply(@RequestBody ReplySaveRequestDto requestDto) {
        return replyService.saveReply(requestDto);
    }

    @PutMapping("/reply/{replyId}")
    public Long updateReply(@PathVariable Long replyId, @RequestBody ReplyUpdateRequestDto requestDto) {
        return replyService.updateReply(replyId, requestDto);
    }

    @DeleteMapping("/reply/{replyId}")
    public boolean deleteReply(@PathVariable Long replyId) {
        return replyService.deleteReply(replyId);
    }


}
