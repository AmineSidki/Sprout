package org.AmineSidki.model;

import java.util.List;

public record EntityMetadata(String packageName, String className, FieldMetadata id, List<FieldMetadata> fields) {
}
