package com.yvanscoop.ecommerce.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Categorie implements Serializable {
    @Id
    @GeneratedValue
    private Long idCategorie;

    @NotEmpty(message = "entrez un nom de categorie")
    @Size(min = 4,max = 25)
    private String nomCategorie;

    @NotEmpty(message = "entrez une description de la catégorie")
    private String description;

    private String Photo;

    @JsonIgnore
    @OneToMany(mappedBy = "categorie")
    private Collection<Produit> produits = new ArrayList<Produit>();
}
