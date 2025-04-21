package repository;

/**
 * A generic repository interface for model CRUD operations.
 * @param <T> the type of entity
*/



public interface ICRUDRepository<T> extends IRepository<T> {
    void add(T item);
    void remove(T item);
    T getById(Object id);
}
