package com.yvanscoop.ecommerce.services;

import com.yvanscoop.ecommerce.entities.Client;
import com.yvanscoop.ecommerce.repositories.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClientService {

    private static final Logger log = LoggerFactory.getLogger(ClientService.class);

    @Autowired
    private ClientRepository clientRepository;


    //ajouter un client
    public Client save(Client client){
        /*System.out.println(client.getPassword());
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String passHash = passwordEncoder.encode(client.getPassword());
        client.setPassword(passHash);
        System.out.println(client.getPassword());*/
        log.debug("Request to save Client : {}", client);

        return clientRepository.save(client);
    }

    //chercher un client par son id
    @Transactional(readOnly = true)
    public Client findOne(Long id) {
        log.debug("Request to get Client : {}", id);
        Client client = clientRepository.getOne(id);

        return client;
    }

    //chercher un client par son clientname
    @Transactional(readOnly = true)
    public Client findClient(String name) {
        log.debug("Request to get Client : {}", name);

        return clientRepository.getByNomClient(name);
    }


    //mettre a jour les clients
    public Client update(Client client) {
        log.debug("Request to update Client : {}", client);
        Client u = new Client();
        u.setIdClient(client.getIdClient());
        u.setEmail(client.getEmail());
        u.setAdresse(client.getAdresse());
        u.setTel(client.getTel());


        return clientRepository.save(u);
    }

    //supprimer un client
    public void delete(Long id) {
        log.debug("Request to delete Client : {}", id);
        Client client = clientRepository.getOne(id);

        clientRepository.delete(client);
    }

    // avoir la liste des clients par page et par taille
    @Transactional(readOnly = true)
    public Page<Client> findAll(String nom, int page, int size) {
        log.debug("Request to get all Categories");
        // Sort triparnom = new Sort(Sort.Direction.DESC,"designation");
        //List<Client> clients = new ArrayList<>();

        return clientRepository.chercher(nom+"%",new PageRequest(page,size));
    }

}
