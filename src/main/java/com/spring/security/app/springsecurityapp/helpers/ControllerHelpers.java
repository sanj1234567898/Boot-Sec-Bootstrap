package com.spring.security.app.springsecurityapp.helpers;

import com.spring.security.app.springsecurityapp.models.Role;
import com.spring.security.app.springsecurityapp.models.User;
import com.spring.security.app.springsecurityapp.services.DataService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ControllerHelpers {
    public static void findUserByEmail(Model model, Principal principal, DataService dataService) {
        Optional<User> optionalUser = dataService.findUserByEmail(principal.getName());
        User user;

        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            throw new UsernameNotFoundException(principal.getName() + " is not found");
        }


        model.addAttribute("user", user);
    }

    public static Set<Role> addRolesForUser(Set<String> roleNames, DataService dataService) {
        Set<Role> roleSet = new HashSet<>();
        if (roleNames != null) {
            for (String roleName : roleNames) {
                dataService.findRoleByName(roleName).ifPresent(roleSet::add);
            }
        }
        return roleSet;
    }
}
