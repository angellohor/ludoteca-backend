package com.ccsw.tutorial.rental;

import com.ccsw.tutorial.common.criteria.SearchCriteria;
import com.ccsw.tutorial.rental.model.Rental;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class RentalSpecification implements Specification<Rental> {

    private static final long serialVersionUID = 1L;

    private final SearchCriteria criteria;

    public RentalSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Rental> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (criteria.getValue() == null) {
            return null;
        }
        Path path = getPath(root);

        if (criteria.getOperation().equalsIgnoreCase(":") && criteria.getValue() != null) {
            if (path.getJavaType() == String.class) {
                return criteriaBuilder.like(path, "%" + criteria.getValue() + "%");
            } else {
                return criteriaBuilder.equal(path, criteria.getValue());
            }
        } else if (criteria.getOperation().equalsIgnoreCase("<=")) {
            return criteriaBuilder.lessThanOrEqualTo(path, (Comparable) criteria.getValue());
        } else if (criteria.getOperation().equalsIgnoreCase(">=")) {
            return criteriaBuilder.greaterThanOrEqualTo(path, (Comparable) criteria.getValue());
        }
        return null;
    }

    private Path getPath(Path<Rental> root) {
        String key = criteria.getKey();
        String[] split = key.split("[.]", 0);

        Path expression = root.get(split[0]);
        for (int i = 1; i < split.length; i++) {
            expression = expression.get(split[i]);
        }

        return expression;
    }
}
