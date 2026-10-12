package org.aminesidki.generator;

import java.util.HashSet;
import java.util.Map;
import org.aminesidki.model.EntityMetadata;
import org.aminesidki.model.HelperMetadata;

/** Generates imports for given entity */
public interface SproutImportsGenerator {
  HashSet<String> generate(
      EntityMetadata entityMetadata,
      Map<String, EntityMetadata> persistenceMap,
      Map<String, HelperMetadata> helperMap);
}
