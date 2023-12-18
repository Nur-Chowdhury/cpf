package com.c.p.service;

import com.c.p.model.Query;
import com.c.p.repository.QueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QueryService {
    @Autowired
    private QueryRepository queryRepository;

    public void save(Query query){
        queryRepository.save(query);
    }
}
