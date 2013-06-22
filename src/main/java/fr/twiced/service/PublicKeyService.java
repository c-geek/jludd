package fr.twiced.service;

import java.io.IOException;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.twiced.dao.IDao;
import fr.twiced.dao.IPublicKeyDao;
import fr.twiced.entities.PublicKey;
import fr.twiced.pgp.PGPHelper;

@Service
@Transactional
public class PublicKeyService extends AService<PublicKey> {

	@Autowired
	private IPublicKeyDao dao;
	
	public PublicKey findByUdid(String udid){
		return dao.findByUid(udid);
	}
	
	public void add(String stream) {
		Iterator<PublicKey> it = PGPHelper.extract(stream).iterator();
		while (it.hasNext()) {
			PublicKey publicKey = (PublicKey) it.next();
			if(dao.findByUid(publicKey.getUid()) == null)
				dao.persist(publicKey);
		}
	}

	public String getFullArmored() {
		String res = "";
		Iterator<PublicKey> it = dao.findAll().iterator();
		byte[] buff = new byte[0];
		while (it.hasNext()) {
			PublicKey pk = (PublicKey) it.next();
			byte[] append = pk.getBytes(), temp = buff.clone();
			buff = new byte[buff.length + append.length];
			System.arraycopy(temp, 0, buff, 0, temp.length);
			System.arraycopy(append, 0, buff, temp.length, append.length);
		}
		try {
			res = PGPHelper.enarmor(buff);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}

	@Override
	protected IDao<PublicKey> getDao() {
		return dao;
	}
}
