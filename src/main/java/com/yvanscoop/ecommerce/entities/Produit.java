package com.yvanscoop.ecommerce.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Produit implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProduit;

    @NotEmpty(message = "Entrez une désignation de ce produit")
    private String designation;

    private String description;

    @NotNull(message = "Entrez une désignation de ce produit")
    @Positive(message = "entrez un prix supérieur à 0")
    private double prix;
    private String photo;

    @PositiveOrZero(message = "entrez une quantite supérieur ou égal à 0")
    private int quantite;
    private boolean selectionne;

    @ManyToOne
    @JoinColumn(name = "idCategorie")
    private Categorie categorie;

}
