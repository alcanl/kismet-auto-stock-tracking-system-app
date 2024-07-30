package com.alcanl.app.repository.entity;

import com.alcanl.app.repository.entity.type.StockMovementType;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "stock_movement_info")
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_movement_id")
    private long stockMovementId;

    @Column(name = "stock_movement_amount", nullable = false)
    private int amount;

    @Column(name = "stock_movement_date", nullable = false)
    private LocalDate recordDate = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "stock_movement_type", nullable = false)
    private StockMovementType stockMovementType;

    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
