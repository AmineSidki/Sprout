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
 * Specific implementation for service classes generation of the <code>SproutFileGenerator</code>
 */
@RequiredArgsConstructor
public class ServiceGenerator implements SproutFileGenerator {
  private final String entityPackageName;
  private final String defaultDir;
  private final GenericImportsGenerator genericImportsGenerator;
  private final Map<String, EntityMetadata> em;
  private final Map<String, HelperMetadata> hm;

  public void generate(EntityMetadata entityMetadata, Mustache mustache)
      throws IOException, FileSystemException {
    File serviceFile = FileCreator.createFile(entityMetadata.className(), "Service", defaultDir);

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(serviceFile))) {
      HashMap<String, Object> serviceContext = new HashMap<>();

      serviceContext.put("ClassName", entityMetadata.className());
      serviceContext.put("PackageName", entityMetadata.packageName());
      serviceContext.put("EntityPackageName", entityPackageName);
      serviceContext.put("IdType", entityMetadata.id().type().regularName());
      serviceContext.put("Imports", genericImportsGenerator.generate(entityMetadata, em, hm));
      serviceContext.put(
          "className",
          entityMetadata.className().substring(0, 1).toLowerCase()
              + entityMetadata.className().substring(1));
      serviceContext.put(
          "Id",
          entityMetadata.id().name().substring(0, 1).toUpperCase()
              + entityMetadata.id().name().substring(1));
      serviceContext.put("Paginated", entityMetadata.isPaginated());
      serviceContext.put("hasProjection", entityMetadata.hasProjection());
      mustache.execute(writer, serviceContext);
    }
  }
}
