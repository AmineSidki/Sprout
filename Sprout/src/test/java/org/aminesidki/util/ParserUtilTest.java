package org.aminesidki.util;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserUtilTest {

    private CompilationUnit parse(String source) {
        return new JavaParser().parse(source).getResult().get();
    }

    // -------------------------------------------------------------------------
    // getPackageName
    // -------------------------------------------------------------------------

    @Test
    public void getPackageName_stripsTrailingEntityPackage() {
        assertEquals("com.example", ParserUtil.getPackageName("com.example.entity", ".entity"));
    }

    @Test
    public void getPackageName_noMatch_returnsInitialPackage() {
        assertEquals("com.example.entity", ParserUtil.getPackageName("com.example.entity", ".model"));
    }

    @Test
    public void getPackageName_matchOnlyInMiddle_returnsInitialPackage() {
        assertEquals("com.entity.shop", ParserUtil.getPackageName("com.entity.shop", ".entity"));
    }

    @Test
    public void getPackageName_withoutLeadingDot_doesNotMatchOnSegmentBoundary() {
        // callers must prefix the sub-package with "."; without it "subentity" would also match
        assertEquals("com.example.sub", ParserUtil.getPackageName("com.example.subentity", "entity"));
        assertEquals("com.example.subentity", ParserUtil.getPackageName("com.example.subentity", ".entity"));
    }

    @Test
    public void getPackageName_onlyStripsLastOccurrence() {
        assertEquals("com.entity", ParserUtil.getPackageName("com.entity.entity", ".entity"));
    }

    // -------------------------------------------------------------------------
    // hasAnnotation
    // -------------------------------------------------------------------------

    @Test
    public void hasAnnotation_presentOnClass_returnsTrue() {
        CompilationUnit cu = parse("package a; @Entity @SproutCached public class Foo {}");

        assertTrue(ParserUtil.hasAnnotation(cu, "Foo.java", "SproutCached"));
        assertTrue(ParserUtil.hasAnnotation(cu, "Foo.java", "Entity"));
    }

    @Test
    public void hasAnnotation_absentOnClass_returnsFalse() {
        CompilationUnit cu = parse("package a; @Entity public class Foo {}");

        assertFalse(ParserUtil.hasAnnotation(cu, "Foo.java", "SproutIgnore"));
    }

    @Test
    public void hasAnnotation_classNameDoesNotMatchFile_returnsFalse() {
        CompilationUnit cu = parse("package a; @Entity public class Foo {}");

        assertFalse(ParserUtil.hasAnnotation(cu, "Bar.java", "Entity"));
    }

    @Test
    public void hasAnnotation_annotationOnFieldOnly_returnsFalse() {
        CompilationUnit cu = parse("package a; public class Foo { @SproutIgnore private int x; }");

        assertFalse(ParserUtil.hasAnnotation(cu, "Foo.java", "SproutIgnore"));
    }

    // -------------------------------------------------------------------------
    // extractCollectionGenericType
    // -------------------------------------------------------------------------

    @Test
    public void extractCollectionGenericType_list_returnsElementType() {
        assertEquals("Doctor", ParserUtil.extractCollectionGenericType("List<Doctor>"));
    }

    @Test
    public void extractCollectionGenericType_nestedGeneric_returnsOuterContent() {
        assertEquals("Map<String, Long>", ParserUtil.extractCollectionGenericType("List<Map<String, Long>>"));
    }
}
