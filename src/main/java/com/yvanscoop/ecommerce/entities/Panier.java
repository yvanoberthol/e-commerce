package com.yvanscoop.ecommerce.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Panier implements Serializable {
    private Map<Long,LigneCommande> items = new HashMap<Long, LigneCommande>();
    public void addItem(Produit produit, int quantite){
        LigneCommande ligneCommande = items.get(produit.getIdProduit());
        if (ligneCommande == null){
            LigneCommande art = new LigneCommande();
            art.setProduit(produit);
            art.setQuantite(quantite);
            art.setPrix(produit.getPrix());
            items.put(produit.getIdProduit(),art);
        }else {
            ligneCommande.setQuantite(ligneCommande.getQuantite()+quantite);
        }
    }

    public Collection<LigneCommande> getItems(){
        return items.values();
    }

    public int getSize(){
        return items.size();
    }

    public double getTotal(){
        double total = 0;
        for (LigneCommande ligneCommande : items.values()){
            total +=ligneCommande.getPrix()*ligneCommande.getQuantite();
        }
        return total;
    }

    public void deleteItem(long idProduit){
        items.remove(idProduit);
    }
}
