package com.best.onstyle.domain.itemInfo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ItemInfoRepository extends JpaRepository<ItemInfo,Long> {
    List<ItemInfo> findAllByCurrentUpdate(Date currentUpdate);
    Date findTopByOrderByCurrentUpdateDesc();
    Optional<ItemInfo> findByItemInfoId(String itemInfoId);

}
