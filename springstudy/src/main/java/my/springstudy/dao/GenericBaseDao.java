package my.springstudy.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Repository()
public class GenericBaseDao<T extends Serializable, PK extends Serializable> implements BaseDao<T, PK> {

	@Autowired
	protected HibernateTemplate hibernateTemplate;

	protected Class<T> entityClass;

	public T get(PK id) {
		return (T) this.hibernateTemplate.get(entityClass, id);
	}

	public T load(PK id) {
		return (T) this.hibernateTemplate.load(entityClass, id);
	}

	public List<T> loadAll() {
		return (List<T>) this.hibernateTemplate.loadAll(entityClass);
	}

	public void update(T entity) {
		this.hibernateTemplate.update(entity);
	}

	public void save(T entity) {
		this.hibernateTemplate.save(entity);
	}

	public void delete(T entity) {
		this.hibernateTemplate.delete(entity);
	}

	public void deleteByKey(PK id) {
		this.delete(this.load(id));
	}

	public void deleteAll(Collection<T> entities) {
		this.hibernateTemplate.deleteAll(entities);
	}

	public Class<T> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<T> entityClass) {
		this.entityClass = entityClass;
	}
}
