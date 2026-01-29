package org.AmineSidki.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class EntityMetadata {
    final String packageName , className;
    final FieldMetadata id;
    final List<FieldMetadata> fields;
}
