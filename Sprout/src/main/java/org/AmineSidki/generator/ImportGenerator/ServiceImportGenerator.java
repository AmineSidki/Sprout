package org.AmineSidki.generator.ImportGenerator;

import org.AmineSidki.generator.SproutImportGenerator;
import org.AmineSidki.model.EntityMetadata;
import org.AmineSidki.model.HelperMetadata;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

public class ServiceImportGenerator implements SproutImportGenerator {
    @Override
    public HashSet<String> generate(EntityMetadata entityMetadata, Map<String, EntityMetadata> persistenceMap, Map<String, HelperMetadata> helperMap) {
        if((!entityMetadata.getId().getType().getFullQualifiedName().startsWith("java.lang.")
                || entityMetadata.getId().getType().getFullQualifiedName().substring(10).contains("."))) {
            return new HashSet<>(Collections.singleton(entityMetadata.getId().getType().getFullQualifiedName()));
        }
        return new HashSet<>();
    }
}
