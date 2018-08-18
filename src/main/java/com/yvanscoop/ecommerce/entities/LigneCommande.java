package com.yvanscoop.ecommerce.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class LigneCommande implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "idProduit")
    private Produit produit;

    @Positive(message = "entrez un prix supérieur à 0")
    private int quantite;

    @PositiveOrZero(message = "entrez une quantite supérieur ou égal à 0")
    private double prix;

}
