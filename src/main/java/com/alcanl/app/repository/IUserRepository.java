package com.alcanl.app.repository;

import com.alcanl.app.repository.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsernameAndPassword(String username, String password);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByUsernameAndPassword(String username, String password);
}
