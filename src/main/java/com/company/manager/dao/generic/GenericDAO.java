package com.company.manager.dao.generic;

import java.io.Serializable;
import java.util.Optional;

public interface GenericDAO<T> {

    // CRUD
    void save(T entity);
    Optional<T> findById(Serializable id);
    void update(T entity);
    void delete(T entity);

}
