package org.aminesidki.parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.aminesidki.exception.ParsingException;
import org.aminesidki.model.EntityMetadata;
import org.aminesidki.provider.JavaParserProvider;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

import static org.junit.Assert.*;

public class EntityParserTest {

    private EntityParser parser;
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
        parser = new EntityParser();
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
    // Department.java — @SproutLightDTO, @SproutLDF fields, Long id
    // -------------------------------------------------------------------------

    @Test
    public void parse_department_returnsCorrectClassName() throws Exception {
        EntityMetadata meta = parser.parse(parseFile("Department.java"), "Department.java");

        assertEquals("Department", meta.className());
    }

    @Test
    public void parse_department_returnsCorrectPackageName() throws Exception {
        EntityMetadata meta = parser.parse(parseFile("Department.java"), "Department.java");

        assertTrue(meta.packageName().contains("org.aminesidki.test"));
    }

    @Test
    public void parse_department_hasLightDTOIsTrue() throws Exception {
        EntityMetadata meta = parser.parse(parseFile("Department.java"), "Department.java");

        assertTrue(meta.hasLightDTO());
    }

    @Test
    public void parse_department_idFieldNameIsId() throws Exception {
        EntityMetadata meta = parser.parse(parseFile("Department.java"), "Department.java");

        assertEquals("id", meta.id().name());
    }

    @Test
    public void parse_department_idTypeIsLong() throws Exception {
        EntityMetadata meta = parser.parse(parseFile("Department.java"), "Department.java");

        assertEquals("Long", meta.id().type().regularName());
    }

    @Test
    public void parse_department_sproutLDFFieldsExcludedFromLightFields() throws Exception {
        EntityMetadata meta = parser.parse(parseFile("Department.java"), "Department.java");

        boolean buildingInLight = meta.lightFields().stream()
                .anyMatch(f -> f.name().equals("building"));
        boolean dateInLight = meta.lightFields().stream()
                .anyMatch(f -> f.name().equals("establishedDate"));

        assertFalse("@SproutLDF field 'building' should be excluded from lightFields", buildingInLight);
        assertFalse("@SproutLDF field 'establishedDate' should be excluded from lightFields", dateInLight);
    }

    @Test
    public void parse_department_nonLDFFieldPresentInLightFields() throws Exception {
        EntityMetadata meta = parser.parse(parseFile("Department.java"), "Department.java");

        boolean nameInLight = meta.lightFields().stream()
                .anyMatch(f -> f.name().equals("name"));

        assertTrue("Non-LDF field 'name' should be present in lightFields", nameInLight);
    }

    @Test
    public void parse_department_allFieldsContainsBuildingAndDate() throws Exception {
        EntityMetadata meta = parser.parse(parseFile("Department.java"), "Department.java");

        boolean buildingInFields = meta.fields().stream()
                .anyMatch(f -> f.name().equals("building"));
        boolean dateInFields = meta.fields().stream()
                .anyMatch(f -> f.name().equals("establishedDate"));

        assertTrue("'building' should still appear in full fields list", buildingInFields);
        assertTrue("'establishedDate' should still appear in full fields list", dateInFields);
    }

    // -------------------------------------------------------------------------
    // Doctor.java — no special Sprout annotations, Long id, ManyToOne relation
    // -------------------------------------------------------------------------

    @Test
    public void parse_doctor_returnsCorrectClassName() throws Exception {
        EntityMetadata meta = parser.parse(parseFile("Doctor.java"), "Doctor.java");

        assertEquals("Doctor", meta.className());
    }

    @Test
    public void parse_doctor_hasLightDTOIsFalse() throws Exception {
        EntityMetadata meta = parser.parse(parseFile("Doctor.java"), "Doctor.java");

        assertFalse(meta.hasLightDTO());
    }

    @Test
    public void parse_doctor_isPaginatedIsFalse() throws Exception {
        EntityMetadata meta = parser.parse(parseFile("Doctor.java"), "Doctor.java");

        assertFalse(meta.isPaginated());
    }

    @Test
    public void parse_doctor_isIgnoredIsFalse() throws Exception {
        EntityMetadata meta = parser.parse(parseFile("Doctor.java"), "Doctor.java");

        assertFalse(meta.isIgnored());
    }

    @Test
    public void parse_doctor_idFieldIsCorrect() throws Exception {
        EntityMetadata meta = parser.parse(parseFile("Doctor.java"), "Doctor.java");

        assertEquals("id", meta.id().name());
        assertEquals("Long", meta.id().type().regularName());
    }

    @Test
    public void parse_doctor_fieldsArePopulated() throws Exception {
        EntityMetadata meta = parser.parse(parseFile("Doctor.java"), "Doctor.java");

        assertNotNull(meta.fields());
        assertFalse(meta.fields().isEmpty());
    }

    @Test
    public void parse_doctor_lightFieldsEqualFieldsWhenNoLDF() throws Exception {
        EntityMetadata meta = parser.parse(parseFile("Doctor.java"), "Doctor.java");

        // No @SproutLFD annotations, so lightFields should match fields exactly
        assertEquals(meta.fields().size(), meta.lightFields().size());
    }

    // -------------------------------------------------------------------------
    // MedicalRecord.java — UUID id (non-Long), OneToOne relation
    // -------------------------------------------------------------------------

    @Test
    public void parse_medicalRecord_returnsCorrectClassName() throws Exception {
        EntityMetadata meta = parser.parse(parseFile("MedicalRecord.java"), "MedicalRecord.java");

        assertEquals("MedicalRecord", meta.className());
    }

    @Test
    public void parse_medicalRecord_idFieldNameIsRecordUuid() throws Exception {
        EntityMetadata meta = parser.parse(parseFile("MedicalRecord.java"), "MedicalRecord.java");

        assertEquals("recordUuid", meta.id().name());
    }

    @Test
    public void parse_medicalRecord_idTypeIsUUID() throws Exception {
        EntityMetadata meta = parser.parse(parseFile("MedicalRecord.java"), "MedicalRecord.java");

        assertEquals("UUID", meta.id().type().regularName());
    }

    @Test
    public void parse_medicalRecord_fieldsContainDiagnosisAndTreatmentPlan() throws Exception {
        EntityMetadata meta = parser.parse(parseFile("MedicalRecord.java"), "MedicalRecord.java");

        boolean hasDiagnosis = meta.fields().stream()
                .anyMatch(f -> f.name().equals("diagnosis"));
        boolean hasTreatmentPlan = meta.fields().stream()
                .anyMatch(f -> f.name().equals("treatmentPlan"));

        assertTrue(hasDiagnosis);
        assertTrue(hasTreatmentPlan);
    }

    // -------------------------------------------------------------------------
    // Patient.java — ManyToMany relation, Long id
    // -------------------------------------------------------------------------

    @Test
    public void parse_patient_returnsCorrectClassName() throws Exception {
        EntityMetadata meta = parser.parse(parseFile("Patient.java"), "Patient.java");

        assertEquals("Patient", meta.className());
    }

    @Test
    public void parse_patient_idFieldIsCorrect() throws Exception {
        EntityMetadata meta = parser.parse(parseFile("Patient.java"), "Patient.java");

        assertEquals("id", meta.id().name());
        assertEquals("Long", meta.id().type().regularName());
    }

    @Test
    public void parse_patient_fieldsContainNameAndBloodType() throws Exception {
        EntityMetadata meta = parser.parse(parseFile("Patient.java"), "Patient.java");

        boolean hasName = meta.fields().stream().anyMatch(f -> f.name().equals("name"));
        boolean hasBloodType = meta.fields().stream().anyMatch(f -> f.name().equals("bloodType"));

        assertTrue(hasName);
        assertTrue(hasBloodType);
    }

    // -------------------------------------------------------------------------
    // Failure paths (inline source, no file needed)
    // -------------------------------------------------------------------------

    @Test(expected = ParsingException.class)
    public void parse_missingPackage_throwsParsingException() throws Exception {
        CompilationUnit cu = javaParser.parse(
                "@Entity public class NoPackage { @Id private Long id; }"
        ).getResult().get();

        parser.parse(cu, "NoPackage.java");
    }

    @Test(expected = ParsingException.class)
    public void parse_missingIdAnnotation_throwsParsingException() throws Exception {
        CompilationUnit cu = javaParser.parse(
                "package com.example;\n" +
                        "@Entity public class NoId { private Long id; }"
        ).getResult().get();

        parser.parse(cu, "NoId.java");
    }
}