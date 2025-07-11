package com.example.gestion_pharmacie_garde.controller;

import com.example.gestion_pharmacie_garde.dto.LigneSemaine;
import com.example.gestion_pharmacie_garde.model.Calendrier;
import com.example.gestion_pharmacie_garde.model.Groupe;
import com.example.gestion_pharmacie_garde.model.Pharmacie;
import com.example.gestion_pharmacie_garde.model.Responsable;
import com.example.gestion_pharmacie_garde.service.CalendrierService;
import com.example.gestion_pharmacie_garde.service.GroupeService;
import com.example.gestion_pharmacie_garde.service.PharmacieService;
import com.example.gestion_pharmacie_garde.service.ResponsableService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ResponsableController {
    @Autowired
    private ResponsableService responsableService;

    @Autowired
    private PharmacieService pharmacieService;

    @Autowired
    private CalendrierService calendrierService;

    @Autowired
    private GroupeService  groupeService;

//    @GetMapping("/responsable/accueil")
//    public String afficherAccueilResponsable(Model model, Principal principal) {
//        String email = principal.getName();
//
//        Responsable responsable = responsableService
//                .RechercherByEmail(email)
//                .orElseThrow(() -> new RuntimeException("Responsable non trouvé pour l'email : " + email));
//
//        model.addAttribute("email", email);
//        model.addAttribute("responsable", responsable); // utile si tu veux afficher les infos de l'utilisateur
//
//        return "responsable"; // ne pas faire "redirect:/responsable"
//    }

    @GetMapping("/responsable/pharmacie")
    public String afficherAccueilPharmacie(Model model, Principal principal) {
        String email = principal.getName();

        Responsable responsable = responsableService
                .RechercherByEmail(email)
                .orElseThrow(() -> new RuntimeException("Responsable non trouvé pour l'email : " + email));

        List<Pharmacie> pharmacies = pharmacieService.getPharmaciesParResponsable(responsable);

        model.addAttribute("pharmacies", pharmacies);
        model.addAttribute("email", email);
        model.addAttribute("responsable", responsable);

        return "pharmacie";
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

    @GetMapping("/responsable/calendrier")
    public String afficherFormulaireCalendrier(Model model, Principal principal) {
        String email = principal.getName();

        Responsable responsable = responsableService
                .RechercherByEmail(email)
                .orElseThrow(() -> new RuntimeException("Responsable non trouvé pour l'email : " + email));

        Calendrier calendrier = new Calendrier();
        model.addAttribute("calendrier", calendrier);

        model.addAttribute("email", email);
        model.addAttribute("responsable", responsable); // utile si tu veux afficher les infos de l'utilisateur

        return "creerCalendrier"; // ne pas faire "redirect:/responsable"
    }

    @PostMapping("/responsable/calendrier/enregistrer")
    public String enregistrerCalendrier(@ModelAttribute Calendrier calendrier,Model model, Principal principal) {
        String email = principal.getName();
        Responsable responsable = responsableService
                .RechercherByEmail(email)
                .orElseThrow(() -> new RuntimeException("Responsable non trouvé"));

        calendrier.setResponsable(responsable);
        calendrierService.ajouteCalendrier(calendrier);

        // 👉 Redirige vers l'étape suivante pour créer les groupes
        return "redirect:/responsable/calendrier/groupes?calendrierId=" + calendrier.getId();
    }

    @GetMapping("/responsable/calendrier/groupes")
    public String afficherFormulaireGroupes(@RequestParam Long calendrierId, Model model, Principal principal) {
        Calendrier calendrier = calendrierService.trouverParId(calendrierId);

        String email = principal.getName();
        Responsable responsable = responsableService
                .RechercherByEmail(email)
                .orElseThrow(() -> new RuntimeException("Responsable non trouvé pour l'email : " + email));

        model.addAttribute("calendrier", calendrier);
        model.addAttribute("pharmacies", pharmacieService.getPharmaciesParResponsable(responsable));
        model.addAttribute("nombreGroupes", calendrier.getNombreGroupes());
        model.addAttribute("email", email);

        return "groupesCalendrier";
    }


    @PostMapping("/responsable/calendrier/groupes/enregistrer")
    public String enregistrerGroupes(@RequestParam Long calendrierId, HttpServletRequest request) {

        Calendrier calendrier = calendrierService.getById(calendrierId);

        Map<Integer, List<Pharmacie>> groupesMap = new HashMap<>();

        // On boucle sur les paramètres de la requête
        request.getParameterMap().forEach((paramName, values) -> {
            if (paramName.startsWith("groupes[")) {
                String numeroStr = paramName.substring(8, paramName.length() - 1); // extrait 1 de groupes[1]
                int numeroGroupe = Integer.parseInt(numeroStr);

                List<Pharmacie> pharmacies = Arrays.stream(values)
                        .map(idStr -> pharmacieService.getById(Long.parseLong(idStr)))
                        .collect(Collectors.toList());

                groupesMap.put(numeroGroupe, pharmacies);
            }
        });

        // Sauvegarde des groupes
        groupesMap.forEach((numero, pharmacies) -> {
            Groupe groupe = new Groupe();
            groupe.setNumero(numero);
            groupe.setCalendrier(calendrier);
            groupe.setPharmacies(pharmacies);
            groupeService.ajouterGroupe(groupe);
        });

        return "redirect:/responsable/VoirCalendrier";

    }


    @GetMapping("/responsable/VoirCalendrier")
    public String voirTousLesCalendriers(Model model, Principal principal) {
        String email = principal.getName();

        Responsable responsable = responsableService
                .RechercherByEmail(email)
                .orElseThrow(() -> new RuntimeException("Responsable non trouvé"));

        List<Calendrier> calendriers = calendrierService.getAll().stream()
                .filter(c -> c.getResponsable().getId().equals(responsable.getId()))
                .collect(Collectors.toList());

        Map<Long, List<LigneSemaine>> affectations = new HashMap<>();

        for (Calendrier calendrier : calendriers) {
            List<Groupe> groupes = groupeService.findByCalendrierId(calendrier.getId());

            if (groupes.isEmpty()) continue; // Évite la division par zéro

            List<LigneSemaine> lignes = new ArrayList<>();
            LocalDate debut = calendrier.getDateDebut();
            LocalDate fin = calendrier.getDateFin();
            int nbSemaines = (int) ChronoUnit.WEEKS.between(debut, fin);
            int totalGroupes = groupes.size();

            for (int i = 0; i <= nbSemaines; i++) {
                Groupe groupe = groupes.get(i % totalGroupes);
                LocalDate debutSemaine = debut.plusWeeks(i);
                LocalDate finSemaine = debutSemaine.plusDays(6);

                lignes.add(new LigneSemaine(i + 1, groupe, debutSemaine, finSemaine));
            }

            affectations.put(calendrier.getId(), lignes);
        }

        model.addAttribute("responsable", responsable);
        model.addAttribute("calendriers", calendriers);
        model.addAttribute("affectations", affectations);

        return "calendrier"; // Nom du fichier Thymeleaf
    }

    @GetMapping("/responsable/calendrier/supprimer")
    public String supprimerCalendrier(@RequestParam("calendrierId") Long calendrierId, RedirectAttributes redirectAttributes) {
        try {
            // 1. Récupérer les groupes liés au calendrier
            List<Groupe> groupes = groupeService.findByCalendrierId(calendrierId);

            // 2. Supprimer les pharmacies liées à chaque groupe
            for (Groupe groupe : groupes) {
                groupe.getPharmacies().clear(); // supprime les liens (relation many-to-many)
                groupeService.save(groupe); // sauvegarder le groupe modifié
            }

            // 3. Supprimer les groupes
            groupeService.deleteAll(groupes);

            // 4. Supprimer le calendrier
            calendrierService.supprimerParId(calendrierId);

            redirectAttributes.addFlashAttribute("success", "Calendrier supprimé avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression : " + e.getMessage());
        }

        return "redirect:/responsable/VoirCalendrier";
    }

    @GetMapping("/responsable/calendrier/imprimer")
    public String imprimerCalendrier(@RequestParam("calendrierId") Long calendrierId, Model model) {
        Calendrier calendrier = calendrierService.getById(calendrierId);
        List<Groupe> groupes = groupeService.findByCalendrierId(calendrierId);

        List<LigneSemaine> calendrierHebdo = new ArrayList<>();
        LocalDate debut = calendrier.getDateDebut();
        LocalDate fin = calendrier.getDateFin();
        int nbSemaines = (int) ChronoUnit.WEEKS.between(debut, fin);
        int totalGroupes = groupes.size();

        for (int i = 0; i <= nbSemaines; i++) {
            Groupe groupe = groupes.get(i % totalGroupes);
            LigneSemaine ligne = new LigneSemaine(i + 1, groupe, debut.plusWeeks(i), debut.plusWeeks(i).plusDays(6));
            calendrierHebdo.add(ligne);
        }

        model.addAttribute("calendrier", calendrier);
        model.addAttribute("calendrierHebdo", calendrierHebdo);
        return "calendrier_print";
    }


    @GetMapping("/responsable/accueil")
    public String voirToutesPharmaciesGarde(@RequestParam(required = false) String commune,
                                            @RequestParam(defaultValue = "0") int semaineOffset,
                                            Model model) {

        LocalDate today = LocalDate.now().plusWeeks(semaineOffset);
        List<String> communes = responsableService.findAllCommunes();
        model.addAttribute("communes", communes);
        model.addAttribute("selectedCommune", commune);
        model.addAttribute("semaineOffset", semaineOffset);
        model.addAttribute("dateSemaine", today);

        Map<String, List<Pharmacie>> pharmaciesParCommune = new LinkedHashMap<>();

        Optional<Calendrier> calendrierSelectionne = Optional.empty();
        List<Groupe> groupesSelectionnes = new ArrayList<>();

        for (String c : communes) {
            Optional<Calendrier> calendrier = calendrierService.findCalendrierByDateAndCommune(today, c);
            if (calendrier.isPresent()) {
                List<Groupe> groupes = groupeService.findByCalendrierId(calendrier.get().getId());

                int index = (int) ChronoUnit.WEEKS.between(calendrier.get().getDateDebut(), today);
                if (!groupes.isEmpty()) {
                    Groupe groupe = groupes.get(index % groupes.size());
                    pharmaciesParCommune.put(c, groupe.getPharmacies());

                    // Si c'est la commune sélectionnée, on garde les infos pour vérifier la semaine suivante
                    if (c.equals(commune)) {
                        calendrierSelectionne = calendrier;
                        groupesSelectionnes = groupes;
                        model.addAttribute("groupe", groupe); // utile pour affichage si besoin
                        model.addAttribute("calendrier", calendrier.get());
                    }
                }
            }
        }

        // Si filtre sélectionné : n'afficher que cette commune
        if (commune != null && !commune.isEmpty()) {
            pharmaciesParCommune.keySet().removeIf(key -> !key.equals(commune));
        }

        // Gérer l’affichage conditionnel du bouton “Semaine suivante”
        boolean hasNextWeek = false;
        if (calendrierSelectionne.isPresent() && !groupesSelectionnes.isEmpty()) {
            int totalWeeks = (int) ChronoUnit.WEEKS.between(
                    calendrierSelectionne.get().getDateDebut(),
                    calendrierSelectionne.get().getDateFin()
            ) + 1;

            int currentIndex = (int) ChronoUnit.WEEKS.between(
                    calendrierSelectionne.get().getDateDebut(), today
            );

            if (currentIndex < totalWeeks - 1) {
                hasNextWeek = true;
            }
        }

        model.addAttribute("hasNextWeek", hasNextWeek);
        model.addAttribute("pharmaciesParCommune", pharmaciesParCommune);

        return "responsable";
    }


    @GetMapping("/index")
    public String voirPharmaciesGardePublique(@RequestParam(required = false) String commune,
                                              @RequestParam(defaultValue = "0") int semaineOffset,
                                              Model model) {

        LocalDate today = LocalDate.now().plusWeeks(semaineOffset);
        List<String> communes = responsableService.findAllCommunes();
        model.addAttribute("communes", communes);
        model.addAttribute("selectedCommune", commune);
        model.addAttribute("semaineOffset", semaineOffset);
        model.addAttribute("dateSemaine", today);

        Map<String, List<Pharmacie>> pharmaciesParCommune = new LinkedHashMap<>();

        Optional<Calendrier> calendrierSelectionne = Optional.empty();
        List<Groupe> groupesSelectionnes = new ArrayList<>();

        for (String c : communes) {
            Optional<Calendrier> calendrier = calendrierService.findCalendrierByDateAndCommune(today, c);
            if (calendrier.isPresent()) {
                List<Groupe> groupes = groupeService.findByCalendrierId(calendrier.get().getId());

                int index = (int) ChronoUnit.WEEKS.between(calendrier.get().getDateDebut(), today);
                if (!groupes.isEmpty()) {
                    Groupe groupe = groupes.get(index % groupes.size());
                    pharmaciesParCommune.put(c, groupe.getPharmacies());

                    if (c.equals(commune)) {
                        calendrierSelectionne = calendrier;
                        groupesSelectionnes = groupes;
                        model.addAttribute("groupe", groupe);
                        model.addAttribute("calendrier", calendrier.get());
                    }
                }
            }
        }

        if (commune != null && !commune.isEmpty()) {
            pharmaciesParCommune.keySet().removeIf(key -> !key.equals(commune));
        }

        boolean hasNextWeek = false;
        if (calendrierSelectionne.isPresent() && !groupesSelectionnes.isEmpty()) {
            int totalWeeks = (int) ChronoUnit.WEEKS.between(
                    calendrierSelectionne.get().getDateDebut(),
                    calendrierSelectionne.get().getDateFin()
            ) + 1;

            int currentIndex = (int) ChronoUnit.WEEKS.between(
                    calendrierSelectionne.get().getDateDebut(), today
            );

            if (currentIndex < totalWeeks - 1) {
                hasNextWeek = true;
            }
        }

        model.addAttribute("hasNextWeek", hasNextWeek);
        model.addAttribute("pharmaciesParCommune", pharmaciesParCommune);

        return "index"; // nom de la vue publique (pharmacie-garde.html)
    }

    @GetMapping("/responsable/calendrier/telecharger")
    public ResponseEntity<Resource> telechargerCalendrier(@RequestParam("calendrierId") Long calendrierId) {

        // Exemple : récupération du fichier généré (PDF, Excel, etc.)
        Path cheminFichier = Paths.get("documents/calendrier_" + calendrierId + ".pdf");

        if (!Files.exists(cheminFichier)) {
            return ResponseEntity.notFound().build();
        }

        try {
            Resource resource = new UrlResource(cheminFichier.toUri());

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (MalformedURLException e) {
            return ResponseEntity.internalServerError().build();
        }
    }


}
