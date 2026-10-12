package org.aminesidki.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class TypeMetadataTest {

    @Test
    public void isImportNeeded_emptyQualifiedName_returnsFalse() {
        assertFalse(new TypeMetadata("Foo", "").isImportNeeded());
    }

    @Test
    public void isImportNeeded_javaLangClass_returnsFalse() {
        assertFalse(new TypeMetadata("String", "java.lang.String").isImportNeeded());
    }

    @Test
    public void isImportNeeded_javaLangSubpackage_returnsTrue() {
        assertTrue(new TypeMetadata("Retention", "java.lang.annotation.Retention").isImportNeeded());
    }

    @Test
    public void isImportNeeded_javaUtilClass_returnsTrue() {
        assertTrue(new TypeMetadata("UUID", "java.util.UUID").isImportNeeded());
    }

    @Test
    public void isImportNeeded_projectClass_returnsTrue() {
        assertTrue(new TypeMetadata("Doctor", "org.aminesidki.test.entity.Doctor").isImportNeeded());
    }
}
