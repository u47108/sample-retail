package com.employees.utils;


import java.util.regex.Pattern;

/**
 * @author luis
 *
 */
public class StringUtil {

  private static final Pattern PATTERN_NEW_LINE = Pattern.compile("\r\n|\r|\n|\t");
  private static final Pattern PATTERN_WHITE_SPACES = Pattern.compile("\\s");
  public static final String EXPRESSION_CLIENT = "\" : \"";
  public static final String EXPRESSION_SERVICE = "\":\"";

  public StringUtil() {
    super();
  }

  public static String replaceSpaces(final String text) {
    if (text == null) {
      return text;
    }
    String res = PATTERN_WHITE_SPACES.matcher(text).replaceAll("");
    return PATTERN_NEW_LINE.matcher(res).replaceAll("");
  }
}
