package hznu.grade15x.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
/*
 * 实体类，表中对应的table名user，字段即为属性名
 * 采用默认的主键生成方式
 */
@Table(name="user")
@Entity
public class User {
	@GeneratedValue
	@Id
	private Integer id;
	private String username;
	private String secretKey;
	private Integer Totp=1;//是否开启谷歌身份验证 1->open 2->close
	//新增s_key需求
	private String serverMD5String;
	private Integer userLoginTime=0;
	public String getServerMD5String() {
		return serverMD5String;
	}
	public void setServerMD5String(String serverMD5String) {
		this.serverMD5String = serverMD5String;
	}
	public Integer getUserLoginTime() {
		return userLoginTime;
	}
	
	public void setUserLoginTime(Integer userLoginTime) {
		this.userLoginTime = userLoginTime;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	
	public Integer getTotp() {
		return Totp;
	}
	public void setTotp(Integer totp) {
		Totp = totp;
	}
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	public User(Integer id, String username, String secretKey) {
		super();
		this.id = id;
		this.username = username;
		this.secretKey = secretKey;
	}
	public User() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", secretKey=" + secretKey + ", serverMD5String="
				+ serverMD5String + ", userLoginTime=" + userLoginTime + "]";
	}
}
