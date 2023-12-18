package com.c.p.controller;


import com.c.p.model.Contest;
import com.c.p.model.Problem;
import com.c.p.model.Role;
import com.c.p.model.User;
import com.c.p.repository.RoleRepository;
import com.c.p.repository.UserRepository;
import com.c.p.service.ContestService;
import com.c.p.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminContrloller {

    @Autowired
    ContestService contestService;

    @Autowired
    ProblemService problemService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @GetMapping("/admin")
    public String adminHome(){
        return "adminHome";
    }
    @GetMapping("/admin/contests")
    public String getCon(Model model){
        model.addAttribute("contests", contestService.getAllContest());
        return "manageContests";
    }
    @GetMapping("/admin/contests/add")
    public String getConAdd(Model model){
        model.addAttribute("contest", new Contest());
        return "addContest";
    }

    @PostMapping("/admin/contests/add")
    public String postConAdd(@ModelAttribute("contest") Contest contest){
        contestService.addContest(contest);
        return "redirect:/admin/contests";
    }

    @GetMapping("/admin/contests/delete/{id}")
    public String deleteCon(@PathVariable int id){
        contestService.removeContestById(id);
        return "redirect:/admin/contests";
    }

    @GetMapping("/admin/contests/update/{id}")
    public String updateCon(@PathVariable int id, Model model){
        Optional<Contest> contest = contestService.getContestById(id);
        if(contest.isPresent()){
            model.addAttribute("contest", contest.get());
            return "addContest";
        }
        else
            return "404";
    }

    //Problem Section

    @GetMapping("/admin/problems")
    public String problems(Model model){
        model.addAttribute("problems", problemService.getAllProblem());
        return "manageProblems";
    }

    @GetMapping("/admin/problems/add")
    public String probAddGet(Model model){
        model.addAttribute("problem", new Problem());
        return "problemAdd";
    }

    @PostMapping("/admin/problems/add")
    public String postProbAdd(@ModelAttribute("problem") Problem problem){
        problemService.addProblem(problem);
        return "redirect:/admin/problems";
    }

    @GetMapping("/admin/problems/delete/{id}")
    public String deletePro(@PathVariable int id){
        problemService.removeProblemById(id);
        return "redirect:/admin/problems";
    }

    @GetMapping("/admin/problems/update/{id}")
    public String updatePro(@PathVariable int id, Model model){
        Optional<Problem> problem = problemService.getProblemById(id);
        if(problem.isPresent()){
            model.addAttribute("problem", problem.get());
            return "problemAdd";
        }
        else
            return "404";
    }

    @GetMapping("/admin/users")
    public String users(Model model){
        model.addAttribute("users", userRepository.findAll());
        return "manageUsers";
    }

    @GetMapping("/admin/user/make/{id}")
    public String makeAdmin(@PathVariable int id){
        User user = userRepository.findById(id).get();
        List<Role> roles = user.getRoles();
        Role role = roleRepository.findById(1).get();
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/user/remove/{id}")
    public String removeAdmin(@PathVariable int id, Principal principal){
        User us = userRepository.findUserByUsername(principal.getName()).get();
        User user = userRepository.findById(id).get();
        if(user!=us){
            user.getRoles().removeIf(role -> role.getId().equals(1));
            userRepository.save(user);
        }
        return "redirect:/admin/users";
    }
}














