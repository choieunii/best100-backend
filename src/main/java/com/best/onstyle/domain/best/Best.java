package com.best.onstyle.domain.best;

import com.best.onstyle.domain.itemInfo.ItemInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "best")
public class Best {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "item_info_id", nullable = false)
    private ItemInfo itemInfo;

    @Column(name = "ranking", nullable = false)
    private Long ranking;

    @Column(name = "current_update", nullable = false)
    private Date currentUpdate;

    @Formula("(select r.like_cnt from item_info r where r.item_info_id = item_info_id)")
    private Long countOfItemLikes;

    @Formula("(select count(*) from reply r where r.item_info_id = item_info_id)")
    private Long countOfReplies;

    @Builder
    public Best(ItemInfo itemInfo, Long ranking, Date currentUpdate){
        this.itemInfo = itemInfo;
        this.ranking = ranking;
        this.currentUpdate = currentUpdate;
    }
}
