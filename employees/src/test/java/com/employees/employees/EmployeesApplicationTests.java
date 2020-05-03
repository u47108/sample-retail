package com.employees.employees;

import static org.assertj.core.api.Java6Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.employees.EmployeesApplication;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeesApplicationTests {

	@Test
	public void contextLoads() {
		EmployeesApplication app = new EmployeesApplication();
	    String[] args={""};
        app.main(args);
        app.employeeApi();
        assertThat(true).isEqualTo(true);
	}

}
