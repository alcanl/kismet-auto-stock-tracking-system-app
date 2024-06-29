package com.alcanl.app.repository.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

import java.io.File;
import java.util.Set;

@Entity
@Table(name = "product_info")
@EqualsAndHashCode
public class Product {

    @Id
    @Column(name = "original_code")
    public String originalCode;

    @Column(name = "stock_code", nullable = false)
    public String stockCode;

    @Column(name = "product_name", nullable = false)
    public String productName;

    @Column(name = "product_image")
    public File imageFile;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "stockId", nullable = false)
    public Stock stock;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.ALL)
    public Set<InputRecord> inputRecords;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.ALL)
    public Set<OutputRecord> outputRecords;

}
