package com.yvanscoop.ecommerce.repositories;

import com.yvanscoop.ecommerce.entities.Produit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProduitRepository extends JpaRepository<Produit,Long> {
    @Query("select p from Produit p where p.designation like :motcle or p.description like :motcle")
    public Page<Produit> listeProduitsParMotcle(@Param(value = "motcle") String mot, Pageable pageable);

    @Query("select p from Produit p where p.selectionne = 1 and p.categorie in (select c from Categorie c where c.nomCategorie like :cat )")
    public Page<Produit> listeProduitsParCategorie(@Param(value = "cat") String categorie,Pageable pageable);
}
