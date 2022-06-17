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
        // 더보기를 위해 pageable 사용, 댓글 리스트 조회
        ItemInfo item = itemInfoRepository.findByItemInfoId(itemInfoId).get();
        int totalReplyCnt = item.getReplyList().size();
        List<ReplyResponseDto> replyList = replyRepository.findAllByItemInfo(item, pageable)
                .map(ReplyResponseDto::new).getContent();
        return new ReplyListResponseDto(totalReplyCnt, replyList);
    }

    @Transactional
    public Long saveReply(ReplySaveRequestDto requestDto) {
        //댓글 저장
        ItemInfo itemInfo = itemInfoRepository.findByItemInfoId(requestDto.getItemInfoId())
                .orElseThrow(()->new IllegalArgumentException("해당 상품이 없습니다."));

        if(requestDto.isHasPassword() && requestDto.getPassword() == ""){
            throw new PasswordException("비밀번호를 입력해주세요.");
        }

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
        //댓글 수정
        Reply reply = replyRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 한줄평이 없습니다. id=" + id));

        if(!reply.getPassword().equals(requestDto.getPassword())){
            throw new PasswordException("비밀번호가 일치하지 않습니다.");
        }

        reply.update(requestDto.getContent());
        return replyRepository.save(reply).getId();
    }

    @Transactional
    public boolean deleteReply(Long id, ReplyDeleteRequestDto requestDto) {
        //댓글 삭제
        Reply reply = replyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 한줄평이 없습니다. id=" + id));

        if(!reply.getPassword().equals(requestDto.getPassword())){
            throw new PasswordException("비밀번호가 일치하지 않습니다.");
        }

        replyRepository.delete(reply);
        return true;
    }

    @Transactional
    public ReplyTopResponseDto findTopLikeCntAndHateCnt(String itemInfoId){
        // BEST 좋아요, BEST 싫어요 댓글 반환
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
        // 댓글 좋아요 증가
        Reply reply = replyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 한줄평이 없습니다. id=" + id));
        reply.updateLikeCnt();
        replyRepository.save(reply);
    }

    @Transactional
    public void updateReplyHateCnt(Long id){
        // 댓글 싫어요 증가
        Reply reply = replyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 한줄평이 없습니다. id=" + id));
        reply.updateHateCnt();
        replyRepository.save(reply);
    }
}
