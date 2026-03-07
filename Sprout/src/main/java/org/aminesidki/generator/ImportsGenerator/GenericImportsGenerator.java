package org.aminesidki.generator.ImportsGenerator;

import org.aminesidki.generator.SproutImportsGenerator;
import org.aminesidki.model.EntityMetadata;
import org.aminesidki.model.HelperMetadata;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

public class GenericImportsGenerator implements SproutImportsGenerator {

    @Override
    public HashSet<String> generate(EntityMetadata entityMetadata, Map<String, EntityMetadata> persistenceMap, Map<String, HelperMetadata> helperMap) {
        if((entityMetadata.id().type().isImportNeeded())) {
            return new HashSet<>(Collections.singleton(entityMetadata.id().type().fullQualifiedName()));
        }
        return new HashSet<>();
    }
}
