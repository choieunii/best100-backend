package com.best.onstyle.domain.reply;

import com.best.onstyle.domain.best.Best;
import com.best.onstyle.domain.itemInfo.ItemInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply,Long> {
    Page<Reply> findAllByItemInfo(ItemInfo itemInfo, Pageable pageable);
    Optional<Reply> findTopByItemInfoOrderByLikeCntDesc(ItemInfo itemInfo);
    Optional<Reply> findTopByItemInfoOrderByLikeCntDesc(Best best);
    Optional<Reply> findTopByItemInfoOrderByHateCntDesc(ItemInfo itemInfo);
    Optional<Reply> findTopByItemInfoOrderByHateCntDesc(Best best);
}
