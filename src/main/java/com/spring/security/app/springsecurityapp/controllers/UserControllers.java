package com.spring.security.app.springsecurityapp.controllers;

import com.spring.security.app.springsecurityapp.helpers.ControllerHelpers;
import com.spring.security.app.springsecurityapp.services.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserControllers {
    private DataService dataService;

    @Autowired
    public UserControllers(DataService dataService) {
        this.dataService = dataService;
    }
    @GetMapping
    public String getUserPage(Model model, Principal principal) {
        ControllerHelpers.findUserByEmail(model, principal, dataService);
        return "views/user/user-page";
    }
}
