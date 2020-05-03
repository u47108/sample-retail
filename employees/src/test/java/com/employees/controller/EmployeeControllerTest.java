package com.employees.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import com.employees.dto.ResponseSearch;
import com.employees.dto.SearchRequest;
import com.employees.service.SearchService;

@RunWith(SpringRunner.class)
public class EmployeeControllerTest {
	@Mock
	private SearchService searchService;
	@InjectMocks
	private EmployeeController employeeController;

	 @Before
	    public void init() {
		 employeeController = new EmployeeController(searchService);
	       
	        
	    }

	    @Test
	    public void testSearch() {
	        ResponseSearch response= new ResponseSearch();
			Mockito.when(searchService.search(Mockito.any())).thenReturn(response);
	         SearchRequest rq=new SearchRequest();
			ResponseSearch rs = employeeController.search(rq);
	        Assert.assertNotNull(ConstantsTest.MUST_BE_A_NOTNULL, rs);
	    }
}
