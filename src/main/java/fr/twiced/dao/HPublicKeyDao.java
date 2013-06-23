package fr.twiced.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

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
		return getSingleResult(PublicKey_.uid, uid);
	}

	@Override
	public PublicKey findByFingerprint(String fpr) {
		return getSingleResult(PublicKey_.fingerprint, fpr);
	}
	
	private PublicKey getSingleResult(SingularAttribute<PublicKey, String> attr, String value){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<PublicKey> c = cb.createQuery(PublicKey.class);
		Root<PublicKey> root = c.from(PublicKey.class);
		c.select(root);
		c.where(cb.like(root.get(attr), value));
		List<PublicKey> res = em.createQuery(c).getResultList();
		if(res.size() > 0)
			return res.get(0);
		return null;
	}
}
