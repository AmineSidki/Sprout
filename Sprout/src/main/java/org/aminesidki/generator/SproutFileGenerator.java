package org.aminesidki.generator;

import com.github.mustachejava.Mustache;
import java.io.IOException;
import org.aminesidki.exception.FileSystemException;
import org.aminesidki.model.EntityMetadata;

/** Generates source files for given entity */
public interface SproutFileGenerator {
  void generate(EntityMetadata entityMetadata, Mustache mustache)
      throws IOException, FileSystemException;
}
