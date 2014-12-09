package my.springstudy.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface BaseDao<T extends Serializable, PK extends Serializable> {

    public T get(PK id);

    public T load(PK id);

    public List<T> loadAll();

    public void update(T entity);

    public T save(T entity);

    public void delete(T entity);

    public void deleteByKey(PK id);

    public void deleteAll(Collection<T> entities); 
}
