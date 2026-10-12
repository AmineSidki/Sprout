package org.aminesidki.util;

public class StringUtils {
  public static String toCamelCase(String str) {
    return str.substring(0, 1).toLowerCase() + str.substring(1);
  }
}
