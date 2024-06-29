package com.alcanl.app.repository.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Table(name = "user_info")
@Entity
@EqualsAndHashCode
public class User {

    @Id()
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    public long id;

    @Column(name = "user_name", nullable = false)
    public String username;

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
}
