package com.c.p.controller;

import com.c.p.model.*;
import com.c.p.repository.*;
import com.c.p.service.ContestService;
import com.c.p.service.ProblemService;
import com.c.p.service.QueryService;
import com.c.p.service.RequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller
public class ParentController {

    @Autowired
    ContestService contestService;
    @Autowired
    ProblemService problemService;

    @Autowired
    QueryService queryService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MentorRepository mentorRepository;

    @Autowired
    MenteeRepository menteeRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    RequestRepository requestRepository;
    @GetMapping("/")
    public String show(Model model, Principal principal) {
        if(principal!=null){
            User user = userRepository.findUserByUsername(principal.getName()).get();
            int id = user.getId();
            model.addAttribute("loggedid", id);
        }
        return "index";
    }

    @GetMapping("/contests/add")
    public String notif(Model model){
        model.addAttribute("contest", new Contest());
        return "notify";
    }

    @GetMapping("/access-denied")
    public String access(){
        return "access";
    }

    @PostMapping("/contests/add")
    public String postConAdd(@ModelAttribute("contest") Contest contest, Authentication authentication) {
        if (authentication != null) {
            String username = authentication.getName();
            Optional<User> optionalUser = userRepository.findUserByUsername(username);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                contest.setStatus(false);
                contest.setDownVote(0);
                contest.setUpVote(0);
                contest.setUsername(user.getUsername());
                contestService.addContest(contest);
                return "redirect:/contests/success";
            } else return "index";
        }
        else return "login";
    }

    @GetMapping("/contests/success")
    public String contestS(){
        return "addSuccess";
    }
    @GetMapping("/contests")
    public String contests(Model model, Principal principal){
        if(principal!=null){
            User user = userRepository.findUserByUsername(principal.getName()).get();
            int id = user.getId();
            model.addAttribute("loggedid", id);
        }
        model.addAttribute("contests", contestService.getAllContest());
        return "contests";
    }
    @GetMapping("/problems")
    public String problems(Model model, Principal principal){
        if(principal!=null){
            User user = userRepository.findUserByUsername(principal.getName()).get();
            int id = user.getId();
            model.addAttribute("loggedid", id);
        }
        model.addAttribute("problems", problemService.getAllProblem());
        return "problems";
    }

    @GetMapping("/query")
    public String showQueryForm(Model model, Principal principal) {
        if(principal!=null){
            User user = userRepository.findUserByUsername(principal.getName()).get();
            int id = user.getId();
            model.addAttribute("loggedid", id);
        }
        return "query";
    }

    @PostMapping("/query/submit")
    public String submitQuery(@RequestParam String content, Authentication authentication) {
        if (authentication != null) {
            String username = authentication.getName();
            System.out.println("Logged-in username: " + username);

            // You can also get other details like authorities, etc.
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            System.out.println("Authorities: " + authorities);

            // Further processing...

            Optional<User> optionalUser = userRepository.findUserByUsername(username);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                Query query = new Query();
                query.setContent(content);
                query.setUser(user);
                query.setUserEmail(user.getEmail());
                queryService.save(query);
                return "qSuccess";
            } else {
                return "login";
            }
        } else {
            return "login";
        }
    }

    @GetMapping("/contests/upvote/{id}")
    public String upvote(@PathVariable int id, Principal principal, Model model) {
        Optional<Contest> optionalContest = contestService.getContestById(id);
        if (optionalContest.isPresent()) {
            Contest contest = optionalContest.get();

            User user = userRepository.findUserByUsername(principal.getName()).orElse(null);
            if (user != null && !user.getVotedContests().contains(contest)) {
                user.getVotedContests().add(contest);
                contest.getVoters().add(user);
                Integer up = contest.getUpVote();
                Integer down = contest.getDownVote();
                contest.setUpVote(up + 1);
                if (up + 1 - down >= 10) {
                    Optional<User> oUser = userRepository.findUserByUsername(contest.getUsername());
                    if (oUser.isPresent()) {
                        User uSer = oUser.get();
                        uSer.setTrustPoint(uSer.getTrustPoint() + 1);
                    } else {
                        contest.setUsername(null);
                    }
                    contest.setStatus(true);
                }
                userRepository.save(user);
                contestService.addContest(contest);
                model.addAttribute("voted", false);
            } else {
                model.addAttribute("voted", true);
            }
        }
        // Return the name of the Thymeleaf template
        return "redirect:/contests";
    }

    @GetMapping("/contests/downvote/{id}")
    public String downVote(@PathVariable int id, Principal principal, Model model) {
        Optional<Contest> optionalContest = contestService.getContestById(id);
        if (optionalContest.isPresent()) {
            Contest contest = optionalContest.get();
            User user = userRepository.findUserByUsername(principal.getName()).orElse(null);
            if (user != null && !user.getVotedContests().contains(contest)) {
                user.getVotedContests().add(contest);
                contest.getVoters().add(user);
                Integer up = contest.getUpVote();
                Integer down = contest.getDownVote();
                contest.setDownVote(down + 1);
                if (down + 1 - up >= 10) {
                    Optional<User> oUser = userRepository.findUserByUsername(contest.getUsername());
                    if (oUser.isPresent()) {
                        User uSer = oUser.get();
                        uSer.setTrustPoint(uSer.getTrustPoint() + 1);
                    } else {
                        contest.setUsername(null);
                    }
                    contestService.removeContestById(id);
                } else {
                    userRepository.save(user);
                    contestService.addContest(contest);
                }
                model.addAttribute("voted", false);
            } else {
                model.addAttribute("voted", true);
            }
        }
        // Return the name of the Thymeleaf template
        return "redirect:/contests";
    }

    @GetMapping("/req/success")
    public String menreq(){
        return "reqSuccess";
    }
    @GetMapping("/acc/success")
    public String menacc(){
        return "accSuccess";
    }

    @GetMapping("/user/{id}")
    public String userProfile(@PathVariable Integer id, Model model, Principal principal) {
        if(principal!=null){
            User user = userRepository.findUserByUsername(principal.getName()).get();
            int iid = user.getId();
            model.addAttribute("loggedid", iid);
        }
        User user = userRepository.findById(id).get();
        List<Role> roles = user.getRoles();
        boolean isMentor = false;
        for (Role role : roles) {
            if ("ROLE_MENTOR".equals(role.getName())) {
                isMentor = true;
                break;
            }
        }
        model.addAttribute("isMentor", isMentor);
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        return "userprofile";
    }
}
