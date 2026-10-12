package org.aminesidki.util;

import java.io.InputStream;
import java.util.Properties;

/** Retrieves the program's version out of the <code>pom.xml</code> */
public class VersionUtil {
  public static final String version = getVersion();

  public static String getVersion() {
    try (InputStream input =
        VersionUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
      if (input != null) {
        Properties prop = new Properties();
        prop.load(input);
        return prop.getProperty("app.version", "Unknown");
      }
    } catch (Exception ex) {
      System.err.println("Could not load version properties: " + ex.getMessage());
    }
    return "";
  }
}
