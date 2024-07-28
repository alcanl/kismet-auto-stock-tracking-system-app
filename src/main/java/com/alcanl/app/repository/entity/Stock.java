package com.alcanl.app.repository.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Entity
@Table(name = "stock_info")
@EqualsAndHashCode
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_id")
    public long stockId;

    @Column(name = "stock_amount", nullable = false)
    public int amount;

    @Column(name = "stock_threshold", nullable = false)
    public int threshold;

    @Column(name = "shelf_no", nullable = false)
    public String shelfNumber;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "stock", cascade = CascadeType.ALL)
    public Product product;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "stock", cascade = CascadeType.ALL)
    public Set<InputRecord> inputRecords;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "stock", cascade = CascadeType.ALL)
    public Set<OutputRecord> outputRecords;
}
