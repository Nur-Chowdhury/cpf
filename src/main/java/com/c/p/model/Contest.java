package com.c.p.model;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class Contest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "contest_id")
    private int id;

    private String type;
    private String link;

    @Column(name = "date")
    private String date;

    private String place;

    private String host;

    private Boolean status;

    private Integer upVote;

    private Integer downVote;

    private String username;

    @ManyToMany(mappedBy = "votedContests")
    private List<User> voters;


}
