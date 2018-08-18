package com.yvanscoop.ecommerce.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Client implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idClient;

    @Size(min = 4, max = 30,message = "entrez un nom de 4 à 30 caractères")
    @NotNull(message = "entrez votre nom")
    private String nomClient;

    @NotEmpty(message = "entrez votre adresse")
    private String adresse;

    @Email
    @NotEmpty(message = "entrez votre email")
    private String email;

    @NotNull(message = "entrez votre numero de telephone")
    @Min(value = 650000000,message = "entrez un numero a 9 chiffres commençant par (65,66,67,68,69)")
    @Max(value = 699999999,message = "entrez un numero a 9 chiffres commençant par (67,68,69")
    private int tel;

    @OneToMany(mappedBy = "client")
    private Collection<Commande> commandes;

}
