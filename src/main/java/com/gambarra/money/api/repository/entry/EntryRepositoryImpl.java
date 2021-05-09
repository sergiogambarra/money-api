package com.gambarra.money.api.repository.entry;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.gambarra.money.api.dto.EntryStatisticByDay;
import com.gambarra.money.api.dto.EntryStatisticCategory;
import com.gambarra.money.api.dto.EntryStatisticPerson;
import com.gambarra.money.api.model.Person_;
import com.gambarra.money.api.model.Entry;
import com.gambarra.money.api.model.Entry_;
import com.gambarra.money.api.model.Category_;
import com.gambarra.money.api.repository.filter.EntryFilter;
import com.gambarra.money.api.repository.projection.EntryResume;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;


public class EntryRepositoryImpl implements EntryRepositoryQuery {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<EntryStatisticPerson> byPerson(LocalDate start, LocalDate end) {
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();

        CriteriaQuery<EntryStatisticPerson> criteriaQuery = criteriaBuilder.
                createQuery(EntryStatisticPerson.class);

        Root<Entry> root = criteriaQuery.from(Entry.class);

        criteriaQuery.select(criteriaBuilder.construct(EntryStatisticPerson.class,
                root.get(Entry_.type),
                root.get(Entry_.person),
                criteriaBuilder.sum(root.get(Entry_.value))));

        criteriaQuery.where(
                criteriaBuilder.greaterThanOrEqualTo(root.get(Entry_.dueDate),
                        start),
                criteriaBuilder.lessThanOrEqualTo(root.get(Entry_.dueDate),
                        end));

        criteriaQuery.groupBy(root.get(Entry_.type),
                root.get(Entry_.person));

        TypedQuery<EntryStatisticPerson> typedQuery = manager
                .createQuery(criteriaQuery);

        return typedQuery.getResultList();
    }

    @Override
    public List<EntryStatisticCategory> byCategory(LocalDate monthReference) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<EntryStatisticCategory> criteria = builder.createQuery(EntryStatisticCategory.class);

        Root<Entry> root = criteria.from(Entry.class);

        criteria.select(builder.construct(EntryStatisticCategory.class, root.get(Entry_.category),
                builder.sum(root.get(Entry_.value))));

        LocalDate firstDay = monthReference.withDayOfMonth(1);
        LocalDate lastDay = monthReference.withDayOfMonth(monthReference.lengthOfMonth());

        criteria.where(
          builder.greaterThanOrEqualTo(root.get(Entry_.dueDate),
                  firstDay),
            builder.lessThanOrEqualTo(root.get(Entry_.dueDate),
                    lastDay));

        criteria.groupBy(root.get(Entry_.category));

        TypedQuery<EntryStatisticCategory> typedQuery = manager.createQuery(criteria);

        return typedQuery.getResultList();
    }

    @Override
    public List<EntryStatisticByDay> byDay(LocalDate monthReference) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();

        CriteriaQuery<EntryStatisticByDay> criteria = builder.createQuery(EntryStatisticByDay.class);

        Root<Entry> root = criteria.from(Entry.class);

        criteria.select(builder.construct(EntryStatisticByDay.class,
                root.get(Entry_.type),
                root.get(Entry_.dueDate),
                builder.sum(root.get(Entry_.value))));

        LocalDate firstDay = monthReference.withDayOfMonth(1);
        LocalDate lastDay = monthReference.withDayOfMonth(monthReference.lengthOfMonth());

        criteria.where(
                builder.greaterThanOrEqualTo(root.get(Entry_.dueDate),
                        firstDay),
                builder.lessThanOrEqualTo(root.get(Entry_.dueDate),
                        lastDay));

        criteria.groupBy(root.get(Entry_.type), root.get(Entry_.dueDate));

        TypedQuery<EntryStatisticByDay> typedQuery = manager.createQuery(criteria);

        return typedQuery.getResultList();
    }

    @Override
    public Page<Entry> filter(EntryFilter entryFilter, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Entry> criteria = builder.createQuery(Entry.class);
        Root<Entry> root = criteria.from(Entry.class);

        Predicate[] predicates = criarRestricoes(entryFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Entry> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(entryFilter));
    }


    @Override
    public Page<EntryResume> resume(EntryFilter EntryFilter, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<EntryResume> criteria = builder.createQuery(EntryResume.class);
        Root<Entry> root = criteria.from(Entry.class);

        criteria.select(builder.construct(EntryResume.class
                , root.get(Entry_.id), root.get(Entry_.description)
                , root.get(Entry_.dueDate), root.get(Entry_.payDate)
                , root.get(Entry_.value), root.get(Entry_.type)
                , root.get(Entry_.category).get(Category_.name)
                , root.get(Entry_.person).get(Person_.name)));

        Predicate[] predicates = criarRestricoes(EntryFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<EntryResume> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(EntryFilter));
    }

    private Predicate[] criarRestricoes(EntryFilter entryFilter, CriteriaBuilder builder,
                                        Root<Entry> root) {
        List<Predicate> predicates = new ArrayList<>();

        if (!StringUtils.isEmpty(entryFilter.getDescription())) {
            predicates.add(builder.like(
                    builder.lower(root.get(Entry_.description)), "%" + entryFilter.getDescription().toLowerCase() + "%"));
        }

        if (entryFilter.getDueDateFrom() != null) {
            predicates.add(
                    builder.greaterThanOrEqualTo(root.get(Entry_.dueDate), entryFilter.getDueDateFrom()));
        }

        if (entryFilter.getDueDateTo() != null) {
            predicates.add(
                    builder.lessThanOrEqualTo(root.get(Entry_.dueDate), entryFilter.getDueDateTo()));
        }

        return predicates.toArray(new Predicate[predicates.size()]);
    }

    private void adicionarRestricoesDePaginacao(TypedQuery<?> query, Pageable pageable) {
        int paginaAtual = pageable.getPageNumber();
        int totalRegistrosPorPagina = pageable.getPageSize();
        int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;

        query.setFirstResult(primeiroRegistroDaPagina);
        query.setMaxResults(totalRegistrosPorPagina);
    }

    private Long total(EntryFilter EntryFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Entry> root = criteria.from(Entry.class);

        Predicate[] predicates = criarRestricoes(EntryFilter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }

}