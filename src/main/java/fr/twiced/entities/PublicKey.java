package fr.twiced.entities;

import java.sql.Blob;
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
	private String udid2;
	@Transient
	private String name;
	@Transient
	private String email;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setUid(String uid){
		processUid(uid);
	}

	public void processUid(){
		processUid(uid);
	}

	public void processUid(String uid){
		Pattern p = Pattern.compile(REGEX_UDID);
		Matcher m = p.matcher(uid);
		if(m.matches()){
			name = m.group(1);
			udid2 = m.group(2);
			email = m.group(3);
		}
		else{
			p = Pattern.compile(REGEX_NAME_COMMENT_MAIL);
			m = p.matcher(uid);
			if(m.matches()){
				name = m.group(1);
				udid2 = m.group(2);
				email = m.group(3);
			}
			else{
				p = Pattern.compile(REGEX_NAME_MAIL);
				m = p.matcher(uid);
				if(m.matches()){
					name = m.group(1);
					email = m.group(2);
				}
			}
		}
		this.uid = uid;
		udid2 = udid2 == null ? "" : udid2;
		name = name == null ? "" : name;
		email = email == null ? "" : email;
		System.out.println(name + " " + udid2 + " " + email);
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

	public String getUdid2() {
		return udid2;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public void setUdid(String udid) {
		this.udid2 = udid;
	}

	public void setName(String name) {
		this.name = name;
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
}
