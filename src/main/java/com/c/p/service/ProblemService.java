package com.c.p.service;

import com.c.p.model.Problem;
import com.c.p.repository.ProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProblemService {
    @Autowired
    ProblemRepository problemRepository;
    public List<Problem> getAllProblem(){ return problemRepository.findAll(); }

    public void addProblem(Problem problem){
        problemRepository.save(problem);
    }

    public void removeProblemById(int id){
        problemRepository.deleteById(id);
    }

    public Optional<Problem> getProblemById(int id){
        return problemRepository.findById(id);
    }
}
