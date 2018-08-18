package com.yvanscoop.ecommerce.repositories;

import com.yvanscoop.ecommerce.entities.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClientRepository extends JpaRepository<Client,Long> {
    @Query("select c from Client c where c.nomClient like :nom  order by c.idClient")
    public Page<Client> chercher(@Param("nom") String nom, Pageable pageable);
    public Client getByNomClient(String name);
}
