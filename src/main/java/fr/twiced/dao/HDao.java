package fr.twiced.dao;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class HDao<T> implements IDao<T> {

	@PersistenceContext
	protected EntityManager em;
	
	@Override
	public Collection<T> findAll() {
		return em.createQuery("from " + entityClass().getSimpleName(), entityClass()).getResultList();
	}
	
	@Override
	public void persist(T entity) {
		em.persist(entity);
	}
	
	protected abstract Class<T> entityClass();
}
