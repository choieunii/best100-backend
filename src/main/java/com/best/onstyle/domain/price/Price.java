package com.best.onstyle.domain.price;

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
@Table(name = "price")
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "item_info_id", nullable = false)
    private ItemInfo itemInfo;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "discount_rate", nullable = true)
    private Long discountRate;

    @Column(name = "customer_price", nullable = true)
    private Long customerPrice;

    @Column(name = "current_update", nullable = false)
    private Date currentUpdate;

    @Builder
    public Price(ItemInfo itemInfo, Long price, Long discountRate, Long customerPrice, Date currentUpdate){
        this.itemInfo = itemInfo;
        this.price = price;
        this.discountRate = discountRate;
        this.customerPrice = customerPrice;
        this.currentUpdate = currentUpdate;
    }
}
