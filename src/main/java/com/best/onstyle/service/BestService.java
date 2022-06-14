package com.best.onstyle.service;

import com.best.onstyle.domain.best.Best;
import com.best.onstyle.domain.best.BestRepository;
import com.best.onstyle.domain.itemInfo.ItemInfo;
import com.best.onstyle.domain.itemInfo.ItemInfoRepository;
import com.best.onstyle.domain.reply.Reply;
import com.best.onstyle.domain.reply.ReplyRepository;
import com.best.onstyle.dto.BestByRankingResponseDto;
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
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class BestService {
    private final ItemInfoRepository itemInfoRepository;
    private final BestRepository bestRepository;
    private final ReplyRepository replyRepository;
    @Transactional
    public List<BestTop100ResponseDto> findBestTop100(Optional<String> sortType) {
        Date before = new Date();
        before = new Date(before.getTime() + (1000 * 60 * 60 * 10 * -1));

        Sort sort = sortType.isPresent() ? getSortType(sortType.get()) : getSortType("ranking");

        List<Best> bestTop100 = bestRepository.findAllByCurrentUpdateBetween(before, new Date(2022,06,14), sort);

        return bestTop100.stream().map(best -> getBestTop100ResponseDtoFromBest(best))
                .collect(Collectors.toList());
    }
    @Transactional
    public List<BestTop100ResponseDto> findBestTop100ByCateName(String categoryType, String categoryName){

        List<Best> bestTop100 = bestRepository
                .findAllByCurrentUpdateBetween(getBeforeDate(1), new Date(), getSortType("ranking"));

        Stream<Best> filteredTop100 = bestTop100.stream().filter(best -> {
            switch (categoryType) {
                case "largeCategory": return best.getItemInfo().getLargeCateName().equals(categoryName);
                case "middleCategory": return best.getItemInfo().getMiddleCateName().equals(categoryName);
                case "category" : return best.getItemInfo().getCateName().equals(categoryName);
                case "brandName" : return best.getItemInfo().getBrandName().equals(categoryName);
                default: break;
            }
            return false;
        });

        return filteredTop100.map(best ->getBestTop100ResponseDtoFromBest(best))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<BestByRankingResponseDto> findBestByRanking(Long ranking){
        List<Best> bestByRanking = bestRepository.findTop5ByRankingOrderByCurrentUpdateDesc(ranking);
        return bestByRanking.stream().map(best -> {
            BestTop100ResponseDto bestItemInfo = getBestTop100ResponseDtoFromBest(best);
            return new BestByRankingResponseDto(bestItemInfo, best);
        }).collect(Collectors.toList());
    }
    @Transactional
    public List<BestTop100ResponseDto> findBestTop100ByBrandName(String brandName){

        List<Best> bestTop100 = bestRepository
                .findAllByCurrentUpdateBetween(getBeforeDate(1), new Date(), getSortType("ranking"));

        Stream<Best> filteredTop100 = bestTop100.stream().filter(best ->best.getItemInfo().getBrandName().equals(brandName));

        return filteredTop100.map(best ->getBestTop100ResponseDtoFromBest(best))
                .collect(Collectors.toList());
    }
    @Transactional
    public List<BestTop100ResponseDto> findCurrent3DaysBestTop5(){
        List<Best> bestTop100 = bestRepository.findAllByCurrentUpdateBetweenAndRankingBetween(getBeforeDate(3), new Date(), Long.valueOf(1), Long.valueOf(5));
        return bestTop100.stream()
                .map(best -> getBestTop100ResponseDtoFromBest(best))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<BestItemRankingsResponseDto> findBestItemRankings(String itemInfoId) {
        ItemInfo itemInfo = itemInfoRepository.findByItemInfoId(itemInfoId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다." + itemInfoId));

        return bestRepository.findTop5ByItemInfoOrderByCurrentUpdateDesc(itemInfo).stream()
                .map(BestItemRankingsResponseDto::new)
                .collect(Collectors.toList());
    }

    private BestTop100ResponseDto getBestTop100ResponseDtoFromBest(Best best){
        Optional<Reply> likeTopReply = replyRepository.findTopByItemInfoOrderByLikeCntDesc(best.getItemInfo());
        Optional<Reply> hateTopReply = replyRepository.findTopByItemInfoOrderByHateCntDesc(best.getItemInfo());

        ReplyResponseDto likeReplyResponseDto = likeTopReply.filter(r -> r.getLikeCnt() >= 1)
                .map(ReplyResponseDto::new)
                .orElse(null);
        ReplyResponseDto hateReplyResponseDto = hateTopReply.filter(h -> h.getHateCnt() >= 1)
                .map(ReplyResponseDto::new)
                .orElse(null);
        return new BestTop100ResponseDto(best, likeReplyResponseDto, hateReplyResponseDto);
    }

    private Date getBeforeDate(int day){
        Date before = new Date();
        before = new Date(before.getTime() + (1000 * 60 * 60 * 24 * -(day)));
        return before;
    }

    private Sort getSortType(String sortType){
        if(sortType.equals("replyCnt")){
            return Sort.by(Sort.Direction.DESC, "countOfReplies");
        }else if(sortType.equals("likeCnt")){
            return Sort.by(Sort.Direction.DESC, "countOfItemLikes");
        }
        return Sort.by(Sort.Direction.ASC, "ranking");
    }

}
