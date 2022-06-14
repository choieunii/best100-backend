package com.best.onstyle.service;

import com.best.onstyle.config.PasswordException;
import com.best.onstyle.domain.itemInfo.ItemInfo;
import com.best.onstyle.domain.itemInfo.ItemInfoRepository;
import com.best.onstyle.domain.reply.Reply;
import com.best.onstyle.domain.reply.ReplyRepository;
import com.best.onstyle.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final ItemInfoRepository itemInfoRepository;

    @Transactional
    public ReplyListResponseDto findPagingReplyList(String itemInfoId, Pageable pageable) {
        ItemInfo item = itemInfoRepository.findByItemInfoId(itemInfoId).get();
        int totalReplyCnt = item.getReplyList().size();
        List<ReplyResponseDto> replyList = replyRepository.findAllByItemInfoOrderByCurrentUpdateDesc(item, pageable)
                .map(ReplyResponseDto::new).getContent();
        return new ReplyListResponseDto(totalReplyCnt, replyList);
    }

    @Transactional
    public Long saveReply(ReplySaveRequestDto requestDto) {
        ItemInfo itemInfo = itemInfoRepository.findByItemInfoId(requestDto.getItemInfoId())
                .orElseThrow(()->new IllegalArgumentException("해당 상품이 없습니다."));

        return replyRepository.save(requestDto.toSaveEntity(itemInfo)).getId();
    }

    @Transactional(readOnly = true)
    public ReplyResponseDto findReplyById(Long id) {
        Reply reply = replyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 한줄평이 없습니다. id=" + id));
        return new ReplyResponseDto(reply);
    }
    @Transactional
    public Long updateReply(Long id, ReplyUpdateRequestDto requestDto) {
        Reply reply = replyRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 한줄평이 없습니다. id=" + id));

        if(!reply.getPassword().equals(requestDto.getPassword())){
            throw new PasswordException();
        }

        reply.update(requestDto.getContent());
        return replyRepository.save(reply).getId();
    }

    @Transactional
    public boolean deleteReply(Long id) {
        Reply reply = replyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 한줄평이 없습니다. id=" + id));
        replyRepository.delete(reply);
        return true;
    }

    @Transactional
    public ReplyTopResponseDto findTopLikeCntAndHateCnt(String itemInfoId){
        ItemInfo item = itemInfoRepository.findByItemInfoId(itemInfoId).get();
        Optional<ReplyResponseDto> likeTopReply = replyRepository.findTopByItemInfoOrderByLikeCntDesc(item)
                .filter(r -> r.getLikeCnt() >= 1)
                .map(ReplyResponseDto::new);
        Optional<ReplyResponseDto> hateTopReply = replyRepository.findTopByItemInfoOrderByHateCntDesc(item)
                .filter(r -> r.getHateCnt() >= 1)
                .map(ReplyResponseDto::new);

        ReplyResponseDto likeTopReplyResponseDto = likeTopReply.orElse(null);
        ReplyResponseDto hateTopReplyResponseDto = hateTopReply.orElse(null);

        return new ReplyTopResponseDto(likeTopReplyResponseDto, hateTopReplyResponseDto);
    }

    @Transactional
    public void updateReplyLikeCnt(Long id){
        Reply reply = replyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 한줄평이 없습니다. id=" + id));
        reply.updateLikeCnt();
        replyRepository.save(reply);
    }

    @Transactional
    public void updateReplyHateCnt(Long id){
        Reply reply = replyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 한줄평이 없습니다. id=" + id));
        reply.updateHateCnt();
        replyRepository.save(reply);
    }
}
