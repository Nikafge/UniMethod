package com.example.unimethod.repository;

import com.example.unimethod.model.Publication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:repository-test;MODE=MySQL;DATABASE_TO_LOWER=TRUE;NON_KEYWORDS=YEAR;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=false"
})
@Transactional
class PublicationRepositoryIntegrationTest {

    @Autowired
    PublicationRepository publicationRepository;

    @Test
    void findsDistinctYearsAndDepartments() {
        publicationRepository.save(publication("Spring Testing", 2024, "CS", Publication.Source.MANUAL));
        publicationRepository.save(publication("Data Testing", 2023, "Math", Publication.Source.REPOSITORY));
        publicationRepository.save(publication("No Department", 2024, "", Publication.Source.MANUAL));

        assertThat(publicationRepository.findDistinctYears()).containsExactly(2024, 2023);
        assertThat(publicationRepository.findDistinctDepartments()).containsExactly("CS", "Math");
    }

    @Test
    void checksUniqueTitleAndUrlIgnoringCase() {
        publicationRepository.save(publication(
                "Spring Testing",
                2024,
                "CS",
                Publication.Source.MANUAL,
                "https://example.test/PUBLICATION"
        ));

        assertThat(publicationRepository.existsByTitleIgnoreCase("spring testing")).isTrue();
        assertThat(publicationRepository.existsByUrlIgnoreCase("https://example.test/publication")).isTrue();
        assertThat(publicationRepository.existsByTitleIgnoreCase("different title")).isFalse();
    }

    @Test
    void findsPublicationsForReportByYearDepartmentAndSource() {
        Publication matching = publication("Spring Testing", 2024, "CS", Publication.Source.MANUAL);
        publicationRepository.save(matching);
        publicationRepository.save(publication("Repository Publication", 2024, "CS", Publication.Source.REPOSITORY));
        publicationRepository.save(publication("Other Department", 2024, "Math", Publication.Source.MANUAL));
        publicationRepository.save(publication("Other Year", 2023, "CS", Publication.Source.MANUAL));

        List<Publication> result = publicationRepository.findForReport(
                List.of(2024),
                List.of("CS"),
                List.of(Publication.Source.MANUAL)
        );

        assertThat(result).extracting(Publication::getTitle)
                .containsExactly(matching.getTitle());
    }

    private Publication publication(String title, Integer year, String department, Publication.Source source) {
        return publication(title, year, department, source, "https://example.test/" + title.replace(" ", "-"));
    }

    private Publication publication(
            String title,
            Integer year,
            String department,
            Publication.Source source,
            String url
    ) {
        Publication publication = new Publication();
        publication.setTitle(title);
        publication.setYear(year);
        publication.setDepartment(department);
        publication.setSource(source);
        publication.setUrl(url);
        publication.setPublisher("Publisher");
        return publication;
    }
}
