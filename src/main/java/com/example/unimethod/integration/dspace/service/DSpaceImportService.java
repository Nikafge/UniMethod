package com.example.unimethod.integration.dspace.service;

import com.example.unimethod.integration.dspace.dto.DSpaceRecord;
import com.example.unimethod.integration.dspace.dto.DSpaceSyncResult;
import com.example.unimethod.service.PublicationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DSpaceImportService {

    private final PublicationService publicationService;

    public DSpaceImportService(PublicationService publicationService) {
        this.publicationService = publicationService;
    }

    public DSpaceSyncResult importRecords(
            String departmentCode,
            String departmentName,
            List<DSpaceRecord> records
    ) {
        DSpaceSyncResult result = new DSpaceSyncResult();
        result.setDepartmentCode(departmentCode);
        result.setDepartmentName(departmentName);

        for (DSpaceRecord record : records) {
            result.incrementScanned();

            try {
                PublicationService.ImportResult importResult =
                        publicationService.upsertFromDspace(record, departmentName);

                switch (importResult) {
                    case CREATED -> result.incrementCreated();
                    case UPDATED -> result.incrementUpdated();
                    case SKIPPED -> result.incrementSkipped();
                }
            } catch (Exception ex) {
                result.incrementFailed();
            }
        }

        return result;
    }
}