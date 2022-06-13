package com.best.onstyle.service;

import com.best.onstyle.domain.best.Best;
import com.best.onstyle.domain.best.BestRepository;
import com.best.onstyle.domain.itemInfo.ItemInfo;
import com.best.onstyle.domain.itemInfo.ItemInfoRepository;
import com.best.onstyle.domain.reply.Reply;
import com.best.onstyle.domain.reply.ReplyRepository;
import com.best.onstyle.dto.BestItemRankingsResponseDto;
import com.best.onstyle.dto.BestTop100ResponseDto;
import com.best.onstyle.dto.ReplyResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BestService {
    private final ItemInfoRepository itemInfoRepository;
    private final BestRepository bestRepository;
    private final ReplyRepository replyRepository;
    @Transactional
    public List<BestTop100ResponseDto> findBestTop100(String sortType) {
        Date before = new Date();
        before = new Date(before.getTime() + (1000 * 60 * 60 * 10 * -1));

        Sort sort;
        if(sortType == null){
            sort = Sort.by(Sort.Direction.ASC, "ranking");
        }else {
            sort = getSortType(sortType);
        }

        List<Best> bestTop100 = bestRepository.findAllByCurrentUpdateBetween(before, new Date(), sort);

        return bestTop100.stream().map(best -> {
            Optional<Reply> likeTopReply = replyRepository.findTopByItemInfoOrderByLikeCntDesc(best.getItemInfo());
            Optional<Reply> hateTopReply = replyRepository.findTopByItemInfoOrderByHateCntDesc(best.getItemInfo());

            ReplyResponseDto likeReplyResponseDto = likeTopReply.map(ReplyResponseDto::new).orElse(null);
            ReplyResponseDto hateReplyResponseDto = hateTopReply.map(ReplyResponseDto::new).orElse(null);

            return new BestTop100ResponseDto(best, likeReplyResponseDto, hateReplyResponseDto);
        }).collect(Collectors.toList());
    }

    @Transactional
    public List<BestItemRankingsResponseDto> findBestItemRankings(String itemInfoId) {
        ItemInfo itemInfo = itemInfoRepository.findByItemInfoId(itemInfoId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다." + itemInfoId));

        return bestRepository.findTop5ByItemInfoOrderByCurrentUpdateDesc(itemInfo).stream()
                .map(BestItemRankingsResponseDto::new)
                .collect(Collectors.toList());
    }

    private Sort getSortType(String sortType){
        if(sortType.equals("replyCnt")){
            return Sort.by(Sort.Direction.DESC, "countOfReplies");
        }else if(sortType.equals("likeCnt")){
            return Sort.by(Sort.Direction.DESC, "countOfItemLikes");
        }
        return Sort.by(Sort.Direction.DESC, "currentUpdate");
    }

}
