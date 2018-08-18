package com.yvanscoop.ecommerce.repositories;

import com.yvanscoop.ecommerce.entities.Categorie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategorieRepository extends JpaRepository<Categorie,Long> {
    @Query("select c from Categorie c where c.nomCategorie like :motcle")
    Page<Categorie> listeCategoriesParMotcle(@Param(value = "motcle") String mot, Pageable pageable);

    @Query("select count(p) from Produit p where p.categorie in (select c from Categorie c where c.nomCategorie =:nom)")
    int countProduitFromCategorie(@Param("nom") String nom);
}
