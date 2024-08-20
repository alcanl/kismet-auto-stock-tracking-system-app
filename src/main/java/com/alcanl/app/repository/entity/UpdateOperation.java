package com.alcanl.app.repository.entity;

import com.alcanl.app.repository.entity.type.UpdateOperationType;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "update_operation_info")
public class UpdateOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "update_operation_id")
    private long updateOperationId;

    @Column(name = "update_operation_date", nullable = false)
    private LocalDate recordDate = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "update_operation_type", nullable = false)
    private UpdateOperationType updateOperationType;

    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.REMOVE})
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
