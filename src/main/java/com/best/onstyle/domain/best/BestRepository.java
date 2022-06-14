package com.best.onstyle.domain.best;

import com.best.onstyle.domain.itemInfo.ItemInfo;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface BestRepository extends JpaRepository<Best,Long> {
    List<Best> findAllByItemInfo(ItemInfo itemInfo);
    List<Best> findTop5ByItemInfoOrderByCurrentUpdateDesc(ItemInfo itemInfo);
    List<Best> findAllByCurrentUpdateBetween(Date start, Date end, Sort sort);
    Optional<Best> findByItemInfoAndCurrentUpdateBetween(ItemInfo itemInfo, Date start, Date end);
    List<Best> findAllByCurrentUpdateBetweenAndRankingBetween(Date start, Date end, Long sRanking, Long eRanking);
    List<Best> findTop5ByRankingOrderByCurrentUpdateDesc(Long ranking);
}
