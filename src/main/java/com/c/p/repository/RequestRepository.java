package com.c.p.repository;

import com.c.p.model.Mentee;
import com.c.p.model.Mentor;
import com.c.p.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    List<Request> findByMentee(Mentee mentee);

    @Modifying
    @Query("DELETE FROM Request r WHERE r.mentee = :mentee")
    void deleteRequestsForMentee(@Param("mentee") Mentee mentee);
}
