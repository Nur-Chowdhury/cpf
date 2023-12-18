package com.c.p.model;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Query {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String userEmail;
}
