package com.example.weibotest.pojo;

import android.graphics.drawable.Drawable;

public class User {
	private int id;
	private String userId;
	private String userName;
	private String token;
	private String tokenSecret;
	// 当前用户登入状态 1代表是这个用户登录,2代表非这个用户登录,有且只有一个用户为1
	private String description;
	private String expires_in;
	private Drawable userHead;

	public User(String userId, String userName, String token,
			String tokenSecret, String description, String expires_in) {
		this.userId = userId;
		this.userName = userName;
		this.token = token;
		this.tokenSecret = tokenSecret;
		this.description = description;
		this.expires_in = expires_in;
	}

	public User(String userId, String userName, String token,
			String tokenSecret, String description, Drawable userHead,
			String expires_in) {
		this(userId, userName, token, tokenSecret, description, expires_in);
		this.userHead = userHead;
	}

	public User() {

	}

	public User(String userId, String token, String expires_in) {
		this.userId = userId;
		this.token = token;
		this.expires_in = expires_in;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTokenSecret() {
		return tokenSecret;
	}

	public void setTokenSecret(String tokenSecret) {
		this.tokenSecret = tokenSecret;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Drawable getUserHead() {
		return userHead;
	}

	public void setUserHead(Drawable userHead) {
		this.userHead = userHead;
	}

	public String getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}
}
