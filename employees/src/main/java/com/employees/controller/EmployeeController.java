package com.employees.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.employees.dto.ResponseSearch;
import com.employees.dto.SearchRequest;
import com.employees.service.SearchService;
import com.employees.utils.JsonTransformer;

import io.swagger.annotations.ApiOperation;

@RestController("employeeController")
@RequestMapping("/api/employee")
public class EmployeeController {

	SearchService searchService;
	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

	@Autowired
	public EmployeeController(SearchService searchService) {
		this.searchService = searchService;
	}

	@CrossOrigin(origins = "*")
	@PostMapping("/search")
	@ApiOperation(value = "busca los employee y genera un archivo csv a partir de la respuesta del JSON de employee dummy")
	public ResponseSearch search(@RequestBody(required = true) SearchRequest request) {
		JsonTransformer.requestOrResponseToString(request, LOGGER);
		return searchService.search(request);
	}
}
