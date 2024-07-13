package com.alcanl.app.repository.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Table(name = "stock_input_record_info")
@Entity
@EqualsAndHashCode
public class InputRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "input_record_id")
    public long inputRecordId;

    @Column(name = "input_amount", nullable = false)
    public int amount;

    @Column(name = "record_date", nullable = false)
    public LocalDate recordDate;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", nullable = false)
    public User user;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "stockId", nullable = false)
    public Stock stock;

}
