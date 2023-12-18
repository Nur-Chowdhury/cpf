package com.c.p.service;

import com.c.p.model.Contest;
import com.c.p.repository.ContestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContestService {

    @Autowired
    ContestRepository contestRepository;
    public List<Contest> getAllContest(){
        return contestRepository.findAll();
    }

    public void addContest(Contest contest){
        contestRepository.save(contest);
    }

    public void removeContestById(int id) { contestRepository.deleteById(id); }

    public Optional<Contest> getContestById(int id){ return contestRepository.findById(id); }
}
