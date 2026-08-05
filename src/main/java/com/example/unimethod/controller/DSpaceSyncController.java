package com.example.unimethod.controller;

import com.example.unimethod.integration.dspace.dto.DSpaceSyncResult;
import com.example.unimethod.integration.dspace.service.DSpaceSyncService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/sync/dspace")
public class DSpaceSyncController {

    private final DSpaceSyncService dSpaceSyncService;

    public DSpaceSyncController(DSpaceSyncService dSpaceSyncService) {
        this.dSpaceSyncService = dSpaceSyncService;
    }

    @GetMapping
    public String syncPage(Model model) {
        model.addAttribute("departments", dSpaceSyncService.getEnabledDepartments());
        return "sync/dspace";
    }

    @PostMapping("/department/{code}")
    public String syncDepartment(
            @PathVariable String code,
            @RequestParam(defaultValue = "2025") int fromYear,
            Model model
    ) throws Exception {
        DSpaceSyncResult result = dSpaceSyncService.syncDepartment(code, fromYear);

        model.addAttribute("departments", dSpaceSyncService.getEnabledDepartments());
        model.addAttribute("singleResult", result);

        return "sync/dspace";
    }

    @PostMapping("/all")
    public String syncAll(
            @RequestParam(defaultValue = "2025") int fromYear,
            Model model
    ) {
        List<DSpaceSyncResult> results = dSpaceSyncService.syncAllDepartments(fromYear);

        model.addAttribute("departments", dSpaceSyncService.getEnabledDepartments());
        model.addAttribute("results", results);

        return "sync/dspace";
    }
}