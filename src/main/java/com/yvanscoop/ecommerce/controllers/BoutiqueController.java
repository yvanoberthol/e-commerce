package com.yvanscoop.ecommerce.controllers;

import com.yvanscoop.ecommerce.entities.*;
import com.yvanscoop.ecommerce.services.BoutiqueService;
import com.yvanscoop.ecommerce.services.ClientService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@Scope("session")
public class BoutiqueController {

    @Autowired
    private BoutiqueService boutiqueService;

    @Autowired
    private ClientService clientService;

    private static final Logger log = LoggerFactory.getLogger(BoutiqueController.class);

    //******************************************************************************//
    //**************************** CATEGORIE Controller ****************************//
    //******************************************************************************//

    @Secured(value = {"ROLE_ADMIN"})
    @RequestMapping("/categories")
    public String index(Model model,
                        @RequestParam(name = "nom", defaultValue = "") String code,
                        @RequestParam(name = "page", defaultValue = "0") int page,
                        @RequestParam(name = "size", defaultValue = "5") int size){
        Page<Categorie> categories = boutiqueService.listeCategories(code,page,size);
        model.addAttribute("categories", categories);
        int[] pages = new int[categories.getTotalPages()];
        model.addAttribute("pages", pages);
        model.addAttribute("total", categories.getTotalElements());
        model.addAttribute("pageCourante", page);
        model.addAttribute("code", code);
        return "categories";
    }

    @Secured(value = {"ROLE_ADMIN"})
    @RequestMapping("/categorie/form")
    public String formCategorie(Model model){
        model.addAttribute("categorie", new Categorie());
        return "ajoutCategorie";
    }

    @Secured(value = {"ROLE_ADMIN"})
    @RequestMapping(value = "/categorie/add",method = RequestMethod.POST)
    public String saveCategorie(@Valid Categorie categorie, BindingResult bindingResult, Model model, MultipartFile file,
                                @RequestParam(name = "nom", defaultValue = "") String code,
                                @RequestParam(name = "page", defaultValue = "0") int page,
                                @RequestParam(name = "size", defaultValue = "5") int size)
    throws Exception{
        if (bindingResult.hasErrors()){
            model.addAttribute("categories",boutiqueService.listeCategories(code,page,size));
            return "ajoutCategorie";
        }
        if (!file.isEmpty()) {
            String path = System.getProperty("java.io.tmpdir");
            categorie.setPhoto(file.getOriginalFilename());
            Long idC = null;

            if (categorie.getIdCategorie() == null) {
                idC = boutiqueService.ajouterCategorie(categorie);
            } else {
                boutiqueService.modifierCategorie(categorie);
                idC = categorie.getIdCategorie();
            }

            file.transferTo(new File(path + "/" + "cat_" + idC + "_" + file.getOriginalFilename()));
        }else{
            if (categorie.getIdCategorie() == null){
                boutiqueService.ajouterCategorie(categorie);
            }else{
                boutiqueService.modifierCategorie(categorie);
            }
        }

        return "redirect:/categories";
    }

    @Secured(value = {"ROLE_ADMIN"})
    @RequestMapping(value = "/categorie/photo", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getPhoto(Long idCat) throws IOException{
        Categorie categorie = boutiqueService.getCategorie(idCat);

        File f = new File(System.getProperty("java.io.tmpdir")+"/CAT_"+idCat+"_"+categorie.getPhoto( ));
        return IOUtils.toByteArray(new FileInputStream(f));
    }

    @Secured(value = {"ROLE_ADMIN"})
    @RequestMapping(value = "/categorie/delete", method = RequestMethod.DELETE)
    public String suppCat(Long idCat, Model model,
                          @RequestParam(name = "nom", defaultValue = "") String code,
                          @RequestParam(name = "page", defaultValue = "0") int page,
                          @RequestParam(name = "size", defaultValue = "5") int size){
        boutiqueService.supprimerCategorie(idCat);
        return "redirect:/categories";
    }

    @Secured(value = {"ROLE_ADMIN"})
    @RequestMapping("/categorie/editForm")
    public String editCat(Long idCat, Model model){
        Categorie categorie = boutiqueService.getCategorie(idCat);
        model.addAttribute("editedCat",categorie);
        model.addAttribute("categorie", categorie);
        return "modifCategorie";
    }

    @Secured(value = {"ROLE_ADMIN"})
    @RequestMapping(value = "/categorie/edit", method = RequestMethod.PUT)
    public String editCategorie(@Valid Categorie categorie, BindingResult bindingResult, Model model, MultipartFile file,
                                @RequestParam(name = "nom", defaultValue = "") String code,
                                @RequestParam(name = "page", defaultValue = "0") int page,
                                @RequestParam(name = "size", defaultValue = "5") int size)
            throws Exception{
        if (bindingResult.hasErrors()){
            model.addAttribute("categorie", categorie);
            return "modifCategorie";
        }
        if (!file.isEmpty()){
            log.debug("yess");
            String path = System.getProperty("java.io.tmpdir");
            categorie.setPhoto(file.getOriginalFilename());
            Long idC = null;
            if (categorie.getIdCategorie() == null) {
                idC = boutiqueService.ajouterCategorie(categorie);
            } else {
                boutiqueService.modifierCategorie(categorie);
                idC = categorie.getIdCategorie();
            }
            file.transferTo(new File(path + "/" + "cat_" + idC + "_" + file.getOriginalFilename()));
        }else {

            if (categorie.getIdCategorie() != null) {
                boutiqueService.modifierCategorie(categorie);
            }
        }
        return "redirect:/categories";
    }

    //****************************************************************************//
    //**************************** PRODUIT Controller ****************************//
    //****************************************************************************//
    @Secured(value = {"ROLE_ADMIN"})
    @RequestMapping("/produits")
    public String produits(Model model,
                        @RequestParam(name = "nom", defaultValue = "") String code,
                        @RequestParam(name = "page", defaultValue = "0") int page,
                        @RequestParam(name = "size", defaultValue = "5") int size){
        Page<Produit> produits = boutiqueService.listeProduitsParMC(code,page,size);
        model.addAttribute("produits", produits);
        int[] pages = new int[produits.getTotalPages()];
        model.addAttribute("pages", pages);
        model.addAttribute("total", produits.getTotalElements());
        model.addAttribute("pageCourante", page);
        model.addAttribute("code", code);
        return "produits";
    }

    @Secured(value = {"ROLE_ADMIN"})
    @RequestMapping("/produit/form")
    public String formProduit(Model model){
        model.addAttribute("produit", new Produit());
        model.addAttribute("categories",boutiqueService.listCategories());
        return "ajoutProduit";
    }
    @Secured(value = {"ROLE_ADMIN"})
    @RequestMapping(value = "/produit/add", method = RequestMethod.POST)
    public String saveProduit(@Valid Produit produit, BindingResult bindingResult, Model model, MultipartFile file,
                                @RequestParam(name = "nom", defaultValue = "") String code,
                                @RequestParam(name = "page", defaultValue = "0") int page,
                                @RequestParam(name = "size", defaultValue = "5") int size)
            throws Exception{
        if (bindingResult.hasErrors()){
            model.addAttribute("categories",boutiqueService.listCategories());
            model.addAttribute("produit", produit);
            return "ajoutProduit";
        }
        if (!file.isEmpty()){
            String path = System.getProperty("java.io.tmpdir");
            produit.setPhoto(file.getOriginalFilename());
            Long idP = null;

            if (produit.getIdProduit() == null){
                idP = boutiqueService.ajouterProduit(produit);
            }else{
                boutiqueService.modifierProduit(produit);
                idP = produit.getIdProduit();
            }

            file.transferTo(new File(path+"/"+"prod_"+idP+"_"+file.getOriginalFilename()));

        }else {
            if (produit.getIdProduit() == null) {
                boutiqueService.ajouterProduit(produit);
            } else {
                boutiqueService.modifierProduit(produit);
            }
        }
        return "redirect:/produits";
    }

    @RequestMapping(value = "/produit/photo", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getPhotoProd(Long idProd) throws IOException{
        Produit produit = boutiqueService.getProduit(idProd);

        File f=new File(System.getProperty("java.io.tmpdir")+"/PROD_"+idProd+"_"+produit.getPhoto( ));
        return IOUtils.toByteArray(new FileInputStream(f));
    }

    @Secured(value = {"ROLE_ADMIN"})
    @RequestMapping(value = "/produit/delete", method = RequestMethod.DELETE)
    public String suppProd(Long idProd){
        boutiqueService.supprimerProduit(idProd);
        return "redirect:/produits";
    }

    @Secured(value = {"ROLE_ADMIN"})
    @RequestMapping("/produit/editForm")
    public String editProd(Long idProd, Model model){
        Produit produit = boutiqueService.getProduit(idProd);
        model.addAttribute("categories",boutiqueService.listCategories());
        model.addAttribute("produit", produit);
        return "modifProduit";
    }

    @Secured(value = {"ROLE_ADMIN"})
    @RequestMapping(value = "/produit/edit", method = RequestMethod.PUT)
    public String editProduit(@Valid Produit produit, BindingResult bindingResult, Model model, MultipartFile file,
                              @RequestParam(name = "nom", defaultValue = "") String code,
                              @RequestParam(name = "page", defaultValue = "0") int page,
                              @RequestParam(name = "size", defaultValue = "5") int size)
            throws Exception{
        if (bindingResult.hasErrors()){
            model.addAttribute("categories",boutiqueService.listCategories());
            model.addAttribute("produit", produit);
            return "modifProduit";
        }
        if (!file.isEmpty()){
            log.debug("yesss");
            String path = System.getProperty("java.io.tmpdir");
            produit.setPhoto(file.getOriginalFilename());
            Long idProduit = null;

            if (produit.getIdProduit() == null){
                idProduit = boutiqueService.ajouterProduit(produit);
            }else{
                boutiqueService.modifierProduit(produit);
                idProduit = produit.getIdProduit();
            }

            file.transferTo(new File(path+"/"+"prod_"+idProduit+"_"+file.getOriginalFilename()));

        }else {
            if (produit.getIdProduit() == null) {
                boutiqueService.ajouterProduit(produit);
            } else {
                boutiqueService.modifierProduit(produit);
            }
        }
        return "redirect:/produits";
    }

    @RequestMapping("/produit/all")
    public String espaceProduits(Model model, HttpSession session,
                                 @RequestParam(name = "nom", defaultValue = "") String code,
                                 @RequestParam(name = "page", defaultValue = "0") int page,
                                 @RequestParam(name = "size", defaultValue = "9") int size,
                                 @RequestParam(defaultValue = "") String error){
        model.addAttribute("categories",boutiqueService.listCategories());
        Collection<LigneCommande> ligneCommandes = (Collection<LigneCommande>) session.getAttribute("lignecommande");
        System.out.println(ligneCommandes);
        Page<Produit> produits = boutiqueService.listeProduitsParCat(code,page,size);
        model.addAttribute("produits", produits);
        model.addAttribute("lignecommandes", ligneCommandes);
        int[] pages = new int[produits.getTotalPages()];
        model.addAttribute("pages", pages);
        model.addAttribute("error", error);
        model.addAttribute("total", produits.getTotalElements());
        model.addAttribute("pageCourante", page);
        model.addAttribute("code", code);
        return "espaceProduits";
    }

    @RequestMapping(value = "/ligneCommande/add", method = RequestMethod.POST)
    public String ligneCommande (Long idp, int quantite, HttpSession session){
        Produit produit = boutiqueService.getProduit(idp);
        String msg_err ="";
        Collection<LigneCommande> ligneCommandes = (Collection<LigneCommande>) session.getAttribute("lignecommande");
        System.out.println(ligneCommandes);
        if (quantite == 0){
            return "redirect:/produit/all#panneau";
        }
        if (produit.getQuantite() > quantite){
            LigneCommande ligneCommande = new LigneCommande();
            ligneCommande.setId(produit.getIdProduit());
            ligneCommande.setProduit(produit);
            ligneCommande.setQuantite(quantite);
            ligneCommande.setPrix(produit.getPrix());

            if (ligneCommandes !=null){
                ligneCommandes.add(ligneCommande);
                session.setAttribute("lignecommande",ligneCommandes);
                for(LigneCommande lg : ligneCommandes){
                    System.out.println(lg);
                }
            }else{
                Collection<LigneCommande> ligneCommandess1= new ArrayList<>();
                ligneCommandess1.add(ligneCommande);
                session.setAttribute("lignecommande",ligneCommandess1);
            }



        }else{
             msg_err = "le stock est inferieur";
        }
        return "redirect:/produit/all?error="+msg_err+"#panneau";
    }

    @RequestMapping("/panier")
    public String consulterPanier(HttpSession session,Model model){
        Panier panier = new Panier();
        Collection<LigneCommande> ligneCommandes = new ArrayList<>();
        ligneCommandes =(Collection<LigneCommande>) session.getAttribute("lignecommande");
        if (ligneCommandes !=null){
            for (LigneCommande ligneCommande : ligneCommandes){
                panier.addItem(ligneCommande.getProduit(),ligneCommande.getQuantite());
            }
        }

        model.addAttribute("panier",panier);
        session.setAttribute("panier",panier);
        return "panier";
    }

    @RequestMapping(value = "/panier/delete")
    public String suppProdPanier(int idProd,HttpSession session){
        Panier panier = new Panier();
        Collection<LigneCommande> ligneCommandes = new ArrayList<>();
        ligneCommandes =(Collection<LigneCommande>) session.getAttribute("lignecommande");
        for (LigneCommande ligneCommande : ligneCommandes){
            panier.addItem(ligneCommande.getProduit(),ligneCommande.getQuantite());

        }
        panier.deleteItem(idProd);
        ligneCommandes.clear();
        ligneCommandes.addAll(panier.getItems());
        for(LigneCommande lg : ligneCommandes){
            System.out.println(lg);
        }
        session.setAttribute("lignecommande",ligneCommandes);
        session.setAttribute("panier",panier);
        return "redirect:/panier";
    }

    @RequestMapping(value = "/client/form", method = RequestMethod.POST)
    public String formClient(Model model){
       model.addAttribute("client", new Client());
       return "ajoutClient";
    }

    @RequestMapping(value = "/client/add", method = RequestMethod.POST)
    public String addClient(@Valid Client client, BindingResult bindingResult,HttpSession session, Model model){
        if (bindingResult.hasErrors()){
            return "ajoutClient";
        }else{
            Panier panier = (Panier) session.getAttribute("panier");
            System.out.println(panier);
            boutiqueService.ajouterCommande(panier,client);
            model.addAttribute("panier", panier);
            model.addAttribute("client", client);
            session.setAttribute("lignecommande",null);
            session.setAttribute("panier",null);
            return "confirmationCommande";
        }
    }

    @Secured(value = {"ROLE_ADMIN","ROLE_SUPERADMIN"})
    @RequestMapping(value = "/produitStat", method = RequestMethod.GET)
    public String index(Model model) {
        List<Categorie> categories = boutiqueService.listCategories();
        List<Integer> counts = new ArrayList<>();
        for (Categorie categorie : categories){
            int count = boutiqueService.countProduit(categorie.getNomCategorie());
            counts.add(count);
            System.out.println(categorie.getNomCategorie());
            System.out.println(count);
        }
        model.addAttribute("counts", counts);
        model.addAttribute("listeCategories",categories);
        return "produitStat";
    }
}
