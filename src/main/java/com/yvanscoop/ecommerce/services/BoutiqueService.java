package com.yvanscoop.ecommerce.services;

import com.yvanscoop.ecommerce.entities.*;
import com.yvanscoop.ecommerce.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BoutiqueService {

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private CategorieRepository categorieRepository;

    @Autowired
    private CommandeRepository commandeRepository;

    @Autowired
    private ClientRepository clientRepository;


    //gestion des categories
    public Long ajouterCategorie(Categorie categorie){
        categorieRepository.save(categorie);
        return categorie.getIdCategorie();
    }

    public Page<Categorie> listeCategories(String nom,int page, int size){

        return categorieRepository.listeCategoriesParMotcle("%"+nom+"%",new PageRequest(page,size));
    }

    public List<Categorie> listCategories(){
        return categorieRepository.findAll();
    }

    public Categorie getCategorie(Long id){
        return categorieRepository.getOne(id);
    }

    public void supprimerCategorie(Long id)
    {
        categorieRepository.deleteById(id);
    }

    public void modifierCategorie(Categorie categorie){
        Categorie cat = categorieRepository.getOne(categorie.getIdCategorie());
        cat.setIdCategorie(categorie.getIdCategorie());
        cat.setDescription(categorie.getDescription());
        cat.setNomCategorie(categorie.getNomCategorie());
        cat.setPhoto(categorie.getPhoto());
        categorieRepository.save(cat);
    }

    //gestion des produits
    public Long ajouterProduit(Produit p){
        produitRepository.save(p);
        return p.getIdProduit();
    }

    public Page<Produit> listeProduitsParMC(String motcle,int page, int size){
        return produitRepository.listeProduitsParMotcle("%"+motcle+"%",new PageRequest(page,size));
    }

    public Page<Produit> listeProduitsParCat(String nomCat,int page, int size){
        return produitRepository.listeProduitsParCategorie("%"+nomCat+"%", new PageRequest(page,size));
    }

    public Produit getProduit(Long id){
        return produitRepository.getOne(id);
    }

    public void supprimerProduit(Long id){
        produitRepository.deleteById(id);
    }

    public void modifierProduit(Produit produit){
        Produit p = produitRepository.getOne(produit.getIdProduit());
        if (p.getIdProduit() != null){
            produitRepository.save(produit);
        }

    }

    public Commande ajouterCommande(Panier panier, Client client){

        Commande cmde = new Commande();
        cmde.setIdCommande(null);
        cmde.setClient(client);
        cmde.setDateCommande(new /Datetime());
        cmde.setLigneCommandes(panier.getItems());
        clientRepository.save(client);
        commandeRepository.save(cmde);
        return cmde;
    }

    public int countProduit(String nom){
        return categorieRepository.countProduitFromCategorie(nom);
    }

}
