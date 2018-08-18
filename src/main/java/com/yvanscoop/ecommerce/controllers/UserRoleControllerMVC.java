package com.yvanscoop.ecommerce.controllers;

import com.yvanscoop.ecommerce.entities.Role;
import com.yvanscoop.ecommerce.entities.User;
import com.yvanscoop.ecommerce.services.RoleService;
import com.yvanscoop.ecommerce.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@Controller
public class UserRoleControllerMVC {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Secured(value = {"ROLE_SUPERADMIN"})
    @RequestMapping(value = "/userRoles", method = RequestMethod.GET)
    public String index(Model model,
                        @RequestParam(name = "motcle", defaultValue = "") String motcle,
                        @RequestParam(name = "page", defaultValue = "0") int page,
                        @RequestParam(name = "size", defaultValue = "5") int size,
                        @RequestParam(name = "msg", defaultValue = "") String msg,
                        @RequestParam(name = "msg1",defaultValue = "") String msg1) {
        Page<User> users = userService.findAll("%" + motcle + "%", page, size);
        model.addAttribute("users", users.getContent());
        model.addAttribute("msg", msg);
        model.addAttribute("msg1", msg1);

        int[] pages = new int[users.getTotalPages()];
        model.addAttribute("pages", pages);
        model.addAttribute("pageCourante", page);
        //le mot cle
        model.addAttribute("motcle", motcle);
        return "userRoles";
    }

   @Secured(value = {"ROLE_SUPERADMIN"})
    @RequestMapping(value = "/formUserRole", method = RequestMethod.GET)
    public String ajouterUserRole(Model model) {
        List<User> users = userService.getAll();
        List<Role> roles = roleService.getAll();

        model.addAttribute("roles", roles);
        model.addAttribute("users", users);
        return "ajoutUserRole";
    }

    @Secured(value = {"ROLE_SUPERADMIN"})
    @RequestMapping(value = "/ajoutUserRole", method = RequestMethod.POST)
    public String saveUserRole(long user_id, long roles_id, Model model) {
        User user = userService.findOne(user_id);
        Role role = roleService.findOne(roles_id);
        List<User> users = userService.getAll();
        List<Role> roles = roleService.getAll();

        model.addAttribute("roles", roles);
        model.addAttribute("users", users);

        if (user.getRoles().isEmpty() || !user.getRoles().contains(role)) {
            user.getRoles().add(role);
            userService.save(user);
            model.addAttribute("msg", "rôle bien ajouté");
        } else {
            model.addAttribute("username", user.getUsername());
            model.addAttribute("role", role.getRole());
            model.addAttribute("msg1", " a déja un role ");
        }

        return "ajoutUserRole";
    }

    @Secured(value = {"ROLE_SUPERADMIN"})
    @RequestMapping(value = "/suppUserRole", method = RequestMethod.GET)
    public String supprimer(long user_id, long role_id, HttpSession httpSession) {
        SecurityContext securityContext = (SecurityContext) httpSession.getAttribute("SPRING_SECURITY_CONTEXT");
        String user_current = securityContext.getAuthentication().getName();

        User user = userService.findOne(user_id);
        Role role = roleService.findOne(role_id);


        String msg = "";
        String msg1 = "";
        List<Role> roles = (List<Role>) user.getRoles();
        List<String> roles_array = new ArrayList<>();
        for(Role rolee : roles) {
            roles_array.add(rolee.getRole());
        }

        if(roles_array.contains("SUPERADMIN") && !user.getUsername().equals(user_current)){
            msg = "c'est un ";
            msg1 = "super administrateur";
        }else{
            if(user.getRoles().contains(role)) {
                user.getRoles().remove(role);
                userService.save(user);
            }
        }
        return "redirect:/userRoles?msg1="+msg1+"&msg="+msg;
    }
}
