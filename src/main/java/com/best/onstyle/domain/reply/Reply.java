package com.best.onstyle.domain.reply;

import com.best.onstyle.domain.itemInfo.ItemInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "reply")
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "item_info_id", nullable = false)
    private ItemInfo itemInfo;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "like_cnt", nullable = false)
    private Long likeCnt;

    @Column(name = "hate_cnt", nullable = false)
    private Long hateCnt;

    @Column(name = "password", nullable = true)
    private String password;

    @Column(name = "has_password", nullable = true)
    private boolean hasPassword;
    @Column(name = "current_update", nullable = false)
    private Date currentUpdate;

    @Builder
    public Reply(ItemInfo itemInfo, String content, Long likeCnt, Long hateCnt, String password, Date currentUpdate, boolean hasPassword) {
        this.itemInfo = itemInfo;
        this.content = content;
        this.likeCnt = likeCnt;
        this.hateCnt = hateCnt;
        this.password = password;
        this.hasPassword = hasPassword;
        this.currentUpdate = currentUpdate;
    }

    public void update(String content) {
        this.content = content;
    }

    public void updateLikeCnt(){
        this.likeCnt += 1;
    }

    public void updateHateCnt(){
        this.hateCnt += 1;
    }
}
