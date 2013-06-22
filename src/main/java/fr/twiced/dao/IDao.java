package fr.twiced.dao;

import java.util.Collection;

public interface IDao<T> {

	void persist(T entity);
	Collection<T> findAll();
}
