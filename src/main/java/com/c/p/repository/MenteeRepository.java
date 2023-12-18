package com.c.p.repository;

import com.c.p.model.Mentee;
import com.c.p.model.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MenteeRepository extends JpaRepository<Mentee, Integer> {
    Optional<Mentee> findByUserUsername(String username);
}
