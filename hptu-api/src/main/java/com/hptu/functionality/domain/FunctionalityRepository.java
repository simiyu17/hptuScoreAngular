package com.hptu.functionality.domain;

import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FunctionalityRepository extends JpaRepository<Functionality, Long> {

}
