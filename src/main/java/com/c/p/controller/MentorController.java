package com.c.p.controller;

import com.c.p.model.*;
import com.c.p.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class MentorController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MentorRepository mentorRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    MenteeRepository menteeRepository;

    @Autowired
    RequestRepository requestRepository;

    @GetMapping("/mentor")
    public String mentor(Model model,Principal principal){
        if(principal!=null){
            User user = userRepository.findUserByUsername(principal.getName()).get();
            int id = user.getId();
            model.addAttribute("loggedid", id);
        }
        User user = userRepository.findUserByUsername(principal.getName()).get();
        System.out.println(user.getUsername());
        List<Role> roles = user.getRoles();
        boolean isMentor = false;
        for (Role role : roles) {
            if ("ROLE_MENTOR".equals(role.getName())) {
                isMentor = true;
                break;
            }
        }
        if(isMentor){
            return "redirect:/mentee/list";
        }
        else return "mentorform";
    }

    @PostMapping("/mentor/submit")
    public String submitMentor(@RequestParam String about, Authentication authentication) {
        if (authentication != null) {
            String username = authentication.getName();

            Optional<User> optionalUser = userRepository.findUserByUsername(username);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                List<Role> roles = user.getRoles();
                roles.add(roleRepository.findById(3).get());
                user.setRoles(roles);
                userRepository.save(user);
                Mentor mentor = new Mentor();
                mentor.setUser(user);
                mentor.setAbout(about);
                mentor.setPoints(0);
                mentorRepository.save(mentor);
                return "redirect:/mentee/list";
            } else {
                return "login";
            }
        } else {
            return "login";
        }
    }

    @GetMapping("/mentors/list")
    public String mentorsList(Model model, Principal principal){
        if(principal!=null){
            User user = userRepository.findUserByUsername(principal.getName()).get();
            int id = user.getId();
            model.addAttribute("loggedid", id);
        }
        Mentee mentee = menteeRepository.findByUserUsername(principal.getName()).get();
        List<Request> requests = requestRepository.findByMentee(mentee);
        List<Mentor> mentors = new ArrayList<>();
        for (Request request : requests) {
            mentors.add(request.getMentor());
        }
        model.addAttribute("mentors", mentors);
        return "mentorList";
    }

    @GetMapping("/mentor/{id}")
    public String setMentees(@PathVariable int id, Principal principal){
        Mentee mentee = menteeRepository.findByUserUsername(principal.getName()).get();
        Optional<Mentor> oMentor = mentorRepository.findById(id);
        if(oMentor.isPresent()) {
            Mentor mentor = oMentor.get();
            mentee.setMentor(mentor);
            mentee.setHaveMentor(true);
            menteeRepository.save(mentee);
            return "redirect:/acc/success";
        }
        else return "redirect:/error";
    }

    @GetMapping("/mentors")
    public String mentors(Model model, Principal principal){
        if(principal!=null){
            User user = userRepository.findUserByUsername(principal.getName()).get();
            int id = user.getId();
            model.addAttribute("loggedid", id);
        }
        Mentee mentee = menteeRepository.findByUserUsername(principal.getName()).get();
        Mentor mentor = mentee.getMentor();
        List<Mentor> mentors = new ArrayList<>();
        mentors.add(mentor);
        model.addAttribute("mentors", mentors);
        model.addAttribute("mentee", mentee);
        return "mentors";
    }

    @GetMapping("/mentor/upvote/{id}")
    public String pointp(@PathVariable int id, Principal principal){
        Optional<Mentor> Omentor = mentorRepository.findById(id);
        Mentee mentee = menteeRepository.findByUserUsername(principal.getName()).get();
        if(Omentor.isPresent()){
            Mentor mentor = Omentor.get();
            mentor.setPoints(mentor.getPoints()+1);
            mentee.setVoted(true);
            mentorRepository.save(mentor);
        }
        return "redirect:/mentors";

    }
    @GetMapping("/mentor/downvote/{id}")
    public String pointm(@PathVariable int id, Principal principal){
        Optional<Mentor> oMentor = mentorRepository.findById(id);
        Mentee mentee = menteeRepository.findByUserUsername(principal.getName()).get();
        if(oMentor.isPresent()){
            Mentor mentor = oMentor.get();
            mentor.setPoints(mentor.getPoints()-1);
            mentee.setVoted(true);
            mentorRepository.save(mentor);
            menteeRepository.save(mentee);
        }
        return "redirect:/mentors";
    }

    @GetMapping("/mentor/delete/{id}")
    @Transactional
    public String deleteM(@PathVariable int id, Principal principal){
        Mentee mentee = menteeRepository.findByUserUsername(principal.getName()).get();
        mentee.removeMentor();
        mentee.setHaveMentor(false);
        mentee.setVoted(false);
        menteeRepository.save(mentee);
        requestRepository.deleteRequestsForMentee(mentee);
        return "redirect:/mentee";
    }
}
