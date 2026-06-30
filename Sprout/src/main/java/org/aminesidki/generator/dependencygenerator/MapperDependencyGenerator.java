package org.aminesidki.generator.dependencygenerator;

import org.aminesidki.generator.SproutDependencyGenerator;
import org.aminesidki.model.EntityMetadata;
import org.aminesidki.util.ParserUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * Specific <code>SproutDependencyGenerator</code> implementation for mapper files
 */
public class MapperDependencyGenerator implements SproutDependencyGenerator {
    public HashSet<String> generate(EntityMetadata entity, Set<String> imports){
        HashSet<String> dependencies = new HashSet<>();
        String repoPackage = ParserUtil.getPackageName(entity.packageName()) + ".repository.";

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