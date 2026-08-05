package com.example.unimethod.integration.dspace.service;

import com.example.unimethod.integration.dspace.config.DSpaceDepartmentProperties;
import com.example.unimethod.integration.dspace.config.DSpaceProperties;
import com.example.unimethod.integration.dspace.dto.DSpaceRecord;
import com.example.unimethod.integration.dspace.dto.DSpaceSyncResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DSpaceSyncService {

    private final DSpaceProperties properties;
    private final DSpaceParserService parserService;
    private final DSpaceImportService importService;

    public DSpaceSyncService(
            DSpaceProperties properties,
            DSpaceParserService parserService,
            DSpaceImportService importService
    ) {
        this.properties = properties;
        this.parserService = parserService;
        this.importService = importService;
    }

    public DSpaceSyncResult syncDepartment(String departmentCode, int fromYear) throws Exception {
        DSpaceDepartmentProperties department = properties.getDepartments().stream()
                .filter(DSpaceDepartmentProperties::isEnabled)
                .filter(d -> d.getCode().equalsIgnoreCase(departmentCode))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Кафедру не знайдено: " + departmentCode));

        List<DSpaceRecord> records =
                parserService.fetchAllRecords(department.getCollectionScope(), fromYear);

        return importService.importRecords(
                department.getCode(),
                department.getName(),
                records
        );
    }

    public List<DSpaceSyncResult> syncAllDepartments(int fromYear) {
        List<DSpaceSyncResult> results = new ArrayList<>();

        for (DSpaceDepartmentProperties department : properties.getDepartments()) {
            if (!department.isEnabled()) {
                continue;
            }

            try {
                List<DSpaceRecord> records =
                        parserService.fetchAllRecords(department.getCollectionScope(), fromYear);

                DSpaceSyncResult result = importService.importRecords(
                        department.getCode(),
                        department.getName(),
                        records
                );

                results.add(result);
            } catch (Exception ex) {
                DSpaceSyncResult result = new DSpaceSyncResult();
                result.setDepartmentCode(department.getCode());
                result.setDepartmentName(department.getName());
                result.setFailed(1);
                results.add(result);
            }
        }

        return results;
    }

    public List<DSpaceDepartmentProperties> getEnabledDepartments() {
        return properties.getDepartments().stream()
                .filter(DSpaceDepartmentProperties::isEnabled)
                .toList();
    }
}