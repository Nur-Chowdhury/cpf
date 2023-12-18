package com.c.p.service;

import com.c.p.model.Mentee;
import com.c.p.model.Mentor;
import com.c.p.model.Request;
import com.c.p.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequestService{

    @Autowired
    RequestRepository requestRepository;

    public void createRequest(Mentee mentee, Mentor mentor){
        Request request = new Request();
        request.setMentee(mentee);
        request.setMentor(mentor);
        requestRepository.save(request);
    }

}
