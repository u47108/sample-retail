package com.employees.dto;

import java.io.Serializable;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ResponseSearch implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String status;
	@JsonInclude(Include.NON_NULL)	
	private Employee[] data;
	
	public ResponseSearch() {}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Employee[] getData() {
		return data;
	}

	public void setData(Employee[] data) {
		this.data = data;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ResponseSearch [status=");
		builder.append(status);
		builder.append(", data=");
		builder.append(Arrays.toString(data));
		builder.append("]");
		return builder.toString();
	}
	

}
