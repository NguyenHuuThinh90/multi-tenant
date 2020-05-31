package com.app.service.impl;

import com.app.common.ItemModel;
import com.app.service.FlexibleSearchService;
import com.app.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author thinhnguyen
 */
@Component
@Transactional("tenantTransactionManager")
public class ModelServiceImpl implements ModelService {

    @Autowired
    private FlexibleSearchService flexibleSearchService;

    private static final String METHOD_SET_DELETED = "setDeleted";
    private static final String ID_PARAMETER = "id";
    private static final String IS_DELETED_FIELD = "is_deleted";
    private static final String DELETE_BATCH_QUERY_STRING = "DELETE FROM %s x WHERE x.id IN (:id)";
    private static final String DELETE_QUERY_STRING = "DELETE FROM %s x WHERE x.id = :id";
    private static final String UPDATE_BATCH_QUERY_STRING = "UPDATE %s x SET %s=%s WHERE x.id IN (:id)";

    /**
     * Save/update model
     * @param entity
     * @param <E>
     * @return
     */
    @Override
    public <E> E save(E entity) {
        try {
            return saveOrUpdate(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Save/Update a list
     * @param entities
     * @param <E>
     * @return
     */
    @Override
    public <E> List<E> saveAll(final Collection<E> entities) {
        List<E> savedEntities = new ArrayList<>(entities.size());
        for (final E e : entities) {
            // New entity/row for DB
            this.saveItemModel(e);
            savedEntities.add(e);
        }
        return savedEntities;
    }

    /**
     * Remove entity
     * @param entity
     * @param <E>
     * @return status
     */
    @Override
    public <E> boolean remove(final E entity) {
        Long id = getEntityId(entity);
        Assert.notNull(id, "The given Iterable of id not be null!");
        if (id == null || id < 0) {
            return false;
        }
        int result = this.flexibleSearchService
                .createQuery(String.format(DELETE_QUERY_STRING, getEntityName(entity)))
                .setParameter(ID_PARAMETER, id).executeUpdate();
        return result != 0 ? true : false;
    }

    /**
     * Remove all entity
     * @param entities
     * @param <E>
     * @return status
     */
    @Override
    public <E> boolean removeAll(Collection<E> entities) {
        Assert.notNull(entities, "The given Iterable of id not be null!");
        if (CollectionUtils.isEmpty(entities)) {
            return false;
        }

        Collection<Long> ids = getIds(entities);
        String entityName = getEntityName(entities.iterator().next());

        int result = this.flexibleSearchService
                .createQuery(String.format(DELETE_BATCH_QUERY_STRING, entityName))
                .setParameter(ID_PARAMETER, ids).executeUpdate();
        return result != 0 ? true : false;
    }

    /**
     * Remove by updating delete flag
     * @param entity
     * @param <E>
     * @return
     */
    @Override
    public <E> boolean delete(E entity) {
        try {
            System.out.println(entity.getClass());
            Class<E> clazz = (Class<E>) entity.getClass();
            Method method = clazz.getMethod(METHOD_SET_DELETED, boolean.class);
            method.invoke(entity, true);
            this.updateItemModel(entity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Remove by updating a list delete flag
     * @param entities
     * @param <E>
     * @return
     */
    @Override
    public <E> boolean deleteAll(Collection<E> entities) {
        Assert.notNull(entities, "The given Iterable of id not be null!");
        if (CollectionUtils.isEmpty(entities)) {
            return false;
        }

        Collection<Long> ids = getIds(entities);
        String entityName = getEntityName(entities.iterator().next());
        int result = this.flexibleSearchService
                .createQuery(String.format(UPDATE_BATCH_QUERY_STRING, entityName, IS_DELETED_FIELD, true))
                .setParameter(ID_PARAMETER, ids).executeUpdate();
        return result != 0 ? true : false;
    }

    /**
     * Refresh data from DB
     * @param entity
     * @param <E>
     */
    @Override
    public <E> void refresh(final E entity) {
        this.flexibleSearchService.getEntityManager().refresh(entity);
    }

    /**
     * Refresh a list from DB
     * @param entities
     * @param <E>
     */
    @Override
    public <E> void refresh(final Collection<E> entities) {
        entities.stream().forEach(entity -> this.refresh(entity));
    }

    /**
     * Get a model
     * @param clazz
     * @param id
     * @param <T>
     * @return
     */
    @Override
    public <T> T  get(Class<T> clazz, long id) {
        Optional<Object> entity = this.flexibleSearchService.create(clazz)
                .and("id", id)
                .getSingleResult();
        if (entity.isPresent()) {
            return (T)entity.get();
        }
        return null;
    }

    /**
     * Find all entity by id
     * @param clazz
     * @param ids
     * @param <E>
     * @return list
     */
    @Override
    public <E> List<E> findAll(Class<E> clazz, Collection<Long> ids) {
        Assert.notNull(ids, "The given Iterable of ids not be null!");
        List<E> result = this.flexibleSearchService.create(clazz)
                .in(ID_PARAMETER, ids).getResultList();
        return result;
    }

    /**
     * Find all
     * @param clazz
     * @param <E>
     * @return list
     */
    @Override
    public <E> List<E> findAll(Class<E> clazz) {
        List<E> result = this.flexibleSearchService.create(clazz)
                .getResultList();
        return result;
    }

    /**
     * Find all with deleted entity
     * @param clazz
     * @param <E>
     * @return list
     */
    @Override
    public <E> List<E> findAllWithDeleted(Class<E> clazz) {
        List<E> result = this.flexibleSearchService.create(clazz)
                .consistOfDeleted()
                .getResultList();
        return result;
    }

    /**
     * Find all with deleted entity
     * @param clazz
     * @param ids
     * @param <E>
     * @return list
     */
    @Override
    public <E> List<E> findAllWithDeleted(Class<E> clazz, Collection<Long> ids) {
        Assert.notNull(ids, "The given Iterable of ids not be null!");
        List<E> result = this.flexibleSearchService.create(clazz)
                .in(ID_PARAMETER, ids)
                .consistOfDeleted()
                .getResultList();
        return result;
    }

    /**
     * Get with deleted flag
     * @param clazz
     * @param id
     * @param <E>
     * @return
     */
    @Override
    public <E> E getWithDeleted(Class<E> clazz, long id) {
        Optional<E> optionalEntity = this.flexibleSearchService.create(clazz)
                .and("id", id)
                .consistOfDeleted()
                .getSingleResult();
        if (optionalEntity.isPresent()) {
            return optionalEntity.get();
        }
        return null;
    }

    /**
     * Check new entity
     * @param entity
     * @param <E>
     * @return status
     */
    @Override
    public <E> boolean isNew(E entity) {
        return getEntityId(entity) == null;
    }

    @Override
    public void clear() {
        this.flexibleSearchService.getEntityManager().clear();
    }

    @Override
    public void flushAndClear() {
        this.flexibleSearchService.getEntityManager().flush();
        this.flexibleSearchService.getEntityManager().clear();
    }

    /**
     * Get id entity
     * @param entity
     * @param <E>
     * @return
     */
    protected <E> Long getEntityId(E entity) {
        if (entity instanceof ItemModel) {
            return ((ItemModel) entity).getId();
        }
        return null;
    }

    /**
     * Get name entity
     * @param entity
     * @param <E>
     * @return
     */
    protected <E> String getEntityName(E entity) {
        return entity.getClass().getSimpleName();
    }

    /**
     * Save or update entity
     * @param e
     * @param <E>
     * @return entity
     */
    protected <E> E saveOrUpdate(E e) {
        if (this.isNew(e)) {
            saveItemModel(e);
        } else {
            updateItemModel(e);
        }
        return e;
    }

    /**
     * Save entity
     * @param e
     * @param <E>
     */
    protected <E> void saveItemModel(E e) {
        this.flexibleSearchService.getEntityManager().persist(e);
    }

    /**
     * Update entity
     * @param e
     * @param <E>
     */
    protected <E> void updateItemModel(E e) {
        this.flexibleSearchService.getEntityManager().merge(e);
    }

    /**
     * Get list id from entity
     * @param entities
     * @param <E>
     * @return list id
     */
    protected <E> Collection<Long> getIds(Collection<E> entities) {
        Collection<Long> ids = entities.stream().map(e -> getEntityId(e)).collect(Collectors.toList());
        return ids;
    }
}
