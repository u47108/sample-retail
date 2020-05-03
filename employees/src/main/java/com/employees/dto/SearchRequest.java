package com.employees.dto;

import java.io.Serializable;

public class SearchRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String body;

	public SearchRequest() {
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
		builder.append("SearchRequest [body=");
		builder.append(body);
		builder.append("]");
		return builder.toString();
	}

}
