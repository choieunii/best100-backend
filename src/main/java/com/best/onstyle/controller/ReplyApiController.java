package com.best.onstyle.controller;

import com.best.onstyle.dto.*;
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

    @GetMapping("/replies/{itemInfoId}")
    public ReplyListResponseDto findPagingReplyList(@PathVariable String itemInfoId,
                                                    @PageableDefault( direction = Sort.Direction.DESC, size = 5) Pageable pageable) {
        return replyService.findPagingReplyList(itemInfoId, pageable);
    }

    @GetMapping("/reply/{replyId}")
    public ReplyResponseDto findReplyById(@PathVariable Long replyId) {
        return replyService.findReplyById(replyId);
    }
    @PostMapping("/reply")
    public Long saveReply(@RequestBody ReplySaveRequestDto requestDto) {
        return replyService.saveReply(requestDto);
    }

    @PutMapping("/reply/{replyId}")
    public Long updateReply(@PathVariable Long replyId, @RequestBody ReplyUpdateRequestDto requestDto) {
        return replyService.updateReply(replyId, requestDto);
    }

    @PostMapping("/reply/{replyId}")
    public boolean deleteReply(@PathVariable Long replyId, @RequestBody ReplyDeleteRequestDto requestDto) {
        return replyService.deleteReply(replyId, requestDto);
    }

    @GetMapping("/reply/top/{itemInfoId}")
    public ReplyTopResponseDto findTopLikeCntAndHateCnt(@PathVariable String itemInfoId){
        return replyService.findTopLikeCntAndHateCnt(itemInfoId);
    }

    @PostMapping("/reply/like/{replyId}")
    public boolean updateLike(@PathVariable Long replyId) {
        replyService.updateReplyLikeCnt(replyId);
        return true;
    }

    @PostMapping("/reply/hate/{replyId}")
    public boolean updateHate(@PathVariable Long replyId) {
        replyService.updateReplyHateCnt(replyId);
        return true;
    }

}
