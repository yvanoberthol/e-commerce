package com.yvanscoop.ecommerce.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Commande implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long idCommande;

    @Basic
    @Temporal(TemporalType.DATE)
    private Date dateCommande;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="idClient")
    private Client client;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="idCommande")
    private Collection<LigneCommande> ligneCommandes;
}
