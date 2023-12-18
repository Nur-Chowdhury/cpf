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
public class MenteeController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    MenteeRepository menteeRepository;

    @Autowired
    MentorRepository mentorRepository;

    @Autowired
    RequestRepository requestRepository;

    @GetMapping("/mentee")
    public String mentee(Model model, Principal principal){
        if(principal!=null){
            User user = userRepository.findUserByUsername(principal.getName()).get();
            int id = user.getId();
            model.addAttribute("loggedid", id);
        }
        User user = userRepository.findUserByUsername(principal.getName()).get();
        System.out.println(user.getUsername());
        List<Role> roles = user.getRoles();
        boolean isMentee = false;
        for (Role role : roles) {
            if ("ROLE_MENTEE".equals(role.getName())) {
                isMentee = true;
                break;
            }
        }
        if(isMentee){
            Mentee mentee = menteeRepository.findByUserUsername(user.getUsername()).get();
            if(mentee.getHaveMentor())  return "redirect:/mentors";
            else return "redirect:/mentors/list";
        }
        else return "menteeform";
    }

    @PostMapping("/mentee/submit")
    public String submitMentee(@RequestParam String about, @RequestParam String lackings, Authentication authentication) {
        if (authentication != null) {
            String username = authentication.getName();

            Optional<User> optionalUser = userRepository.findUserByUsername(username);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                List<Role> roles = user.getRoles();
                roles.add(roleRepository.findById(4).get());
                user.setRoles(roles);
                userRepository.save(user);
                Mentee mentee = new Mentee();
                mentee.setUser(user);
                mentee.setAbout(about);
                mentee.setLackings(lackings);
                mentee.setHaveMentor(false);
                mentee.setVoted(false);
                menteeRepository.save(mentee);
                return "redirect:/";
            } else {
                return "login";
            }
        } else {
            return "login";
        }
    }

    @GetMapping("/mentee/list")
    public String mentees(Model model, Principal principal){
        if(principal!=null){
            User user = userRepository.findUserByUsername(principal.getName()).get();
            int id = user.getId();
            model.addAttribute("loggedid", id);
        }
        Mentor mentor = mentorRepository.findByUserUsername(principal.getName()).get();
        List<Mentee> mentees = new ArrayList<>();
        List<Mentee> myMentees = mentor.getMentees();
        List<Mentee> men = menteeRepository.findAll();
        for (Mentee me : men) {
            if(!me.getHaveMentor()&&mentor.getUser().getId()!=me.getUser().getId()){
                mentees.add(me);
            }
        }
        model.addAttribute("mentees", mentees);
        model.addAttribute("mentor", mentor);
        model.addAttribute("myMentees", myMentees);
        return "menteeList";
    }

    @GetMapping("/mentee/{id}")
    public String setMentees(@PathVariable int id, Principal principal){
        Mentor mentor = mentorRepository.findByUserUsername(principal.getName()).get();
        Optional<Mentee> oMentee = menteeRepository.findById(id);
        if(oMentee.isPresent()) {
            Mentee mentee = oMentee.get();
            Request request = new Request();
            request.setMentee(mentee);
            request.setMentor(mentor);
            requestRepository.save(request);
            return "redirect:/req/success";
        }
        else return "redirect:/error";
    }

    @GetMapping("/mentee/delete/{id}")
    @Transactional
    public String deletem(@PathVariable int id, Principal principal){
        Mentee mentee = menteeRepository.findById(id).get();
        mentee.removeMentor();
        mentee.setHaveMentor(false);
        mentee.setVoted(false);
        menteeRepository.save(mentee);
        requestRepository.deleteRequestsForMentee(mentee);
        return "redirect:/mentee/list";
    }
}
