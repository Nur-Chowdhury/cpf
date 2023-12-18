package com.c.p.model;


import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "mentee")
public class Mentee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private Mentor mentor;

    @OneToMany(mappedBy = "mentee", cascade = CascadeType.ALL)
    private List<Request> mentorRequests;

    private String about;
    private String lackings;
    private Boolean haveMentor;

    private Boolean voted;

    public void removeMentor() {
        this.mentor = null;
    }


}
