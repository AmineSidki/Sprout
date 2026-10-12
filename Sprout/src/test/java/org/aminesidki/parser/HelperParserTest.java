package org.aminesidki.parser;

import static org.junit.Assert.*;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import org.aminesidki.exception.ParsingException;
import org.aminesidki.model.HelperMetadata;
import org.aminesidki.provider.JavaParserProvider;
import org.junit.Before;
import org.junit.Test;

public class HelperParserTest {

  private static final String ENTITY_PACKAGE = "entity";

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
    URL resourceUrl =
        getClass()
            .getClassLoader()
            .getResource("test-entities/org/aminesidki/test/entity/" + fileName);

    if (resourceUrl == null) {
      throw new FileNotFoundException("Test entity not found on classpath: " + fileName);
    }

    return javaParser
        .parse(new File(resourceUrl.getFile()))
        .getResult()
        .orElseThrow(() -> new IllegalStateException("Failed to parse: " + fileName));
  }

  private String resolvedPackage(String declaredPackage, String entityPackageName)
      throws Exception {
    CompilationUnit cu =
        javaParser
            .parse("package " + declaredPackage + ";\npublic class SampleHelper {}")
            .getResult()
            .get();

    return parser.parse(cu, "SampleHelper.java", entityPackageName).packageName();
  }

  // -------------------------------------------------------------------------
  // Happy path — using real test entity files
  // -------------------------------------------------------------------------

  @Test
  public void parse_department_returnsCorrectClassName() throws Exception {
    HelperMetadata meta =
        parser.parse(parseFile("Department.java"), "Department.java", ENTITY_PACKAGE);

    assertEquals("Department", meta.className());
  }

  @Test
  public void parse_department_returnsPackageWithoutEntitySubpackage() throws Exception {
    HelperMetadata meta =
        parser.parse(parseFile("Department.java"), "Department.java", ENTITY_PACKAGE);

    assertEquals("org.aminesidki.test", meta.packageName());
  }

  @Test
  public void parse_doctor_returnsCorrectClassName() throws Exception {
    HelperMetadata meta = parser.parse(parseFile("Doctor.java"), "Doctor.java", ENTITY_PACKAGE);

    assertEquals("Doctor", meta.className());
  }

  @Test
  public void parse_patient_returnsCorrectClassName() throws Exception {
    HelperMetadata meta = parser.parse(parseFile("Patient.java"), "Patient.java", ENTITY_PACKAGE);

    assertEquals("Patient", meta.className());
  }

  @Test
  public void parse_medicalRecord_returnsCorrectClassName() throws Exception {
    HelperMetadata meta =
        parser.parse(parseFile("MedicalRecord.java"), "MedicalRecord.java", ENTITY_PACKAGE);

    assertEquals("MedicalRecord", meta.className());
  }

  @Test
  public void parse_javaExtensionIsStrippedFromClassName() throws Exception {
    HelperMetadata meta = parser.parse(parseFile("Doctor.java"), "Doctor.java", ENTITY_PACKAGE);

    assertFalse("Class name should not contain .java", meta.className().contains(".java"));
  }

  @Test
  public void parse_returnsNonNullMetadata() throws Exception {
    HelperMetadata meta = parser.parse(parseFile("Patient.java"), "Patient.java", ENTITY_PACKAGE);

    assertNotNull(meta);
  }

  // -------------------------------------------------------------------------
  // Entity package resolution
  // -------------------------------------------------------------------------

  @Test
  public void packageResolution_entity_stripsSubpackage() throws Exception {
    assertEquals("com.example", resolvedPackage("com.example.entity", "entity"));
  }

  @Test
  public void packageResolution_entities_stripsSubpackage() throws Exception {
    assertEquals("com.example", resolvedPackage("com.example.entities", "entities"));
  }

  @Test
  public void packageResolution_domain_stripsSubpackage() throws Exception {
    assertEquals("com.example", resolvedPackage("com.example.domain", "domain"));
  }

  @Test
  public void packageResolution_model_stripsSubpackage() throws Exception {
    assertEquals("com.example", resolvedPackage("com.example.model", "model"));
  }

  @Test
  public void packageResolution_models_stripsSubpackage() throws Exception {
    assertEquals("com.example", resolvedPackage("com.example.models", "models"));
  }

  @Test
  public void packageResolution_nonMatchingSubpackageName_leavesPackageUntouched()
      throws Exception {
    assertEquals("com.example.entity", resolvedPackage("com.example.entity", "model"));
  }

  @Test
  public void packageResolution_suffixWithoutDotBoundary_leavesPackageUntouched() throws Exception {
    assertEquals("com.example.subentity", resolvedPackage("com.example.subentity", "entity"));
  }

  @Test
  public void packageResolution_entityNameOnlyInMiddle_leavesPackageUntouched() throws Exception {
    assertEquals("com.entity.shop", resolvedPackage("com.entity.shop", "entity"));
  }

  // -------------------------------------------------------------------------
  // Failure paths (inline source, no file needed)
  // -------------------------------------------------------------------------

  @Test(expected = ParsingException.class)
  public void parse_missingPackage_throwsParsingException() throws Exception {
    CompilationUnit cu = javaParser.parse("public class NoPackageHelper {}").getResult().get();

    parser.parse(cu, "NoPackageHelper.java", ENTITY_PACKAGE);
  }
}
