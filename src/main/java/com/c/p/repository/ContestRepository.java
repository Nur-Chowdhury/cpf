package com.c.p.repository;

import com.c.p.model.Contest;
import com.c.p.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContestRepository extends JpaRepository<Contest, Integer> {
}
