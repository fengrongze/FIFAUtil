/**
 * 
 */
package com.softisland.common.utils.bean;

import java.math.BigDecimal;
import java.util.List;

import org.apache.http.client.CookieStore;

import com.softisland.common.utils.bean.SoftCookie;
import com.softisland.common.utils.bean.SoftHeader;

/**
 * @author Administrator
 *
 */
public class SessionInfo {

	private Long userId;
	private String personaId;
	private String personaName;
	private String platform;
	private String domainname;
	private String gameSku;
	private String sId;
	private String account;
	private String jSessionId;
	private String state;
	private String token;
	private BigDecimal balance;
	private String location;
	private CookieStore cookieStore;
	private String controlType;
	
	private Integer type; //0扫描机器人 1普通机器人
	private Long addTime;//添加时间
	private Long updateTime;//修改时间
	private boolean mobile;//是否为手机端session,默认为WEB端
	private String pid; //手机端
	
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getjSessionId() {
		return jSessionId;
	}
	public void setjSessionId(String jSessionId) {
		this.jSessionId = jSessionId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getPersonaId() {
		return personaId;
	}
	public void setPersonaId(String personaId) {
		this.personaId = personaId;
	}
	public String getPersonaName() {
		return personaName;
	}
	public void setPersonaName(String personaName) {
		this.personaName = personaName;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getDomainname() {
		return domainname;
	}
	public void setDomainname(String domainname) {
		this.domainname = domainname;
	}
	public String getGameSku() {
		return gameSku;
	}
	public void setGameSku(String gameSku) {
		this.gameSku = gameSku;
	}
	public String getsId() {
		return sId;
	}
	public void setsId(String sId) {
		this.sId = sId;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public CookieStore getCookieStore() {
		return cookieStore;
	}
	public void setCookieStore(CookieStore cookieStore) {
		this.cookieStore = cookieStore;
	}
	public String getControlType() {
		return controlType;
	}
	public void setControlType(String controlType) {
		this.controlType = controlType;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Long getAddTime() {
		return addTime;
	}
	public void setAddTime(Long addTime) {
		this.addTime = addTime;
	}
	public Long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

		public boolean isMobile() {
			return mobile;
		}

		public void setMobile(boolean mobile) {
			this.mobile = mobile;
		}

		public String getPid() {
			return pid;
		}

	public void setPid(String pid) {
		this.pid = pid;
	}
}
