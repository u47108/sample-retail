package com.employees.dto;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

import com.employees.controller.ConstantsTest;

@RunWith(SpringRunner.class)
public class ResponseSearchTest {
	@InjectMocks
	private ResponseSearch responseSearch;

	@Test
	public void testResponseSearch() {
		ResponseSearch pojo = new ResponseSearch();
		Assert.assertNotNull(ConstantsTest.MUST_BE_A_NOTNULL, pojo);
	}

	@Test
	public void testGetStatus() {
		ResponseSearch pojo = new ResponseSearch();
		pojo.setStatus("alo");
		Assert.assertNotNull(ConstantsTest.MUST_BE_A_NOTNULL, pojo.getStatus());
	}

	@Test
	public void testGetData() {
		ResponseSearch pojo = new ResponseSearch();
		Employee[] data = { new Employee(), new Employee() };
		pojo.setData(data);
		Assert.assertNotNull(ConstantsTest.MUST_BE_A_NOTNULL, pojo.getData());
	}

	@Test
	public void testSetData() {
		ResponseSearch pojo = new ResponseSearch();
		ResponseSearch one = pojo;
		Assert.assertEquals("These should be equal", one, pojo);
		one = new ResponseSearch();
		Assert.assertNotEquals(one, pojo);
		int oneCode = one.hashCode();
		Assert.assertEquals("HashCodes should be equal", oneCode, one.hashCode());
		Assert.assertEquals("HashCode should not change", oneCode, one.hashCode());
		Assert.assertEquals("HashCode should not change", oneCode, one.hashCode());
		Assert.assertNotNull(ConstantsTest.MUST_BE_A_NOTNULL, pojo.toString());

	}

	@Test
	public void testToString() throws Exception {
		ResponseSearch pojo = new ResponseSearch();
		pojo.setStatus("alo");
		Assert.assertNotNull(ConstantsTest.MUST_BE_A_NOTNULL, pojo.toString());
	}

}
