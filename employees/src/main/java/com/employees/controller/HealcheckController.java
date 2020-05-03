package com.employees.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employee")
public class HealcheckController {

  /**
   * Endpoint para hacer healthcheck
   */
  @CrossOrigin(origins = "*")
  @RequestMapping(value = "/healthcheck", method = RequestMethod.GET)
  public ResponseEntity<String> healthcheck() {
    return new ResponseEntity<>("OK STATUS 200", HttpStatus.OK);
  }
}
