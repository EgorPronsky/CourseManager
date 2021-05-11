package dao.generic;

import java.util.Optional;

public interface GenericDAO<T> {

    // CRUD
    void save(T entity);
    Optional<T> findById(long id);
    T merge(T entity);
    void delete(T entity);

}