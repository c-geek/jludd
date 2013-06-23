package fr.twiced.dao;

import fr.twiced.entities.PublicKey;

public interface IPublicKeyDao extends IDao<PublicKey> {

	PublicKey findByUid(String udid);
	PublicKey findByFingerprint(String fpr);
}
