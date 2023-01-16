package dev.alnat.tinylinkshortener.repository.impl;

import dev.alnat.tinylinkshortener.dto.LinkVisitSearchRequest;
import dev.alnat.tinylinkshortener.dto.common.Sorting;
import dev.alnat.tinylinkshortener.model.Link;
import dev.alnat.tinylinkshortener.model.Visit;
import dev.alnat.tinylinkshortener.model.enums.VisitStatus;
import dev.alnat.tinylinkshortener.repository.VisitSearchRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by @author AlNat on 11.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@RequiredArgsConstructor
@Repository
public class VisitSearchRepositoryImpl implements VisitSearchRepository {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public List<Visit> search(final LinkVisitSearchRequest filter) {
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Visit> criteriaQuery = cb.createQuery(Visit.class);
        final Root<Visit> table = criteriaQuery.from(Visit.class); // From

        final List<Predicate> conditions = new ArrayList<>(); // Where conditions
        final List<Order> orderList = new ArrayList<>(); // Ordering

        // Join Link if need filter with it
        if (StringUtils.hasText(filter.getShortLink())) {
            Join<Visit, Link> linkJoin = table.join("link");
            conditions.add(cb.equal(linkJoin.get("shortLink"), filter.getShortLink()));
        }

        // Status
        if (filter.getStatus() != null && !filter.getStatus().isEmpty()) {
            conditions.add(table.<VisitStatus>get("status").in(filter.getStatus()));
        }

        // Created from
        if (filter.getVisitedFrom() != null) {
            conditions.add(cb.greaterThanOrEqualTo(table.get("created"), filter.getVisitedFrom()));
        }

        // Created to
        if (filter.getVisitedTo() != null) {
            conditions.add(cb.lessThanOrEqualTo(table.get("created"), filter.getVisitedTo()));
        }

        // Sorting
        if (filter.getSorting() != null && StringUtils.hasText(filter.getFiledSort())) {
            final Order order;
            final Expression<?> orderExpression = table.get(filter.getFiledSort());

            // Default sorting is ASC
            if (filter.getSorting().getOrder() == null || filter.getSorting().getOrder() == Sorting.SortOrder.ASC) {
                order = cb.asc(orderExpression);
            } else {
                order = cb.desc(orderExpression);
            }

            orderList.add(order);
        } else {
            // default soring by id
            orderList.add(cb.asc(table.get("id")));
        }

        criteriaQuery = criteriaQuery
                .where(cb.and(conditions.toArray(new Predicate[0])))
                .orderBy(orderList);

        return em
                .unwrap(Session.class)
                .createQuery(criteriaQuery)
                .setReadOnly(true)
                .setFirstResult(filter.getOffset())
                .setMaxResults(filter.getLimit() + 1) // For hasNextPage flag
                .getResultList();
    }

}
