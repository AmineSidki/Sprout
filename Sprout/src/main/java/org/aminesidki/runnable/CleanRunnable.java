package org.aminesidki.runnable;

import java.io.File;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.aminesidki.util.Logger;
import org.apache.commons.io.FileUtils;

@RequiredArgsConstructor
public class CleanRunnable implements Runnable {
  private final String defaultDir;

  @Override
  public void run() {
    File annotationsPackage = new File(defaultDir + File.separator + "sproutannotation");
    try {
      FileUtils.deleteDirectory(annotationsPackage);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    System.out.println(Logger.getInstance().infoMessage("Finished cleaning."));
  }
}
