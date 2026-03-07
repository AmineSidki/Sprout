package org.aminesidki.parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.aminesidki.exception.ParsingException;
import org.aminesidki.model.HelperMetadata;
import org.aminesidki.provider.JavaParserProvider;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

import static org.junit.Assert.*;

public class HelperParserTest {

    private HelperParser parser;
    private JavaParser javaParser;

    @Before
    public void setUp() throws Exception {
        URL resourceUrl = getClass().getClassLoader().getResource("test-entities");

        if (resourceUrl == null) {
            throw new IllegalStateException("Could not find test-entities on classpath");
        }

        File testSourceRoot = new File(resourceUrl.getFile());
        JavaParserProvider provider = new JavaParserProvider(testSourceRoot);
        javaParser = provider.provide();
        parser = new HelperParser();
    }

    private CompilationUnit parseFile(String fileName) throws FileNotFoundException {
        URL resourceUrl = getClass().getClassLoader()
                .getResource("test-entities/org/aminesidki/test/entity/" + fileName);

        if (resourceUrl == null) {
            throw new FileNotFoundException("Test entity not found on classpath: " + fileName);
        }

        return javaParser.parse(new File(resourceUrl.getFile()))
                .getResult()
                .orElseThrow(() -> new IllegalStateException("Failed to parse: " + fileName));
    }

    // -------------------------------------------------------------------------
    // Happy path — using real test entity files
    // -------------------------------------------------------------------------

    @Test
    public void parse_department_returnsCorrectClassName() throws Exception {
        HelperMetadata meta = parser.parse(parseFile("Department.java"), "Department.java");

        assertEquals("Department", meta.className());
    }

    @Test
    public void parse_department_returnsCorrectPackageName() throws Exception {
        HelperMetadata meta = parser.parse(parseFile("Department.java"), "Department.java");

        System.out.println(meta.packageName());
        assertTrue(meta.packageName().contains("org.aminesidki.test"));
    }

    @Test
    public void parse_doctor_returnsCorrectClassName() throws Exception {
        HelperMetadata meta = parser.parse(parseFile("Doctor.java"), "Doctor.java");

        assertEquals("Doctor", meta.className());
    }

    @Test
    public void parse_patient_returnsCorrectClassName() throws Exception {
        HelperMetadata meta = parser.parse(parseFile("Patient.java"), "Patient.java");

        assertEquals("Patient", meta.className());
    }

    @Test
    public void parse_medicalRecord_returnsCorrectClassName() throws Exception {
        HelperMetadata meta = parser.parse(parseFile("MedicalRecord.java"), "MedicalRecord.java");

        assertEquals("MedicalRecord", meta.className());
    }

    @Test
    public void parse_javaExtensionIsStrippedFromClassName() throws Exception {
        HelperMetadata meta = parser.parse(parseFile("Doctor.java"), "Doctor.java");

        assertFalse("Class name should not contain .java", meta.className().contains(".java"));
    }

    @Test
    public void parse_returnsNonNullMetadata() throws Exception {
        HelperMetadata meta = parser.parse(parseFile("Patient.java"), "Patient.java");

        assertNotNull(meta);
    }

    // -------------------------------------------------------------------------
    // Failure paths (inline source, no file needed)
    // -------------------------------------------------------------------------

    @Test(expected = ParsingException.class)
    public void parse_missingPackage_throwsParsingException() throws Exception {
        CompilationUnit cu = javaParser.parse(
                "public class NoPackageHelper {}"
        ).getResult().get();

        parser.parse(cu, "NoPackageHelper.java");
    }
}