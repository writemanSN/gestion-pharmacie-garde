package com.example.gestion_pharmacie_garde.controller;

import com.example.gestion_pharmacie_garde.model.Responsable;
import com.example.gestion_pharmacie_garde.repository.CodeSecretRespository;
import com.example.gestion_pharmacie_garde.service.ResponsableService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class Authentification {

    @Autowired
    private CodeSecretRespository codeSecretRespository;

    @Autowired
    private ResponsableService responsableService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/index")
    public String afficherIndex(Model model) {
        return "index";
    }

    @GetMapping("/index/verificationCode")
    public String afficherVerification(Model model) {
        return "verification";
    }

    @PostMapping("/index/verificationCode")
    public String verifierCode(@RequestParam("code") String code, Model model) {
        if (codeSecretRespository.existsByCode(code)) {
            model.addAttribute("responsable", new Responsable());
            return "inscription";

        } else {
            model.addAttribute("erreur", "Code invalide !");
            return "verification"; // nom du fichier .html contenant le formulaire
        }
    }

    @GetMapping("/login")
    public String afficherLogin(Model model) {
        return "login";
    }

    @RequestMapping(value = "/logout")
    public String logout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/login?logout=true";
    }

    @GetMapping("/inscription")
    public String afficherInscription(Model model) {
        return "inscription";
    }

    @PostMapping("/index/register")
    public String enregistrerUtilisateur(@ModelAttribute Responsable responsable, Model model) {

        if (responsableService.emailExiste(responsable.getEmail())) {
            model.addAttribute("emailDejaUtilise", true);
            return "inscription"; // Reviens à la page d'inscription
        }
        responsable.setEtatCompte(true);
        responsable.setMotDePasse(passwordEncoder.encode(responsable.getMotDePasse()));
        responsableService.AjouterResponsable(responsable);
        return "redirect:/login?inscription=ok";
    }


    @GetMapping("/profil")
    public String afficherProfil(Model model, Principal principal) {
        // On récupère l'utilisateur connecté grâce à son email (nom d'utilisateur)
        Responsable responsable = responsableService.RechercherByEmail(principal.getName()).orElse(null);

        // Vérification pour éviter les erreurs si l'utilisateur est null
        if (responsable == null) {
            return "redirect:/login?error";
        }

        // On ajoute les données nécessaires au modèle pour les afficher dans la page
        model.addAttribute("responsable", responsable);
        model.addAttribute("email", responsable.getEmail());
        return "profil"; // ou .html selon le moteur de template que tu utilises
    }

    @PostMapping("/profil/modifier")
    public String modifierProfil(@ModelAttribute Responsable responsable, Model model) {
        responsableService.MettreAJourResponsable(responsable); // À implémenter dans ton service
        return "redirect:/responsable/accueil?modifie=true"; // Redirection vers le profil après modification
    }
}

