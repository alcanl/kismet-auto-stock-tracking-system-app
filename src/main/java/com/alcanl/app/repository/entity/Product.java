package com.alcanl.app.repository.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

import java.io.File;

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
    @JoinColumn(name = "stock_id", nullable = false)
    public Stock stock;

}
