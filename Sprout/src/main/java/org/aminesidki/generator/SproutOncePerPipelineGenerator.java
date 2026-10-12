package org.aminesidki.generator;

import com.github.mustachejava.Mustache;
import java.io.IOException;
import org.aminesidki.exception.FileSystemException;

/*
 *   Generates components that only need to be generated once per project
 */
public interface SproutOncePerPipelineGenerator {
  void generate(Mustache mustache, String packageName) throws IOException, FileSystemException;
}
