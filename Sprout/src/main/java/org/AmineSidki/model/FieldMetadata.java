package org.AmineSidki.model;

import org.AmineSidki.enumeration.Association;

public record FieldMetadata(TypeMetadata type, String name, Association association) {
}
