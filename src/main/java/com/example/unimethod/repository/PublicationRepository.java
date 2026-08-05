package com.example.diploma.repository;

import com.example.diploma.model.Publication;
import com.example.diploma.model.Publication.Source;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PublicationRepository extends JpaRepository<Publication, Long>, JpaSpecificationExecutor<Publication> {

    @Override
    @EntityGraph(attributePaths = {"authors", "authors.author", "authors.author.user"})
    List<Publication> findAll();

    @Override
    @EntityGraph(attributePaths = {"authors", "authors.author", "authors.author.user"})
    Optional<Publication> findById(Long id);
    List<Publication> findByYear(Integer year);

    @Query("""
        select distinct p.year
        from Publication p
        where p.year is not null
        order by p.year desc
    """)
    List<Integer> findDistinctYears();

    @Query("""
        select distinct p.department
        from Publication p
        where p.department is not null and trim(p.department) <> ''
        order by p.department
    """)
    List<String> findDistinctDepartments();

    @Query("""
        select p
        from Publication p
        where (:years is null or p.year in :years)
          and (:departments is null or p.department in :departments)
          and (:sources is null or p.source in :sources)
        order by p.year desc, p.title asc
    """)
    List<Publication> findForReport(
            List<Integer> years,
            List<String> departments,
            List<Source> sources
    );

    boolean existsByTitleIgnoreCase(String normalizedTitle);

    boolean existsByTitleIgnoreCaseAndIdNot(String normalizedTitle, Long id);


    boolean existsByUrlIgnoreCase(String url);

    boolean existsByUrlIgnoreCaseAndIdNot(String url, Long id);

    @Query("""
       SELECT p FROM Publication p
       WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
       """)
    List<Publication> findPossibleDuplicates(@Param("keyword") String keyword);

    Optional<Publication> findByExternalLink(String externalLink);

    Optional<Publication> findByTitleIgnoreCaseAndYearAndDepartment(String title, Integer year, String department);

}