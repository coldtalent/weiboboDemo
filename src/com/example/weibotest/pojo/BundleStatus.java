package com.example.weibotest.pojo;

import java.io.Serializable;

import com.sina.weibo.sdk.openapi.models.Status;

public class BundleStatus implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Status status;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
