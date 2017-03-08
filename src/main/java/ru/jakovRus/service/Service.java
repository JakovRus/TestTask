package ru.jakovRus.service;

import ru.jakovRus.entity.Entity;
import ru.jakovRus.util.EntityNotUniqueException;

import javax.annotation.Nullable;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

/**
 * @author Jakov Rogov
 */
public interface Service<T extends Entity> {
    public abstract List<T> findAll();

    public abstract List<T> findAll(@Nullable String firstFilter, @Nullable String secondFilter);

    public abstract void save(T entity) throws EntityNotUniqueException;

    public abstract void delete(T entity);
}
