package com.yvanscoop.ecommerce.controllers;

import com.yvanscoop.ecommerce.entities.Role;
import com.yvanscoop.ecommerce.entities.User;
import com.yvanscoop.ecommerce.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@Controller
public class UserControllerMVC {
    @Autowired
    private UserService userService;


    @Secured(value = {"ROLE_SUPERADMIN"})
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String index(Model model,
                        @RequestParam(name = "motcle", defaultValue = "") String motcle,
                        @RequestParam(name = "page", defaultValue = "0") int page,
                        @RequestParam(name = "size", defaultValue = "5") int size,
                        @RequestParam(name = "msg", defaultValue = "") String msg,
                        @RequestParam(name = "msg1",defaultValue = "") String msg1) {
        Page<User> users = userService.findAll("%" + motcle + "%", page, size);
        model.addAttribute("listeUsers", users.getContent());
        model.addAttribute("msg", msg);
        model.addAttribute("msg1", msg1);

        int[] pages = new int[users.getTotalPages()];
        model.addAttribute("pages", pages);
        model.addAttribute("total", users.getTotalElements());
        model.addAttribute("pageCourante", page);
        //le mot cle
        model.addAttribute("motcle", motcle);
        return "users";
    }

    @Secured(value = {"ROLE_SUPERADMIN"})
    @RequestMapping(value = "/formUser", method = RequestMethod.GET)
    public String ajouterUser(Model model) {
        model.addAttribute("user", new User());
        return "ajoutUser";
    }

    @Secured(value = {"ROLE_SUPERADMIN"})
    @RequestMapping(value = "/ajoutUser", method = RequestMethod.POST)
    public String saveUser(@Valid User user, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            userService.save(user);
            return "redirect:/users";
        } else {
            return "ajoutUser";
        }
    }

    @Secured(value = {"ROLE_ADMIN","ROLE_SUPERADMIN","ROLE_USER"})
    @RequestMapping(value = "/form1User", method = RequestMethod.GET)
    public String modifierUser(Model model,HttpSession httpSession) {
        SecurityContext securityContext = (SecurityContext) httpSession.getAttribute("SPRING_SECURITY_CONTEXT");
        String username = securityContext.getAuthentication().getName();
        User user = userService.findUser(username);
        model.addAttribute("user", user);
        model.addAttribute("pass", user.getPassword());
        model.addAttribute("id", user.getId());
        return "modifUser";
    }

    @Secured(value = {"ROLE_ADMIN","ROLE_SUPERADMIN","ROLE_USER"})
    @RequestMapping(value = "/modifUser", method = RequestMethod.POST)
    public String updateUser(Model model,@Valid User user, BindingResult bindingResult,
                                @RequestParam String oldPass,
                                @RequestParam String oldPassVerif,
                             @RequestParam long id) {
        model.addAttribute("pass",oldPass);
        model.addAttribute("id", id);
        if (oldPass.equals(oldPassVerif)) {
            if (!bindingResult.hasErrors()) {
                user.setId(id);
                userService.update(user);
                return "redirect:/users";
            } else {
                return "modifUser";
            }
        }else{
            model.addAttribute("msg", "ancien mot de passe incorrect");
            return "modifUser";
        }


    }

    @Secured(value = {"ROLE_SUPERADMIN"})
    @RequestMapping(value = "/suppUser", method = RequestMethod.GET)
    public String supprimer(@RequestParam(name = "id") long id,HttpSession httpSession) {
        //recherche les roles de l'utilisateur en cours
        SecurityContext securityContext = (SecurityContext) httpSession.getAttribute("SPRING_SECURITY_CONTEXT");
        List<String> my_roles = new ArrayList<>();
        if (!securityContext.getAuthentication().getAuthorities().isEmpty()){
            for (GrantedAuthority ga: securityContext.getAuthentication().getAuthorities()){
                my_roles.add(ga.getAuthority());
            }
        }

        //l'utilisateur à supprimer et ses roles
        User user = userService.findOne(id);
        List<Role> roles = (List<Role>) user.getRoles();
        List<String> roles_array = new ArrayList<>();
        for(Role role : roles) {
            roles_array.add(role.getRole());
        }

        // les messages à renvoyer
        String msg = "";
        String msg1 = "";

        // analyse des differents cas possibles pour la suppression
       if (roles_array.isEmpty()){
            userService.delete(id);
            msg1 = "il n'avait aucun droit d'utilisateur";
            return "redirect:/users?msg1="+msg1;
       }else if (roles_array.contains("SUPERADMIN")){
           msg = "vous ne pouvez pas supprimer un ";
           msg1 = "super administrateur";
           return "redirect:/users?msg1="+msg1+"&msg="+msg;
       }else if (roles_array.contains("ADMIN") && my_roles.contains("ROLE_SUPERADMIN")){
           userService.delete(id);
           msg1 = "il avait des droits d'admin";
           return "redirect:/users?msg1="+msg1;
       }else {
           userService.delete(id);
           msg1 = "il n'avait aucun droit d'admin";
           return "redirect:/users?msg1="+msg1;
       }
    }
}
