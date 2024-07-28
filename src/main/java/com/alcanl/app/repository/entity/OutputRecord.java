package com.alcanl.app.repository.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Entity
@Table(name = "stock_output_record_info")
@EqualsAndHashCode
public class OutputRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "output_record_id")
    public long outputRecordId;

    @Column(name = "input_amount", nullable = false)
    public int amount;

    @Column(name = "record_date", nullable = false)
    public LocalDate recordDate;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    public User user;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "stock_id", nullable = false)
    public Stock stock;

}
