package org.AmineSidki.generator;

import org.AmineSidki.model.EntityMetadata;
import org.AmineSidki.model.HelperMetadata;

import java.util.HashSet;
import java.util.Map;

public interface SproutSourceGenerator {
    HashSet<String> generate(EntityMetadata entityMetadata, Map<String, EntityMetadata> persistenceMap , Map<String, HelperMetadata> helperMap , String type);
}
