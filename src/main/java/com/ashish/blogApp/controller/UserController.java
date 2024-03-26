package com.ashish.blogApp.controller;

import com.ashish.blogApp.entity.User;
import com.ashish.blogApp.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Controller

public class UserController {
    private UserService userService;

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/processRegistrationForm")
    public String processRegistrationForm(
            @Valid @ModelAttribute("user") User user,
            BindingResult theBindingResult,
            HttpSession session, Model model) {
        String userName = user.getName();
        if (theBindingResult.hasErrors()) {
            return "register";
        }
        User existing = userService.findUserByUserName(userName);
        if (existing != null) {
            model.addAttribute("user", new User());
            model.addAttribute("registrationError", "User name already exists.");
            return "register";
        }
        user.setRole("AUTHOR");
        userService.saveUser(user);
        session.setAttribute("user", user);
        return "register";
    }
}
