package com.employees.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Employee implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	@JsonProperty("employee_name")
	private String employeeName;
	@JsonProperty("employee_salary")
	private String employeeSalary;
	@JsonProperty("employee_age")
	private int employeeAge;
	@JsonProperty("profile_image")
	@JsonInclude(Include.NON_NULL)	
	private String profileImage;

	public Employee() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeeSalary() {
		return employeeSalary;
	}

	public void setEmployeeSalary(String employeeSalary) {
		this.employeeSalary = employeeSalary;
	}

	public int getEmployeeAge() {
		return employeeAge;
	}

	public void setEmployeeAge(int employeeAge) {
		this.employeeAge = employeeAge;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Employee [id=");
		builder.append(id);
		builder.append(", employeeName=");
		builder.append(employeeName);
		builder.append(", employeeSalary=");
		builder.append(employeeSalary);
		builder.append(", employeeAge=");
		builder.append(employeeAge);
		builder.append(", profileImage=");
		builder.append(profileImage);
		builder.append("]");
		return builder.toString();
	}

}
