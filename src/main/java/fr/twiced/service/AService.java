package fr.twiced.service;

import java.util.Collection;

import fr.twiced.dao.IDao;

public abstract class AService<T> {
	
	public Collection<T> findAll(){
		return getDao().findAll();
	}
	
	protected abstract IDao<T> getDao();
}
