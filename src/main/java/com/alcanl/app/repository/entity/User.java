package com.alcanl.app.repository.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Table(name = "user_info")
@Entity
@EqualsAndHashCode
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long userId;

    @Column(name = "user_name", nullable = false, unique = true)
    private String username;

    @Column(name = "e_mail", unique = true)
    private String eMail;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String password;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin;

    @Column(name = "register_date", nullable = false)
    private LocalDate dateOfRegister = LocalDate.now();

    @Column(length = 500)
    private String description;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<StockMovement> stockMovements;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<UpdateOperation> updateOperations;

}
