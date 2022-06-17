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
    public List<BestTop100ResponseDto> findBestTop100(Optional<String> sortType, Optional<String> filterType) {
        // BEST TOP 100 정보 조회 및 필터링
        Sort sort = sortType.isPresent() ? getSortType(sortType.get()) : getSortType("ranking");

        List<Best> bestTop100 = bestRepository.findAllByCurrentUpdateBetween(getBeforeDate(1), new Date(), sort);

        if (filterType.isPresent()) {
            return bestTop100.stream().filter(best -> {
                // filter 값이 있다면, 랭킹 증가여부, 랭킹 감소 여부, 할인 여부에 따라 필터링한다.
                        switch (filterType.get()) {
                            case "increaseRank":
                                return isIncreaseAndDecreaseRank(best, "increaseRank");
                            case "decreaseRank":
                                return isIncreaseAndDecreaseRank(best, "decreaseRank");
                            case "sale":
                                return best.getItemInfo().getPriceList().get(0).getDiscountRate() != 0;
                            default:
                                break;
                        }
                        return false;
                    }).map(best -> getBestTop100ResponseDtoFromBest(best))
                    .collect(Collectors.toList());
        } else {
            // 없을 경우 DTO에 넣어 return
            return bestTop100.stream().map(best -> getBestTop100ResponseDtoFromBest(best))
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public List<BestTop100ResponseDto> findBestTop100ByCateName(String categoryType, String categoryName) {
        // 상품 세부 정보 필터링
        List<Best> bestTop100 = bestRepository
                .findAllByCurrentUpdateBetween(getBeforeDate(1), new Date(), getSortType("ranking"));
        // category Type (대분류, 중분류, 소분류, 브랜드 명) 에 따라 필터링한다.
        Stream<Best> filteredTop100 = bestTop100.stream().filter(best -> {
            switch (categoryType) {
                case "largeCategory":
                    return best.getItemInfo().getLargeCateName().equals(categoryName);
                case "middleCategory":
                    return best.getItemInfo().getMiddleCateName().equals(categoryName);
                case "category":
                    return best.getItemInfo().getCateName().equals(categoryName);
                case "brandName":
                    return best.getItemInfo().getBrandName().equals(categoryName);
                default:
                    break;
            }
            return false;
        });

        return filteredTop100.map(best -> getBestTop100ResponseDtoFromBest(best))
                .collect(Collectors.toList());
    }

    private boolean isIncreaseAndDecreaseRank(Best best, String type) {
        // BEST 100 필터링 시 랭킹 증가 감소 여부를 확인하는 메소드
        List<Best> bestList = best.getItemInfo().getBestList();
        if (bestList.size() < 2) return false;

        Long beforeRanking = bestList.get(1).getRanking();
        Long afterRanking = bestList.get(0).getRanking();

        if (type.equals("increaseRank")) {
            return afterRanking < beforeRanking;
        } else if (type.equals("decreaseRank")) {
            return afterRanking > beforeRanking;
        }
        return false;
    }

    @Transactional
    public List<BestByRankingResponseDto> findBestByRanking(Long ranking) {
        // 랭킹 값을 받아와 최근 5일간 동일한 랭크에 위치한 상품을 반환
        List<Best> bestByRanking = bestRepository.findTop5ByRankingOrderByCurrentUpdateDesc(ranking);
        return bestByRanking.stream().map(best -> {
            BestTop100ResponseDto bestItemInfo = getBestTop100ResponseDtoFromBest(best);
            return new BestByRankingResponseDto(bestItemInfo, best);
        }).collect(Collectors.toList());
    }

    @Transactional
    public List<BestTop100ResponseDto> findCurrent5DaysBestTop5() {
        // 최근 5일간 BEST 5에 들었던 상품 반환
        List<Best> bestTop100 = bestRepository.findAllByCurrentUpdateBetweenAndRankingBetween(getBeforeDate(5), new Date(), Long.valueOf(1), Long.valueOf(5));
        return bestTop100.stream()
                .map(best -> getBestTop100ResponseDtoFromBest(best))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<BestItemRankingsResponseDto> findBestItemRankings(String itemInfoId) {
        // 차트를 그리기 위한 랭킹 5일치 정보 반환
        ItemInfo itemInfo = itemInfoRepository.findByItemInfoId(itemInfoId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다." + itemInfoId));

        return bestRepository.findTop5ByItemInfoOrderByCurrentUpdateDesc(itemInfo).stream()
                .map(BestItemRankingsResponseDto::new)
                .collect(Collectors.toList());
    }

    private BestTop100ResponseDto getBestTop100ResponseDtoFromBest(Best best) {
        //BEST 100에서 Reply 정보를 포함하여 DTO에 매핑하는 과정
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

    private Date getBeforeDate(int day) {
        Date before = new Date();
        before = new Date(before.getTime() + (1000 * 60 * 60 * 22 * -(day)));
        return before;
    }

    private Sort getSortType(String sortType) {
        if (sortType.equals("replyCnt")) {
            return Sort.by(Sort.Direction.DESC, "countOfReplies");
        } else if (sortType.equals("likeCnt")) {
            return Sort.by(Sort.Direction.DESC, "countOfItemLikes");
        }else{
            return Sort.by(Sort.Direction.ASC, "ranking");
        }
    }
}
