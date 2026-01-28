package org.AmineSidki.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.AmineSidki.enumeration.Association;

@Getter
@AllArgsConstructor
public class FieldMetadata {
    final TypeMetadata type;
    final String name;
    final Association association;
}
