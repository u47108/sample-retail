package com.employees.dto;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import com.employees.controller.ConstantsTest;

@RunWith(SpringRunner.class)
public class SearchRequestTest {


	@Test
	public void testSearchRequest(){
		SearchRequest pojo = new SearchRequest();
	        pojo.setBody("1");
	       
	        Assert.assertNotNull(ConstantsTest.MUST_BE_A_NOTNULL, pojo);
	        Assert.assertNotNull(ConstantsTest.MUST_BE_A_NOTNULL, pojo.getBody());
	        SearchRequest one = pojo;
	        SearchRequest two = new SearchRequest();
	        Assert.assertEquals("These should be equal", one, pojo);
	        Assert.assertNotEquals(one, two);
	        int oneCode = one.hashCode();
	        Assert.assertEquals("HashCodes should be equal", oneCode, pojo.hashCode());
	        Assert.assertEquals("HashCode should not change", oneCode, one.hashCode());
	        Assert.assertEquals("HashCode should not change", oneCode, one.hashCode());
	        Assert.assertNotNull(ConstantsTest.MUST_BE_A_NOTNULL, pojo.toString());
	}

	
}
