package com.employees.dto;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.employees.controller.ConstantsTest;

@RunWith(SpringRunner.class)
public class EmployeeTest {

	@Test
	public void testEmployee() throws Exception {
		Employee pojo = new Employee();
        pojo.setId(1);
        pojo.setEmployeeName("asd");
        pojo.setEmployeeAge(31);
        pojo.setEmployeeSalary("100000000000000000");
        pojo.setProfileImage("");
        Assert.assertNotNull(ConstantsTest.MUST_BE_A_NOTNULL, pojo);
        Assert.assertNotNull(ConstantsTest.MUST_BE_A_NOTNULL, pojo.getId());
        Assert.assertNotNull(ConstantsTest.MUST_BE_A_NOTNULL, pojo.getEmployeeAge());
        Assert.assertNotNull(ConstantsTest.MUST_BE_A_NOTNULL, pojo.getEmployeeName());
        Assert.assertNotNull(ConstantsTest.MUST_BE_A_NOTNULL, pojo.getEmployeeSalary());
        Assert.assertNotNull(ConstantsTest.MUST_BE_A_NOTNULL, pojo.getProfileImage());
        Employee one = pojo;
        Employee two = new Employee();
        Assert.assertEquals("These should be equal", one, pojo);
        Assert.assertNotEquals(one, two);
        int oneCode = one.hashCode();
        Assert.assertEquals("HashCodes should be equal", oneCode, pojo.hashCode());
        Assert.assertEquals("HashCode should not change", oneCode, one.hashCode());
        Assert.assertEquals("HashCode should not change", oneCode, one.hashCode());
        Assert.assertNotNull(ConstantsTest.MUST_BE_A_NOTNULL, pojo.toString());
	}
}
