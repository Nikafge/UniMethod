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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/publications")
public class PublicationController {

    private static final String DEFAULT_BACK_HREF = "/publications";

    private final PublicationService publicationService;
    private final PublicationDuplicateAnalysisService duplicateAnalysisService;

    public PublicationController(PublicationService publicationService, PublicationDuplicateAnalysisService duplicateAnalysisService) {
        this.publicationService = publicationService;
        this.duplicateAnalysisService = duplicateAnalysisService;
    }

    @GetMapping("/new")
    public String createForm(Model model, HttpServletRequest request) {
        PublicationFormDto form = new PublicationFormDto();
        form.setSource(Publication.Source.MANUAL);

        model.addAttribute("publicationForm", form);
        model.addAttribute("sources", Publication.Source.values());
        model.addAttribute("backHref", backHrefFromRequest(request));

        return "publications/form";
    }

    @PostMapping
    public String save(
            @Valid @ModelAttribute("publicationForm") PublicationFormDto publicationForm,
            BindingResult bindingResult,
            @RequestParam(required = false) String backHref,
            HttpServletRequest request,
            Model model
    ) {
        publicationService.validateUniqueFields(publicationForm, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("sources", Publication.Source.values());
            model.addAttribute("backHref", safeBackHref(backHref, request));
            return "publications/form";
        }

        publicationService.saveFromForm(publicationForm);

        return "redirect:/publications";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model, HttpServletRequest request) {
        Publication publication = publicationService.findById(id);

        model.addAttribute("publicationForm", publicationService.toFormDto(publication));
        model.addAttribute("sources", Publication.Source.values());
        model.addAttribute("backHref", backHrefFromRequest(request));

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
    public String duplicates(Model model, HttpServletRequest request) {
        model.addAttribute("duplicates", duplicateAnalysisService.findPossibleDuplicates());
        model.addAttribute("backHref", backHrefFromRequest(request));
        return "publications/duplicates";
    }

    private String backHrefFromRequest(HttpServletRequest request) {
        return safeBackHref(request.getHeader("Referer"), request);
    }

    private String safeBackHref(String candidate, HttpServletRequest request) {
        if (candidate == null || candidate.isBlank()) {
            return DEFAULT_BACK_HREF;
        }

        String value = candidate.trim();
        try {
            URI uri = URI.create(value);

            if (!uri.isAbsolute()) {
                return value.startsWith("/") && !value.startsWith("//")
                        ? value
                        : DEFAULT_BACK_HREF;
            }

            if (!isSameOrigin(uri, request)) {
                return DEFAULT_BACK_HREF;
            }

            String path = uri.getRawPath();
            if (path == null || path.isBlank()) {
                return DEFAULT_BACK_HREF;
            }

            String query = uri.getRawQuery();
            return query == null ? path : path + "?" + query;
        } catch (IllegalArgumentException ex) {
            return DEFAULT_BACK_HREF;
        }
    }

    private boolean isSameOrigin(URI uri, HttpServletRequest request) {
        return Objects.equals(uri.getScheme(), request.getScheme())
                && Objects.equals(uri.getHost(), request.getServerName())
                && normalizedPort(uri.getScheme(), uri.getPort()) == normalizedPort(request.getScheme(), request.getServerPort());
    }

    private int normalizedPort(String scheme, int port) {
        if (port != -1) {
            return port;
        }
        return "https".equalsIgnoreCase(scheme) ? 443 : 80;
    }

}
