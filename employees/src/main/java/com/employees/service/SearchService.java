package com.employees.service;

import com.employees.dto.ResponseSearch;
import com.employees.dto.SearchRequest;

public interface SearchService {

	ResponseSearch search(SearchRequest request);
}
