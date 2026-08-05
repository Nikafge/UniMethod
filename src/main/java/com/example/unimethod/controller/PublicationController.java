//package com.example.diploma.controller;
//
//import com.example.diploma.model.Author;
//import com.example.diploma.model.Publication;
//import com.example.diploma.repository.AuthorRepository;
//import com.example.diploma.service.PublicationService;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//
//@Controller
//@RequestMapping("/publications")
//public class PublicationController {
//
//    private final PublicationService publicationService;
//
//    public PublicationController(PublicationService publicationService) {
//        this.publicationService = publicationService;
//    }
//
//    @GetMapping
//    public String list(
//            @RequestParam(required = false) String titleKeyword,
//            @RequestParam(required = false) String authorKeyword,
//            @RequestParam(required = false) Integer year,
//            @RequestParam(required = false) String department,
//            @RequestParam(required = false, defaultValue = "createdAtDesc") String sort,
//            Model model
//    ) {
//        List<Publication> publications = publicationService.searchAndFilter(
//                titleKeyword,
//                authorKeyword,
//                year,
//                department,
//                sort
//        );
//
//        model.addAttribute("publications", publications);
//
//        model.addAttribute("availableYears", publicationService.findAvailableYears());
//        model.addAttribute("availableDepartments", publicationService.findAvailableDepartments());
//
//        model.addAttribute("titleKeyword", titleKeyword);
//        model.addAttribute("authorKeyword", authorKeyword);
//        model.addAttribute("selectedYear", year);
//        model.addAttribute("selectedDepartment", department);
//        model.addAttribute("selectedSort", sort);
//
//        return "publications/list";
//    }
//
//    @GetMapping("/new")
//    public String createForm(Model model) {
//        model.addAttribute("publication", new Publication());
//        return "publications/form";
//    }
//
//    @PostMapping
//    public String save(@ModelAttribute Publication publication, Model model) {
//        publicationService.save(publication);
//        return "redirect:/publications";
//    }
//
//    @GetMapping("/{id}/edit")
//    public String edit(@PathVariable Long id, Model model) {
//        Publication publication = publicationService.findById(id);
//
//        String authorsText = publication.getAuthors().stream()
//                .map(link -> link.getAuthor().getDisplayName())
//                .reduce((a, b) -> a + "\n" + b)
//                .orElse("");
//
//        publication.setAuthorsInput(authorsText);
//
//        model.addAttribute("publication", publication);
//        return "publications/form";
//    }
//
//    @GetMapping("/{id}/delete")
//    public String delete(@PathVariable Long id) {
//        publicationService.delete(id);
//        return "redirect:/publications";
//    }
//}

package com.example.unimethod.controller;

import com.example.unimethod.dto.PublicationFormDto;
import com.example.unimethod.model.Publication;
import com.example.unimethod.quality.service.PublicationDuplicateAnalysisService;
import com.example.unimethod.service.PublicationService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/publications")
public class PublicationController {

    private final PublicationService publicationService;
    private final PublicationDuplicateAnalysisService duplicateAnalysisService;

    public PublicationController(PublicationService publicationService, PublicationDuplicateAnalysisService duplicateAnalysisService) {
        this.publicationService = publicationService;
        this.duplicateAnalysisService = duplicateAnalysisService;
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        PublicationFormDto form = new PublicationFormDto();
        form.setSource(Publication.Source.MANUAL);

        model.addAttribute("publicationForm", form);
        model.addAttribute("sources", Publication.Source.values());

        return "publications/form";
    }

    @PostMapping
    public String save(
            @Valid @ModelAttribute("publicationForm") PublicationFormDto publicationForm,
            BindingResult bindingResult,
            Model model
    ) {
        publicationService.validateUniqueFields(publicationForm, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("sources", Publication.Source.values());
            return "publications/form";
        }

        publicationService.saveFromForm(publicationForm);

        return "redirect:/publications";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        Publication publication = publicationService.findById(id);

        model.addAttribute("publicationForm", publicationService.toFormDto(publication));
        model.addAttribute("sources", Publication.Source.values());

        return "publications/form";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        publicationService.delete(id);
        return "redirect:/publications";
    }


        @GetMapping
    public String list(
            @RequestParam(required = false) String titleKeyword,
            @RequestParam(required = false) String authorKeyword,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String department,
            @RequestParam(required = false, defaultValue = "createdAtDesc") String sort,
            Model model
    ) {
        List<Publication> publications = publicationService.searchAndFilter(
                titleKeyword,
                authorKeyword,
                year,
                department,
                sort
        );

        model.addAttribute("publications", publications);

        model.addAttribute("availableYears", publicationService.findAvailableYears());
        model.addAttribute("availableDepartments", publicationService.findAvailableDepartments());

        model.addAttribute("titleKeyword", titleKeyword);
        model.addAttribute("authorKeyword", authorKeyword);
        model.addAttribute("selectedYear", year);
        model.addAttribute("selectedDepartment", department);
        model.addAttribute("selectedSort", sort);

        return "publications/list";
    }
    @GetMapping("/duplicates")
    public String duplicates(Model model) {
        model.addAttribute("duplicates", duplicateAnalysisService.findPossibleDuplicates());
        return "publications/duplicates";
    }

}