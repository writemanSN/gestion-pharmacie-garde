package com.example.gestion_pharmacie_garde.controller;

import com.example.gestion_pharmacie_garde.model.Pharmacie;
import com.example.gestion_pharmacie_garde.model.Responsable;
import com.example.gestion_pharmacie_garde.service.PharmacieService;
import com.example.gestion_pharmacie_garde.service.ResponsableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class ResponsableController {
    @Autowired
    private ResponsableService responsableService;

    @Autowired
    private PharmacieService pharmacieService;

    @GetMapping("/responsable/accueil")
    public String afficherAccueilResponsable(Model model, Principal principal) {
        String email = principal.getName();

        Responsable responsable = responsableService
                .RechercherByEmail(email)
                .orElseThrow(() -> new RuntimeException("Responsable non trouvé pour l'email : " + email));

        model.addAttribute("email", email);
        model.addAttribute("responsable", responsable); // utile si tu veux afficher les infos de l'utilisateur

        return "responsable"; // ne pas faire "redirect:/responsable"
    }

    @GetMapping("/responsable/pharmacie")
    public String afficherAccueilPharmacie(Model model, Principal principal) {
        String email = principal.getName();

        Responsable responsable = responsableService
                .RechercherByEmail(email)
                .orElseThrow(() -> new RuntimeException("Responsable non trouvé pour l'email : " + email));
        List<Pharmacie> pharmacies = pharmacieService.getAll();
        model.addAttribute("pharmacies", pharmacies);

        model.addAttribute("email", email);
        model.addAttribute("responsable", responsable); // utile si tu veux afficher les infos de l'utilisateur

        return "pharmacie"; // ne pas faire "redirect:/responsable"
    }

    @GetMapping("/responsable/pharmacie/ajouter")
    public String afficherAccueilPharmacieAjouter(Model model, Principal principal) {
        String email = principal.getName();

        Responsable responsable = responsableService
                .RechercherByEmail(email)
                .orElseThrow(() -> new RuntimeException("Responsable non trouvé pour l'email : " + email));

        Pharmacie pharmacie = new Pharmacie();
        pharmacie.setResponsable(responsable);

        model.addAttribute("pharmacie", pharmacie); // ✅ Obligatoire

        model.addAttribute("email", email);
        model.addAttribute("responsable", responsable); // utile si tu veux afficher les infos de l'utilisateur

        return "ajoutPharmacie"; // ne pas faire "redirect:/responsable"
    }

    @PostMapping("/responsable/pharmacie/save")
    public String enregistrerPharmacie(@ModelAttribute Pharmacie pharmacie, Principal principal) {
        String email = principal.getName();

        Responsable responsable = responsableService
                .RechercherByEmail(email)
                .orElseThrow(() -> new RuntimeException("Responsable non trouvé"));

        pharmacie.setResponsable(responsable);
        pharmacieService.ajouterPharmacie(pharmacie);

        return "redirect:/responsable/pharmacie?success";
    }

    @GetMapping("/responsable/pharmacie/supprimer/{id}")
    public String supprimerPharmacie(@PathVariable("id") Long id) {
        pharmacieService.supprimerPharmacie(id);
        return "redirect:/responsable/pharmacie?deleteSuccess";
    }

    @GetMapping("/responsable/pharmacie/modifier/{id}")
    public String afficherFormulaireModification(@PathVariable("id") Long id, Model model, Principal principal) {
        Pharmacie pharmacie = pharmacieService.trouverParId(id);

        model.addAttribute("pharmacie", pharmacie);
        model.addAttribute("email", principal.getName());

        return "modifierPharmacie"; // Ce sera ton fichier HTML
    }

    @PostMapping("/responsable/pharmacie/update")
    public String modifierPharmacie(@ModelAttribute Pharmacie pharmacieModifiee, Principal principal) {
        Responsable responsable = responsableService
                .RechercherByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Responsable non trouvé"));

        pharmacieModifiee.setResponsable(responsable); // 🔒 sécurité : réassocie le bon responsable
        pharmacieService.ajouterPharmacie(pharmacieModifiee); // save() fait insert ou update

        return "redirect:/responsable/pharmacie?updateSuccess";
    }

}
