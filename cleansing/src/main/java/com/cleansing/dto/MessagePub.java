package com.cleansing.dto;

import java.io.Serializable;

public class MessagePub implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String body;

	public MessagePub() {
		super();
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MessagePub [body=");
		builder.append(body);
		builder.append("]");
		return builder.toString();
	}

}
