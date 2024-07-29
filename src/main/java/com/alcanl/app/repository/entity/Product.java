package com.alcanl.app.repository.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.time.LocalDate;

@Entity
@Table(name = "product_info")
@Getter
@Setter
@EqualsAndHashCode
public class Product {

    @Id
    @Column(name = "original_code")
    private String originalCode;

    @Column(name = "product_register_date", nullable = false)
    private LocalDate registerDate;

    @Column(name = "stock_code", nullable = false)
    private String stockCode;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_image")
    private File imageFile;

    @Column(length = 500)
    private String description;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
    private Stock stock;

}
