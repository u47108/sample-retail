package com.employees.utils;


import java.util.regex.Pattern;

import org.slf4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public final class JsonTransformer {

  private static final Pattern[] REPLACE_EXPRESIONS = {
      Pattern.compile("([\"]{1}[\\w]{1,}[\"]{1}[\\s]{1}[:]{1}[\\s]{1}null,)"),
      Pattern.compile("([\"]{1}[\\w]{1,}[\"]{1}[\\s]{1}[:]{1}[\\s]{1}null)"), Pattern.compile("\n"),
      Pattern.compile("\\s"), Pattern.compile("([\"]{1}[\\w]{1,}[\"]{1}[:]{1}[\"]{1}[\"]{1}[,]{1})") };

  /**
   * Default constructor
   */
  private JsonTransformer() {
    //Default constructor
  }

  /**
   * method that cleans a string to logear
   * @param object
   * @param logger
   * @return
   */
  public static String requestOrResponseToString(Object object, Logger logger) {
    String json = "";
    if (null != object) {
      ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
      try {
        json = ow.writeValueAsString(object);
        json = StringUtil.replaceSpaces(json);
        json = prepareJson(json);
      } catch (JsonProcessingException e) {
        logger.error(e.getMessage(), e);
      }
    }
    return json;

  }

  /**
   * method that removes the blanks of a string
   * @param json
   * @return
   */
  public static String prepareJson(String json) {
    String empty = "";
    for (Pattern pattern : REPLACE_EXPRESIONS) {
      pattern.matcher(json).replaceAll(empty);
    }
    return json;
  }

}