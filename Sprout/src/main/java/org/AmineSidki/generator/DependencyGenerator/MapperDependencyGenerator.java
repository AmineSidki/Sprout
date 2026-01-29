package org.AmineSidki.generator.DependencyGenerator;

import org.AmineSidki.model.EntityMetadata;
import org.AmineSidki.util.ParserUtil;

import java.util.HashSet;
import java.util.Set;

public class MapperDependencyGenerator {
    public HashSet<String> generate(EntityMetadata entity, Set<String> imports){
        HashSet<String> dependencies = new HashSet<>();
        String repoPackage = ParserUtil.getPackageName(entity.packageName()) + ".repository.";

        for (String s : imports) {
            if (s.startsWith(repoPackage)) {
                String className = s.substring(s.lastIndexOf(".") + 1);
                String variableName;
                if (className.toLowerCase().endsWith("repository")) {
                    variableName = className.toLowerCase().replace("repository", "Repo");
                } else {
                    variableName = Character.toLowerCase(className.charAt(0)) + className.substring(1);
                }

                dependencies.add(className + " " + variableName);
            }
        }
        return dependencies;
    }
}
