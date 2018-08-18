package com.yvanscoop.ecommerce.services;

import com.yvanscoop.ecommerce.entities.Role;
import com.yvanscoop.ecommerce.repositories.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleService {

    private static final Logger log = LoggerFactory.getLogger(RoleService.class);

    @Autowired
    private RoleRepository roleRepository;

    //ajouter un role
    public Role save(Role role) {
        log.debug("Request to save Role : {}", role);
        return roleRepository.save(role);
    }

    //chercher un role par son id
    @Transactional(readOnly = true)
    public Role findOne(Long id) {
        log.debug("Request to get Role : {}", id);
        Role role = roleRepository.getOne(id);

        return role;
    }

    //mettre a jour les roles
    public Role update(Role role) {
        log.debug("Request to update Role : {}", role);
        Role r = new Role();
        r = roleRepository.getOne(r.getId());
        r.setRole(role.getRole());
        Role result = roleRepository.save(r);

        return result;
    }

    //supprimer un role
    public void delete(Long id) {
        log.debug("Request to delete Role : {}", id);
        Role role = roleRepository.getOne(id);

        roleRepository.delete(role);
    }

    // avoir la liste des roles par page et par taille
    @Transactional(readOnly = true)
    public Page<Role> findAll(String nom, int page, int size) {
        log.debug("Request to get all Categories");

        return roleRepository.chercher("%"+nom+"%", new PageRequest(page, size));
    }

    // avoir la liste des roles
    @Transactional(readOnly = true)
    public List<Role> getAll() {
        log.debug("Request to get all Categories");

        return roleRepository.findAll();
    }
}
