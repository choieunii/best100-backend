package com.best.onstyle.service;

import com.best.onstyle.config.PasswordException;
import com.best.onstyle.domain.itemInfo.ItemInfo;
import com.best.onstyle.domain.itemInfo.ItemInfoRepository;
import com.best.onstyle.domain.reply.Reply;
import com.best.onstyle.domain.reply.ReplyRepository;
import com.best.onstyle.dto.ReplyResponseDto;
import com.best.onstyle.dto.ReplySaveRequestDto;
import com.best.onstyle.dto.ReplyTopResponseDto;
import com.best.onstyle.dto.ReplyUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final ItemInfoRepository itemInfoRepository;

    @Transactional
    public Page<ReplyResponseDto> findPagingReplyList(String itemInfoId, Pageable pageable) {
        ItemInfo item = itemInfoRepository.findByItemInfoId(itemInfoId).get();
        return replyRepository.findAllByItemInfo(item, pageable)
                .map(ReplyResponseDto::new);
    }
    @Transactional
    public Long saveReply(ReplySaveRequestDto requestDto) {
        ItemInfo itemInfo = itemInfoRepository.findByItemInfoId(requestDto.getItemInfoId())
                .orElseThrow(()->new IllegalArgumentException("해당 상품이 없습니다."));

        return replyRepository.save(requestDto.toSaveEntity(itemInfo)).getId();
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
        ReplyResponseDto likeTopReply = replyRepository.findTopByItemInfoOrderByLikeCntDesc(item)
                .map(ReplyResponseDto::new).get();
        ReplyResponseDto hateTopReply = replyRepository.findTopByItemInfoOrderByHateCntDesc(item)
                .map(ReplyResponseDto::new).get();
        return new ReplyTopResponseDto(likeTopReply, hateTopReply);
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
