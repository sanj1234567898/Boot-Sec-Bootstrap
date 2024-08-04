package com.spring.security.app.springsecurityapp.controllers;

import com.spring.security.app.springsecurityapp.helpers.ControllerHelpers;
import com.spring.security.app.springsecurityapp.models.Role;
import com.spring.security.app.springsecurityapp.models.User;
import com.spring.security.app.springsecurityapp.services.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminControllers {
    private DataService dataService;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AdminControllers(BCryptPasswordEncoder passwordEncoder, DataService dataService) {
        this.dataService = dataService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String getAdminPage(Model model, Principal principal) {
        List<User> userList = (List<User>) dataService.findAll();
        List<Role> roleSet = (List<Role>) dataService.findAllRoles();
        User emptyUser = new User();

        ControllerHelpers.findUserByEmail(model, principal, dataService);

        model.addAttribute("roles", roleSet);
        model.addAttribute("users", userList);
        model.addAttribute("emptyUser", emptyUser);
        return "views/admin/admin-page";
    }

    @PostMapping("/create")
    public String create(
            @RequestParam("firstname") String firstName,
            @RequestParam("lastname") String lastName,
            @RequestParam("age") int age,
            @RequestParam("password") String password,
            @RequestParam("email") String email,
            @RequestParam(value = "roles", required = false) Set<String> roles
    ) {
        User user = new User();

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setAge(age);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        Set<Role> roleSet = ControllerHelpers.addRolesForUser(roles, dataService);
        user.setRoles(roleSet);

        dataService.save(user);

        return "redirect:/admin";
    }

    @PostMapping("/edit")
    public String edit(
            @RequestParam("id") long id,
            @RequestParam("firstname") String firstName,
            @RequestParam("lastname") String lastName,
            @RequestParam("age") int age,
            @RequestParam("password") String password,
            @RequestParam("email") String email,
            @RequestParam(value = "roles", required = false) Set<String> roleNames) {

        Optional<User> optionalUser = dataService.findById(id);
        if (optionalUser.isEmpty()) {
            // Добавьте логирование или сообщение об ошибке
            return "redirect:/admin?error=user_not_found";
        }
        User user = optionalUser.get();

        // Обновление полей пользователя
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setAge(age);
        user.setEmail(email);
        if (!password.equals(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(password));
        }

        // Создание нового множества для ролей пользователя
        Set<Role> newRoleSet = new HashSet<>();

        if (roleNames != null) {
            // Пройдитесь по всем переданным именам ролей и добавьте их
            for (String roleName : roleNames) {
                dataService.findRoleByName(roleName).ifPresent(newRoleSet::add);
            }
        }

        // Удаление ролей, которых нет в переданных ролях
        user.getRoles().removeIf(userRole -> !newRoleSet.contains(userRole));

        // Добавление новых ролей пользователю
        user.getRoles().addAll(newRoleSet);

        // Сохранение пользователя
        dataService.save(user);

        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("id") long id) {
        dataService.deleteById(id);

        return "redirect:/admin";
    }
}
