package com.example.unimethod.repository.specification;

import com.example.unimethod.model.Author;
import com.example.unimethod.model.Publication;
import com.example.unimethod.model.PublicationAuthor;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class PublicationSpecification {

    public static Specification<Publication> hasYear(Integer year) {
        return (root, query, criteriaBuilder) -> {
            if (year == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(root.get("year"), year);
        };
    }

    public static Specification<Publication> hasDepartment(String department) {
        return (root, query, criteriaBuilder) -> {
            if (department == null || department.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(root.get("department"), department);
        };
    }

    public static Specification<Publication> titleContains(String titleKeyword) {
        return (root, query, criteriaBuilder) -> {
            if (titleKeyword == null || titleKeyword.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            String pattern = "%" + titleKeyword.trim().toLowerCase() + "%";

            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("title")),
                    pattern
            );
        };
    }

    public static Specification<Publication> authorContains(String authorKeyword) {
        return (root, query, criteriaBuilder) -> {
            if (authorKeyword == null || authorKeyword.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            query.distinct(true);

            Join<Publication, PublicationAuthor> publicationAuthors =
                    root.join("authors", JoinType.LEFT);

            Join<PublicationAuthor, Author> author =
                    publicationAuthors.join("author", JoinType.LEFT);

            String pattern = "%" + authorKeyword.trim().toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(
                            criteriaBuilder.lower(author.get("lastName")),
                            pattern
                    ),
                    criteriaBuilder.like(
                            criteriaBuilder.lower(author.get("firstName")),
                            pattern
                    ),
                    criteriaBuilder.like(
                            criteriaBuilder.lower(author.get("middleName")),
                            pattern
                    ),
                    criteriaBuilder.like(
                            criteriaBuilder.lower(author.get("fullNameNormalized")),
                            pattern
                    )
            );
        };
    }
}