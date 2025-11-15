package com.employees.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.employees.dto.ResponseSearch;

/**
 * Feign Client for Employee Search API.
 * Replaces RestTemplate with declarative HTTP client.
 */
@FeignClient(name = "employeeSearchClient", url = "${be.endpoint}")
public interface EmployeeSearchClient {

	/**
	 * Search for employees from external API.
	 * 
	 * @return ResponseSearch containing employee data
	 */
	@GetMapping
	ResponseSearch searchEmployees();
}

