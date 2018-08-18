package com.yvanscoop.ecommerce.controllers;


import com.yvanscoop.ecommerce.entities.Role;
import com.yvanscoop.ecommerce.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
public class RoleControllerMVC {

    @Autowired
    private RoleService roleService;

    @Secured(value = {"ROLE_ADMIN","ROLE_SUPERADMIN"})
    @RequestMapping(value = "/roles", method = RequestMethod.GET)
    public String index(Model model,
                        @RequestParam(name = "motcle", defaultValue = "") String motcle,
                        @RequestParam(name = "page", defaultValue = "0") int page,
                        @RequestParam(name = "size", defaultValue = "5") int size,
                        @RequestParam(name = "msg", defaultValue = "") String msg) {
        Page<Role> roles = roleService.findAll("%" + motcle + "%",page, size);
        model.addAttribute("listeRoles", roles.getContent());

        int[] pages = new int[roles.getTotalPages()];
        model.addAttribute("pages", pages);
        model.addAttribute("pageCourante", page);
        model.addAttribute("msg", msg);
        //le mot cle
        model.addAttribute("motcle", motcle);
        return "roles";
    }

    @Secured(value = {"ROLE_SUPERADMIN"})
    @RequestMapping(value = "/formRole", method = RequestMethod.GET)
    public String ajouterRole(Model model) {
        Role role = new Role();
        model.addAttribute("role", role);
        return "ajoutRole";
    }

    @Secured(value = {"ROLE_SUPERADMIN"})
    @RequestMapping(value = "/ajoutRole", method = RequestMethod.POST)
    public String saveRole(@Valid @ModelAttribute("form") Role r, BindingResult bindingResult) {

        //@RequestBody form in accept-charset="ISO-8859-1"
        if (!bindingResult.hasErrors()) {
            roleService.save(r);
            return "redirect:/roles";
        } else {
            return "ajoutRole";
        }
    }

    @Secured(value = {"ROLE_SUPERADMIN"})
    @RequestMapping(value = "/suppRole", method = RequestMethod.GET)
    public String supprimer(@RequestParam(name = "id") long id) {
        Role role = roleService.findOne(id);
        String msg = "";
        if (role.getRole().equals("SUPERADMIN")){
            msg= "Impossible de supprimer ce role";
        }else {
            roleService.delete(id);
        }

        return "redirect:/roles?msg="+msg;
    }

}
