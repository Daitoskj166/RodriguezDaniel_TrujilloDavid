package data;

import java.util.ArrayList;

public interface CRUD_Operation<T> {
    ArrayList<T> findAll();
    T findById(Object id);
    void save(T entity) throws Exception;
    void update(T entity) throws Exception;
    void delete(Object id) throws Exception;
}