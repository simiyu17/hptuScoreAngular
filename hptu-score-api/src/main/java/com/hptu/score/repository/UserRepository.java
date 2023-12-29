package com.hptu.score.repository;

import com.hptu.score.entity.User;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@ApplicationScoped
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(@Nonnull String username);


}
