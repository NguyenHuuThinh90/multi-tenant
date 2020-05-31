package com.app.service.impl;

import com.app.common.OperateQuery;
import com.app.common.OrderQuery;
import com.app.service.FlexibleSearchService;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.*;

/**
 * @author thinhnguyen
 */
@Component
public class FlexibleSearchServiceImpl<E> implements FlexibleSearchService<E> {

    @PersistenceContext(unitName = "tenant-database-persistence-unit")
    private EntityManager entityManager;

    private CriteriaBuilder criteriaBuilder;
    private CriteriaQuery criteriaQuery;
    private Root<E> root;
    private Collection<Predicate> predicates;
    private Map<String, Join> joinMap;
    private Integer first = null;
    private Integer max = null;
    private boolean isDeleted;
    private List<Order> orders;

    /**
     * Initialize query
     * @param entityClazz
     * @return
     */
    @Override
    public FlexibleSearchService create(final Class<E> entityClazz) {
        init(entityClazz);
        return this;
    }

    /**
     * Initialize query for count record
     * @param entityClazz
     * @return
     */
    @Override
    public FlexibleSearchService createForCount(final Class<E> entityClazz) {
        init(entityClazz);
        this.criteriaQuery.select(this.criteriaBuilder.count(this.root));
        return this;
    }

    /**
     * And condition query
     * @param field field condition
     * @param value value condition
     * @return
     */
    @Override
    public <T> FlexibleSearchService and(String field, T value) {
        this.predicates.add(this.criteriaBuilder.equal(this.root.get(field), value));
        return this;
    }

    @Override
    public <T> FlexibleSearchService and(Map<String, T> params) {
        for (Map.Entry<String, T> entry: params.entrySet()) {
            this.predicates.add(this.criteriaBuilder.equal(this.root.get(entry.getKey()), entry.getValue()));
        }
        return this;
    }

    @Override
    public <T> FlexibleSearchService gt(String field, T value) {
        Path expression = this.root.get(field);
        this.predicates.add(this.criteriaBuilder.greaterThan(expression, (Comparable) value));
        return this;
    }

    @Override
    public <T> FlexibleSearchService gte(String field, T value) {
        Path expression = this.root.get(field);
        this.predicates.add(this.criteriaBuilder.greaterThanOrEqualTo(expression, (Comparable) value));
        return this;
    }

    @Override
    public <T> FlexibleSearchService lt(String field, T value) {
        Path expression = this.root.get(field);
        this.predicates.add(this.criteriaBuilder.lessThan(expression, (Comparable) value));
        return this;
    }

    @Override
    public <T> FlexibleSearchService lte(String field, T value) {
        Path expression = this.root.get(field);
        this.predicates.add(this.criteriaBuilder.lessThanOrEqualTo(expression, (Comparable) value));
        return this;
    }

    @Override
    public FlexibleSearchService like(String field, String value) {
        Path<String> expression = this.root.get(field);
        this.predicates.add(this.criteriaBuilder.like(expression, value));
        return this;
    }

    /**
     * Or condition query
     * @param fields multiple field
     * @param values multiple value
     * @return
     */
    @Override
    public <T> FlexibleSearchService or(String[] fields, T... values) {
        Predicate[] predicates = new Predicate[values.length];
        for(String field: fields) {
            Path<T> path = this.root.get(field);
            for (int i = 0 ; i < values.length; i++) {
                predicates[i] = this.criteriaBuilder.equal(path, values[i]);
            }
        }
        this.predicates.add(this.criteriaBuilder.or(predicates));
        return this;
    }

    /**
     * In query
     * @param field
     * @param values
     * @return
     */
    @Override
    public <T> FlexibleSearchService in(String field, Collection<T> values) {
        Expression<String> expression = this.root.get(field);
        this.predicates.add(expression.in(values));
        return this;
    }

    @Override
    public <T> FlexibleSearchService in(String field, T... values) {
        Expression<String> expression = this.root.get(field);
        this.predicates.add(expression.in(values));
        return this;
    }

    @Override
    public <T> FlexibleSearchService notIn(String field, Collection<T> values) {
        Expression<String> expression = this.root.get(field);
        this.predicates.add(expression.in(values).not());
        return this;
    }

    @Override
    public <T> FlexibleSearchService notIn(String field, T... values) {
        Expression<String> expression = this.root.get(field);
        this.predicates.add(expression.in(values).not());
        return this;
    }

    /**
     * Or condition query
     * @param field
     * @param values
     * @return
     */
    @Override
    public <T> FlexibleSearchService or(String field, T... values) {
        Predicate[] predicates = new Predicate[values.length];
        for (int i = 0 ; i < values.length; i++) {
            predicates[i] = this.criteriaBuilder.equal(this.root.get(field), values[i]);
        }
        this.predicates.add(this.criteriaBuilder.or(predicates));
        return this;
    }

    @Override
    public FlexibleSearchService fetchJoin(String fieldJoin) {
        this.root.fetch(fieldJoin);
        this.joinMap.put(fieldJoin, this.root.join(fieldJoin));
        return this;
    }

    @Override
    public FlexibleSearchService fetchJoin(String fieldJoin, JoinType joinType) {
        this.root.fetch(fieldJoin, joinType);
        this.joinMap.put(fieldJoin, this.root.join(fieldJoin, joinType));
        return this;
    }

    /**
     * Join table
     * @param fieldJoin
     * @return
     */
    @Override
    public FlexibleSearchService join(String fieldJoin) {
        this.joinMap.put(fieldJoin, this.root.join(fieldJoin));
        return this;
    }

    @Override
    public FlexibleSearchService join(String fieldJoin, JoinType joinType) {
        this.joinMap.put(fieldJoin, this.root.join(fieldJoin, joinType));
        return this;
    }

    @Override
    public <T> FlexibleSearchService joinCondition(String fieldJoin, String conditionField, T value) {
        Join join = this.joinMap.get(fieldJoin);
        Path expression = join.get(conditionField);
        this.predicates.add(this.operaterCondition(OperateQuery.EQUAL, expression, value));
        return this;
    }

    @Override
    public <T> FlexibleSearchService joinCondition(String fieldJoin, OperateQuery operateQuery, String conditionField, T value) {
        Join join = this.joinMap.get(fieldJoin);
        Path expression = join.get(conditionField);
        this.predicates.add(this.operaterCondition(operateQuery, expression, value));
        return this;
    }

    /**
     * Order by
     * @param orderMap
     * @return
     */
    @Override
    public FlexibleSearchService orderBy(Map<String, OrderQuery> orderMap) {
        if (orderMap != null && !CollectionUtils.isEmpty(orderMap.values())) {
            orderMap.entrySet().forEach(entry -> {
                if (OrderQuery.ASC.equals(entry.getValue())) {
                    orders.add(criteriaBuilder.asc(this.root.get(entry.getKey())));
                } else {
                    orders.add(criteriaBuilder.desc(this.root.get(entry.getKey())));
                }
            });
        }
        return this;
    }

    /**
     * Order by
     * @param field
     * @param orderQuery
     * @return
     */
    @Override
    public FlexibleSearchService orderBy(String field, OrderQuery orderQuery) {
        if (!StringUtils.isEmpty(field) && orderQuery != null) {
            if (OrderQuery.ASC.equals(orderQuery)) {
                orders.add(criteriaBuilder.asc(this.root.get(field)));
            } else {
                orders.add(criteriaBuilder.desc(this.root.get(field)));
            }
        }
        return this;
    }

    /**
     * Offset record
     * @param first
     * @return
     */
    @Override
    public FlexibleSearchService setFirst(Integer first) {
        this.first = first;
        return this;
    }

    /**
     * Limit record display
     * @param max
     * @return
     */
    @Override
    public FlexibleSearchService setMax(Integer max) {
        this.max = max;
        return this;
    }

    /**
     * Get result list
     * @return
     */
    @Override
    public List<E> getResultList() {
        processCondition();

        if (!CollectionUtils.isEmpty(orders)) {
            this.criteriaQuery.orderBy(orders);
        }

        TypedQuery<E> typedQuery = getTypedQuery();

        List<E> result = typedQuery.getResultList();

        if (CollectionUtils.isEmpty(result)) {
            return Collections.emptyList();
        }

        return result;
    }

    private TypedQuery<E> getTypedQuery() {
        TypedQuery<E> typedQuery = this.entityManager.createQuery(this.criteriaQuery);

        if (this.first != null) {
            typedQuery.setFirstResult(this.first);
        }
        if (this.max != null) {
            typedQuery.setMaxResults(this.max);
        }
        return typedQuery;
    }

    /**
     * Get single result
     * @return optional
     */
    @Override
    public Optional<E> getSingleResult() {
        processCondition();
        try {
            TypedQuery<E> typedQuery = this.entityManager.createQuery(this.criteriaQuery);
            return Optional.ofNullable(typedQuery.getSingleResult());
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Get count record
     * @return
     */
    @Override
    public Long getCount() {
        processCondition();
        TypedQuery<Long> typedQuery = this.entityManager.createQuery(criteriaQuery);
        return typedQuery.getSingleResult();
    }

    private void processCondition() {
        this.defaultCondition();
        Predicate[] predicates = this.predicates.size() > 0 ? new Predicate[this.predicates.size()] : null;

        if (predicates != null) {
            this.criteriaQuery.where(this.predicates.toArray(predicates));
        }
    }

    /**
     * Add condition get item deleted
     * @return
     */
    @Override
    public FlexibleSearchService consistOfDeleted() {
        this.isDeleted = true;
        return this;
    }

    /**
     * Add condition distinct
     * @return
     */
    @Override
    public FlexibleSearchService distinct() {
        this.criteriaQuery.distinct(true);
        return this;
    }

    /**
     * Select fields in table
     * @param fields
     * @return
     */
    @Override
    public FlexibleSearchService select(String... fields) {
        List<Selection<?>> selections = new ArrayList<>(fields.length);
        Arrays.asList(fields).forEach(f -> {
            Expression<Object> expression = this.root.get(f);
            selections.add(expression);
        });
        this.criteriaQuery.multiselect(selections);
        return this;
    }

    /**
     * Default condition
     * @return
     */
    private FlexibleSearchService defaultCondition() {
        if (!this.isDeleted) {
            this.and("isDeleted", this.isDeleted);
        }
        return this;
    }

    /**
     * Create HSQL
     * @param sqlQuery
     * @return
     */
    @Override
     public Query createQuery(String sqlQuery) {
        return this.entityManager.createQuery(sqlQuery);
    }

    /**
     * Create Native query
     * @param sqlQuery
     * @return
     */
    @Override
    public Query createNativeQuery(String sqlQuery) {
        return this.entityManager.createNativeQuery(sqlQuery);
    }

    /**
     * Create HSQL
     * @param sqlQuery
     * @param params
     * @return
     */
    @Override
    public Query createQuery(String sqlQuery, Map<String, Object> params) {
        Query query = this.createQuery(sqlQuery);
        if (!params.isEmpty()) {
            for (Map.Entry<String, Object> entry: params.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return query;
    }

    /**
     * Create Native query
     * @param sqlQuery
     * @param params
     * @return
     */
    @Override
    public Query createNativeQuery(String sqlQuery, Map<String, Object> params) {
        Query query = this.createNativeQuery(sqlQuery);
        if (!params.isEmpty()) {
            for (Map.Entry<String, Object> entry: params.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return query;
    }

    /**
     * Operater condition
     * @param operateQuery
     * @param expression
     * @param value
     * @param <T>
     * @return
     */
    protected <T> Predicate operaterCondition(OperateQuery operateQuery, Path expression, T value) {
        switch (operateQuery) {
            case GREATE_THAN:
                return this.criteriaBuilder.greaterThan(expression, (Comparable) value);
            case GREATE_THAN_OR_EQUAL:
                return this.criteriaBuilder.greaterThanOrEqualTo(expression, (Comparable) value);
            case LESS_THAN:
                return this.criteriaBuilder.lessThan(expression, (Comparable) value);
            case LESS_THAN_OR_EQUAL:
                return this.criteriaBuilder.lessThanOrEqualTo(expression, (Comparable) value);
            case EQUAL:
                return this.criteriaBuilder.equal(expression, value);
            case NOT_EQUAL:
                return this.criteriaBuilder.notEqual(expression, value);
        }
        return null;
    }

    /**
     * Initialize class
     * @param entityClazz
     */
    protected void init(final Class<E> entityClazz) {
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
        this.criteriaQuery = this.criteriaBuilder.createQuery(entityClazz);
        this.root = this.criteriaQuery.from(entityClazz);
        this.predicates = new ArrayList<Predicate>();
        this.isDeleted = false;
        this.orders = new ArrayList<>();
        this.joinMap = new HashMap<>();
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }
}
