package org.aminesidki.generator.onceperpipelinegenerator;

import com.github.mustachejava.Mustache;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.aminesidki.exception.FileSystemException;
import org.aminesidki.generator.SproutOncePerPipelineGenerator;
import org.aminesidki.util.FileCreator;

/**
 * Specific implementation for ResourceNotFound exception class generation of the <code>
 * SproutOncePerPipelineGenerator</code>
 */
@RequiredArgsConstructor
public class ResourceNotFoundExceptionGenerator implements SproutOncePerPipelineGenerator {
  private final String defaultDir;

  @Override
  public void generate(Mustache mustache, String packageName)
      throws IOException, FileSystemException {
    File exceptionFile = FileCreator.createFile("ResourceNotFound", "Exception", defaultDir);

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(exceptionFile))) {
      mustache.execute(writer, Map.of("PackageName", packageName));
    }
  }
}
