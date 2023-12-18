package com.c.p.model;


import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "mentors")
public class Mentor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String about;

    @OneToMany(mappedBy = "mentor", cascade = CascadeType.ALL)
    private List<Mentee> mentees;

    @OneToMany(mappedBy = "mentor", cascade = CascadeType.ALL)
    private List<Request> mentorRequests;

    private Integer points;
}
