package com.app.service;

import java.util.Collection;
import java.util.List;

/**
 * @author thinhnguyen
 */
public interface ModelService {
    <E> E save(E entity);
    <E> List<E> saveAll(Collection<E> entities);
    <E> boolean remove(E entity);
    <E> boolean removeAll(Collection<E> entities);
    <E> boolean delete(E entity);
    <E> boolean deleteAll(Collection<E> entities);
    <E> void refresh(E entity);
    <E> void refresh(Collection<E> entities);
    <E> E  get(Class<E> clazz, long id);
    <E> List<E> findAll(Class<E> clazz, Collection<Long> ids);
    <E> List<E> findAll(Class<E> clazz);
    <E> List<E> findAllWithDeleted(Class<E> clazz);
    <E> List<E> findAllWithDeleted(Class<E> clazz, Collection<Long> ids);
    <E> E  getWithDeleted(Class<E> clazz, long id);
    <E> boolean isNew(E entity);
    void clear();
    void flushAndClear();
}
