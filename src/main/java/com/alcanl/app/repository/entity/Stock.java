package com.alcanl.app.repository.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "stock_info")
@EqualsAndHashCode
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_id")
    private long stockId;

    @Column(name = "stock_amount", nullable = false)
    private int amount;

    @Column(name = "stock_threshold", nullable = false)
    private int threshold;

    @Column(name = "shelf_no", nullable = false)
    private String shelfNumber;

    @OneToMany(mappedBy = "stock", cascade = CascadeType.MERGE)
    private Set<StockMovement> stockMovements;

    @JoinColumn(name = "original_code", referencedColumnName = "original_code")
    @OneToOne(cascade = CascadeType.ALL)
    private Product product;

}
