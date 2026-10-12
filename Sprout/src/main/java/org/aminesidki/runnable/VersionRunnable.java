package org.aminesidki.runnable;

import java.time.LocalDateTime;
import org.aminesidki.util.VersionUtil;
import picocli.CommandLine;

public class VersionRunnable implements Runnable {
  @Override
  public void run() {
    System.out.println(
        CommandLine.Help.Ansi.AUTO.string(
            "@|faint "
                + LocalDateTime.now()
                + "|@ @|bold,blue  INFO|@ --- @|magenta [Sprout]|@ : Sprout version v"
                + VersionUtil.version
                + " is installed !"));
  }
}
