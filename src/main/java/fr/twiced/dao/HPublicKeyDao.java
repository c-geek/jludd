package fr.twiced.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import fr.twiced.entities.PublicKey;
import fr.twiced.entities.PublicKey_;

@Repository
public class HPublicKeyDao extends HDao<PublicKey> implements IPublicKeyDao {

	@Override
	protected Class<PublicKey> entityClass() {
		return PublicKey.class;
	}

	@Override
	public PublicKey findByUid(String uid) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<PublicKey> c = cb.createQuery(PublicKey.class);
		Root<PublicKey> root = c.from(PublicKey.class);
		c.select(root);
		c.where(cb.like(root.get(PublicKey_.uid), uid));
		List<PublicKey> res = em.createQuery(c).getResultList();
		if(res.size() > 0)
			return res.get(0);
		return null;
	}

	@Override
	public void persist(PublicKey entity) {
		em.persist(entity);
	}
}
