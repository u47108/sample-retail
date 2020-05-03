package com.employees.controller;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class HealcheckControllerTest {

  @Test
  public void testHealthcheck() {
    HealcheckController controller = new HealcheckController();
    ResponseEntity<String> rs = controller.healthcheck();
    Assert.assertNotNull("Must be a not null", rs);
  }
}