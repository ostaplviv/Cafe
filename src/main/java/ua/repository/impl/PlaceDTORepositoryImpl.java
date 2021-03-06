package ua.repository.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import ua.dto.PlaceDTO;
import ua.model.entity.Place;
import ua.model.entity.Place_;
import ua.model.filter.PlaceFilter;
import ua.repository.PlaceDTORepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.jpa.repository.query.QueryUtils.toOrders;

@Repository
public class PlaceDTORepositoryImpl implements PlaceDTORepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<PlaceDTO> findAllDTOs(PlaceFilter filter, Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PlaceDTO> cq = cb.createQuery(PlaceDTO.class);
        Root<Place> root = cq.from(Place.class);
        cq.multiselect(root.get(Place_.id), root.get("name"), root.get("countOfPeople"), root.get("isFree"));
        Predicate predicate = new PredicateBuilder(cb, root, filter).toPredicate();
        if (predicate != null) cq.where(predicate);
        cq.orderBy(toOrders(pageable.getSort(), root, cb));
        List<PlaceDTO> content = em.createQuery(cq)
                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Place> countRoot = countQuery.from(Place.class);
        countQuery.select(cb.count(countRoot));
        Predicate countPredicate = new PredicateBuilder(cb, countRoot, filter).toPredicate();
        if (countPredicate != null) countQuery.where(countPredicate);
        return PageableExecutionUtils.getPage(content, pageable, () -> em.createQuery(countQuery).getSingleResult());
    }

    private static class PredicateBuilder {

        final CriteriaBuilder cb;

        final Root<Place> root;

        final PlaceFilter filter;

        final List<Predicate> predicates = new ArrayList<>();

        PredicateBuilder(CriteriaBuilder cb, Root<Place> root, PlaceFilter filter) {
            this.cb = cb;
            this.root = root;
            this.filter = filter;
        }

        void findByIsFree() {
            if (filter.getIsFree().equals("isFree")) {
                predicates.add(cb.equal(root.get("isFree"), Boolean.TRUE));
            }
        }

        void findByIsNotFree() {
            if (filter.getIsFree().equals("isNotFree")) {
                predicates.add(cb.equal(root.get("isFree"), Boolean.FALSE));
            }
        }

        void findByCountOfPeople() {
            if (!filter.getCountOfPeople().isEmpty()) {
                predicates.add(root.get("countOfPeople").in(filter.getCountOfPeople()));
            }
        }

        void findBySearch() {
            if (!filter.getName().isEmpty()) {
                predicates.add(cb.equal(root.get("name"), new Integer(filter.getName())));
            }
        }

        Predicate toPredicate() {
            findByIsFree();
            findByIsNotFree();
            findByCountOfPeople();
            findBySearch();
            return predicates.isEmpty() ? null : cb.and(predicates.toArray(new Predicate[0]));
        }

    }

}
