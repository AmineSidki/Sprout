package org.aminesidki.model;

import org.aminesidki.enumeration.Association;

/**
 * @param type contains type metadata of a field
 * @param name contains declared field's name
 * @param association contains what type of association the field has
 */
public record FieldMetadata(TypeMetadata type, String name, Association association) {}
