package com.c.p.controller;

import com.c.p.model.Role;
import com.c.p.model.User;
import com.c.p.repository.RoleRepository;
import com.c.p.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class LoginController {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerGet(){
        return "register";
    }

    @PostMapping("/register")
    public String registerPost(@ModelAttribute("user")User user, HttpServletRequest request) throws ServletException {
        String password = user.getPassword();
        String username = user.getUsername();
        String email = user.getEmail();
        Optional<User> chkUser = userRepository.findUserByUsername(username);
        User chk2User = userRepository.findUserByEmail(email);
        if(chkUser.isEmpty()&&chk2User==null) {
            user.setPassword(bCryptPasswordEncoder.encode(password));
            List<Role> roles = new ArrayList<>();
            roles.add(roleRepository.findById(2).get());
            user.setRoles(roles);
            userRepository.save(user);
            request.login(username, password);
            return "redirect:/registration/success";
        }
        else {
            return "redirect:/registration/failure";
        }
    }

    @GetMapping("/success")
    public String success() {
        return "loginSuccess";
    }
    @GetMapping("/failure")
    public String unsuccess() {
        return "loginUnsuccessful";
    }
    @GetMapping("/registration/success")
    public String rsuccess() {
        return "registrationSuccess";
    }
    @GetMapping("/registration/failure")
    public String rusuccess() {
        return "registrationUnsuccessful";
    }

}
