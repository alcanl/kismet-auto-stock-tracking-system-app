package com.alcanl.app.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "user_records_info")
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserRecords {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_records_info_id")
    public long userRecordsId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    public User user;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "userRecords", cascade = CascadeType.ALL)
    public Set<ProductRegisterRecord> productRegisterRecords;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "userRecords", cascade = CascadeType.ALL)
    public Set<InputRecord> inputRecords;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "userRecords", cascade = CascadeType.ALL)
    public Set<OutputRecord> outputRecords;
}
