package com.c.p.repository;

import com.c.p.model.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MentorRepository extends JpaRepository<Mentor, Integer> {
    Optional<Mentor> findByUserUsername(String username);
}
