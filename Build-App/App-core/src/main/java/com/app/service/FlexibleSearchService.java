package com.app.service;


import com.app.common.OperateQuery;
import com.app.common.OrderQuery;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.JoinType;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * Custom query
 * @author thinhnguyen
 * @param <E>
 */
public interface FlexibleSearchService<E> {

    FlexibleSearchService create(final Class<E> entityClazz);
    FlexibleSearchService createForCount(final Class<E> entityClazz);
    FlexibleSearchService like(String field, String value);
    FlexibleSearchService setFirst(Integer first);
    FlexibleSearchService setMax(Integer max);
    FlexibleSearchService consistOfDeleted();
    FlexibleSearchService distinct();
    FlexibleSearchService select(String... field);
    FlexibleSearchService orderBy(Map<String, OrderQuery> orderMap);
    FlexibleSearchService orderBy(String field, OrderQuery orderQuery);
    FlexibleSearchService join(String fieldJoin);
    FlexibleSearchService join(String fieldJoin, JoinType joinType);
    FlexibleSearchService fetchJoin(String fieldJoin);
    FlexibleSearchService fetchJoin(String fieldJoin, JoinType joinType);
    <T> FlexibleSearchService and(String field, T value);
    <T> FlexibleSearchService and(Map<String, T> params);
    <T> FlexibleSearchService gt(String field, T value);
    <T> FlexibleSearchService gte(String field, T value);
    <T> FlexibleSearchService lt(String field, T value);
    <T> FlexibleSearchService lte(String field, T value);
    <T> FlexibleSearchService or(String field, T... value);
    <T> FlexibleSearchService or(String field[], T... value);
    <T> FlexibleSearchService notIn(String field, Collection<T> values);
    <T> FlexibleSearchService notIn(String field, T... values);
    <T> FlexibleSearchService in(String field, Collection<T> values);
    <T> FlexibleSearchService in(String field, T... values);
    <T> FlexibleSearchService joinCondition(String fieldJoin, String conditionField, T value);
    <T> FlexibleSearchService joinCondition(String fieldJoin, OperateQuery operateQuery, String conditionField, T value);
    List<E> getResultList();
    Optional<E> getSingleResult();
    Long getCount();
    Query createQuery(String sqlQuery);
    Query createNativeQuery(String sqlQuery);
    Query createQuery(String sqlQuery, Map<String, Object> params);
    Query createNativeQuery(String sqlQuery, Map<String, Object> params);
    EntityManager getEntityManager();
}
