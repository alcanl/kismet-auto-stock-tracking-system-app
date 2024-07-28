package com.alcanl.app.repository.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Table(name = "stock_input_record_info")
@Entity
@EqualsAndHashCode
public class InputRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "input_record_id")
    public long inputRecordId;

    @Column(name = "input_amount", nullable = false)
    public int amount;

    @Column(name = "record_date", nullable = false)
    public LocalDate recordDate = LocalDate.now();

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    public User user;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "stock_id", nullable = false)
    public Stock stock;

}
