package repository;

import java.util.List;

/**
 * A generic repository interface for basic CRUD operations.
 * @param <T> the type of entity
 */

public interface IRepository<T> {
    List<T> getAll();
    void load();
    void update();
    
}
