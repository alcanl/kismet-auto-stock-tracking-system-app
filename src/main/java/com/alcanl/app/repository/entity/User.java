package com.alcanl.app.repository.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.Set;

@Table(name = "user_info")
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @Id()
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    public long userId;

    @Column(name = "user_name", nullable = false)
    public String username;

    @EqualsAndHashCode.Include
    @Column(name = "e_mail")
    public String eMail;

    @Column(name = "first_name", nullable = false)
    public String firstName;

    @Column(nullable = false)
    public String password;

    @Column(name = "last_name", nullable = false)
    public String lastName;

    @Column(name = "is_admin", nullable = false)
    public boolean isAdmin;

    @Column(name = "register_date", nullable = false)
    public LocalDate dateOfRegister = LocalDate.now();

    public String description;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    public Set<InputRecord> inputRecord;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    public Set<OutputRecord> outputRecord;
}
