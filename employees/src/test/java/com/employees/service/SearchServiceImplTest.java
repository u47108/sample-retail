package com.employees.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.integration.ftp.session.FtpRemoteFileTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.employees.controller.ConstantsTest;
import com.employees.dto.Employee;
import com.employees.dto.ResponseSearch;
import com.employees.dto.SearchRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
//@ContextConfiguration(classes = SpringTestConfig.class)
public class SearchServiceImplTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	//@InjectMocks
	//@Spy
	@Mock
	private RestTemplate restTemplate;

	@Mock
	private FtpRemoteFileTemplate templateFtp;
	@InjectMocks
	@Spy
	private SearchServiceImpl searchServiceImpl;
	Employee pojo;
	@Mock
	ObjectMapper objectMapper;
	@Before
	public void init() {
		searchServiceImpl = new SearchServiceImpl(restTemplate, "localhost", templateFtp, "localhost");
		//ReflectionTestUtils.setField(searchServiceImpl, "restTemplate", restTemplate);
		pojo = new Employee();
		pojo.setId(1);
		pojo.setEmployeeName("asd");
		pojo.setEmployeeAge(31);
		pojo.setEmployeeSalary("100000000000000000");
		pojo.setProfileImage("");
		ReflectionTestUtils.setField(searchServiceImpl, "restTemplate", restTemplate);
		// ReflectionTestUtils.setField(searchServiceImpl, "templateFtp", templateFtp);
	}

	@Test
	public void testSearch() throws Exception {
		//RestTemplate restTemplate = mock(RestTemplate.class);
		SearchRequest rq = new SearchRequest();
		rq.setBody("asd.csv");
		ResponseSearch entityResponse = new ResponseSearch();
		Employee[] data = { pojo };
		entityResponse.setData(data);
		Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(ResponseSearch.class)))
				.thenReturn(entityResponse);

		//Mockito.when(searchServiceImpl.search(rq)).thenReturn(entityResponse);
		ResponseSearch rs = searchServiceImpl.search(rq);
		Assert.assertNotNull(ConstantsTest.MUST_BE_A_NOTNULL, rs);
	}

}
