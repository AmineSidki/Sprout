package org.aminesidki.generator.dependencygenerator;

import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.aminesidki.generator.SproutDependencyGenerator;
import org.aminesidki.model.EntityMetadata;
import org.aminesidki.util.ParserUtil;

/** Specific <code>SproutDependencyGenerator</code> implementation for mapper files */
@RequiredArgsConstructor
public class MapperDependencyGenerator implements SproutDependencyGenerator {
  private final String entityPackageName;

  public HashSet<String> generate(EntityMetadata entity, Set<String> imports) {
    HashSet<String> dependencies = new HashSet<>();
    String repoPackage =
        ParserUtil.getPackageName(entity.packageName(), "." + entityPackageName) + ".repository.";

    for (String s : imports) {
      if (s.startsWith(repoPackage)) {
        String className = s.substring(s.lastIndexOf(".") + 1);
        String variableName = Character.toLowerCase(className.charAt(0)) + className.substring(1);
        if (className.toLowerCase().endsWith("repository")) {
          variableName = variableName.replace("Repository", "Repo");
        }

        dependencies.add(className + " " + variableName);
      }
    }
    return dependencies;
  }
}
