package com.yvanscoop.ecommerce.repositories;

import com.yvanscoop.ecommerce.entities.Commande;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandeRepository extends JpaRepository<Commande,Long> {
}
