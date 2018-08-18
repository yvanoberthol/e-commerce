package com.yvanscoop.ecommerce.services;

import com.yvanscoop.ecommerce.entities.User;
import com.yvanscoop.ecommerce.repositories.UserRepository;
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
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;


    //ajouter un user
    public User save(User user){
        /*System.out.println(user.getPassword());
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String passHash = passwordEncoder.encode(user.getPassword());
        user.setPassword(passHash);
        System.out.println(user.getPassword());*/
        log.debug("Request to save User : {}", user);

        return userRepository.save(user);
    }

    //chercher un user par son id
    @Transactional(readOnly = true)
    public User findOne(Long id) {
        log.debug("Request to get User : {}", id);
        User user = userRepository.getOne(id);

        return user;
    }

    //chercher un user par son username
    @Transactional(readOnly = true)
    public User findUser(String name) {
        log.debug("Request to get User : {}", name);
        User user = userRepository.getByUsername(name);

        return user;
    }


    //mettre a jour les users
    public User update(User user) {
        log.debug("Request to update User : {}", user);
        User u = new User();
        u.setId(user.getId());
        u.setUsername(user.getUsername());
        u.setPassword(user.getPassword());
        u.setActive(true);
        User result = userRepository.save(u);

        return result;
    }

    //supprimer un user
    public void delete(Long id) {
        log.debug("Request to delete User : {}", id);
        User user = userRepository.getOne(id);

        userRepository.delete(user);
    }

    // avoir la liste des users par page et par taille
    @Transactional(readOnly = true)
    public Page<User> findAll(String nom, int page, int size) {
        log.debug("Request to get all Categories");
        // Sort triparnom = new Sort(Sort.Direction.DESC,"designation");
        //List<User> users = new ArrayList<>();
        Page<User> users = userRepository.chercher(nom+"%",new PageRequest(page,size));

        return users;
    }

    // avoir la liste des users par page et par taille
    @Transactional(readOnly = true)
    public List<User> getAll() {
        log.debug("Request to get all Categories");
        // Sort triparnom = new Sort(Sort.Direction.DESC,"designation");
        //List<User> users = new ArrayList<>();
        List<User> users = userRepository.findAll();
        return users;
    }
}
