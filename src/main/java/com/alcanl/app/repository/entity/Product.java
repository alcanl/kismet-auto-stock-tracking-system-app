package com.alcanl.app.repository.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

import java.io.File;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "product_info")
@EqualsAndHashCode
public class Product {

    @Id
    @Column(name = "original_code")
    public String originalCode;

    @Column(name = "product_register_date", nullable = false)
    public LocalDate registerDate = LocalDate.now();

    @Column(name = "stock_code", nullable = false)
    public String stockCode;

    @Column(name = "product_name", nullable = false)
    public String productName;

    @Column(name = "product_image")
    public File imageFile;

    @Column(length = 500)
    public String description;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "stock_id", nullable = false)
    public Stock stock;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public ProductRegisterRecord productRegisterRecord;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "productInput", cascade = CascadeType.ALL)
    public Set<InputRecord> inputRecords;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "productOutput", cascade = CascadeType.ALL)
    public Set<OutputRecord> outputRecords;

}
