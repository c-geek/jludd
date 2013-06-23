package fr.twiced.entities;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.util.encoders.Hex;

@Entity
@Table(name = "pubkey")
public class PublicKey {
	
	private static final transient String REGEX_UDID = "(.*) \\((udid2;[ch]{1};.{1,20};.{1,20};\\d{4}-\\d{2}-\\d{2};e[+-]\\d{2}\\.\\d{2}[+-]\\d{3}\\.\\d{2}\\;\\d;?)\\) <(.+@.+)>";
	private static final transient String REGEX_NAME_COMMENT_MAIL = "(.*) \\((.*)\\) <(.+@.+)>";
	private static final transient String REGEX_NAME_MAIL = "(.*) <(.+@.+)>";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Version
	private Integer version;
	@Column(nullable = false, length = 40, unique = true)
	private String fingerprint;
	@Column(nullable = false, length = 255, unique = true)
	private String uid;
	private Blob raw;

	@Transient
	private byte[] bytes;
	@Transient
	private String comment;
	@Transient
	private String name;
	@Transient
	private String email;
	@Transient
	private boolean processed = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setUid(String uid){
		this.uid = uid;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Blob getRaw() {
		return raw;
	}

	public void setRaw(Blob raw) {
		this.raw = raw;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public String getName() {
		processUID();
		return name;
	}

	public String getComment() {
		processUID();
		return comment;
	}

	public String getEmail() {
		processUID();
		return email;
	}

	private void processUID() {
		if(!processed){
			processed = true;
			Pattern p = Pattern.compile(REGEX_UDID);
			Matcher m = p.matcher(uid);
			if(m.matches()){
				this.name = m.group(1);
				this.comment = m.group(2);
				this.email = m.group(3);
			}
			else{
				p = Pattern.compile(REGEX_NAME_COMMENT_MAIL);
				m = p.matcher(uid);
				if(m.matches()){
					this.name = m.group(1);
					this.comment = m.group(2);
					this.email = m.group(3);
				}
				else{
					p = Pattern.compile(REGEX_NAME_MAIL);
					m = p.matcher(uid);
					if(m.matches()){
						this.name = m.group(1);
						this.comment = "";
						this.email = m.group(2);
					}
				}
			}
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUid() {
		return uid;
	}

	public String getFingerprint() {
		return fingerprint;
	}

	public void setFingerprint(String fingerprint) {
		this.fingerprint = fingerprint;
	}

	public String getFingerprintFormatted() {
		return getFormatFingerprint(fingerprint);
	}
	
	public boolean isOpenUDCkey(){
		return uid.matches(REGEX_UDID);
	}
	
	public static String getFormatFingerprint(String fingerprint) {
		Pattern p = Pattern.compile("(\\w{4})(\\w{4})(\\w{4})(\\w{4})(\\w{4})(\\w{4})(\\w{4})(\\w{4})(\\w{4})(\\w{4})");
		Matcher m = p.matcher(fingerprint);
		if(m.matches()){
			StringBuffer buff = new StringBuffer(m.group(1));
			for (int i = 2; i <= 10; i++) {
				buff.append(" " + m.group(i));
			}
			return buff.toString().toUpperCase();
		}
		return fingerprint;
	}
	
	public static PublicKey extract(PGPPublicKey pubKey) throws SerialException, SQLException, IOException{
		PublicKey pk = new PublicKey();
		String uid = extractBestUID(pubKey);
		pk.setRaw(new SerialBlob(pubKey.getEncoded()));
		pk.setUid(uid);
		pk.setBytes(pubKey.getEncoded());
		pk.setFingerprint(new String(Hex.encode(pubKey.getFingerprint())));
		return pk;
	}
	
	public static String extractBestUID(PGPPublicKey pubKey){
		Iterator<String> it = pubKey.getUserIDs();
		String bestUID = "";
		int bestLevel = -1;
		while (it.hasNext()) {
			String uid = (String) it.next();
			int level = 0;
			Pattern p = Pattern.compile(REGEX_UDID);
			Matcher m = p.matcher(uid);
			if(m.matches()){
				level = 3;
			}
			else{
				p = Pattern.compile(REGEX_NAME_COMMENT_MAIL);
				m = p.matcher(uid);
				if(m.matches()){
					level = 2;
				}
				else{
					p = Pattern.compile(REGEX_NAME_MAIL);
					m = p.matcher(uid);
					if(m.matches()){
						level = 1;
					}
				}
			}
			if(level > bestLevel){
				bestLevel = level;
				bestUID = uid;
			}
		}
		return bestUID;
	}
}
