package com.alcanl.app.repository.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

@Entity
@Table(name = "product_register_record_info")
@EqualsAndHashCode
public class ProductRegisterRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_register_record_info_id")
    public Long productRegisterRecordId;

    @Column(name = "record_amount", nullable = false)
    public int amount;

    @Column(name = "product_register_record_date", nullable = false)
    public LocalDate productRegisterRecordDate;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "productRegisterRecord", cascade = CascadeType.ALL)
    @JoinColumn(name = "original_code", nullable = false)
    public Product product;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_records_info_id", nullable = false)
    public UserRecords userRecords;
}
