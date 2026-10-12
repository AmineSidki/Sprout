package org.aminesidki.generator.filegenerator;

import com.github.mustachejava.Mustache;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.aminesidki.exception.FileSystemException;
import org.aminesidki.generator.SproutFileGenerator;
import org.aminesidki.generator.importsgenerator.GenericImportsGenerator;
import org.aminesidki.model.EntityMetadata;
import org.aminesidki.model.HelperMetadata;
import org.aminesidki.util.FileCreator;

/**
 * Specific implementation for repository classes generation of the <code>SproutFileGenerator</code>
 */
@RequiredArgsConstructor
public class RepositoryGenerator implements SproutFileGenerator {
  private final String entityPackageName;
  private final String defaultDir;
  private final GenericImportsGenerator genericImportsGenerator;
  private final Map<String, EntityMetadata> em;
  private final Map<String, HelperMetadata> hm;

  @Override
  public void generate(EntityMetadata entityMetadata, Mustache mustache)
      throws IOException, FileSystemException {
    File repoFile = FileCreator.createFile(entityMetadata.className(), "Repository", defaultDir);

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(repoFile))) {
      HashMap<String, Object> repoContext = new HashMap<>();

      repoContext.put("ClassName", entityMetadata.className());
      repoContext.put("PackageName", entityMetadata.packageName());
      repoContext.put("EntityPackageName", entityPackageName);
      repoContext.put("IdType", entityMetadata.id().type().regularName());
      repoContext.put("Imports", genericImportsGenerator.generate(entityMetadata, em, hm));

      mustache.execute(writer, repoContext);
    }
  }
}
