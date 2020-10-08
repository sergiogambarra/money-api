package com.gambarra.money.api.repository.entry;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.gambarra.money.api.model.*;
import com.gambarra.money.api.repository.entry.EntryRepositoryQuery;
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