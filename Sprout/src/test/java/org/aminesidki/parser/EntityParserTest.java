package org.aminesidki.parser;

import static org.junit.Assert.*;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import org.aminesidki.enumeration.Association;
import org.aminesidki.exception.ParsingException;
import org.aminesidki.model.EntityMetadata;
import org.aminesidki.model.FieldMetadata;
import org.aminesidki.provider.JavaParserProvider;
import org.junit.Before;
import org.junit.Test;

public class EntityParserTest {

  /**
   * Name of the entity sub-package, as discovered by MainRunnable (entity / entities / domain /
   * model / models).
   */
  private static final String ENTITY_PACKAGE = "entity";

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

  private CompilationUnit parseInline(String source) {
    return javaParser
        .parse(source)
        .getResult()
        .orElseThrow(() -> new IllegalStateException("Failed to parse inline source"));
  }

  private EntityMetadata parseEntity(String fileName) throws Exception {
    return parser.parse(parseFile(fileName), fileName, ENTITY_PACKAGE);
  }

  private FieldMetadata field(EntityMetadata meta, String name) {
    return meta.fields().stream()
        .filter(f -> f.name().equals(name))
        .findFirst()
        .orElseThrow(() -> new AssertionError("Field not found: " + name));
  }

  // -------------------------------------------------------------------------
  // Department.java — @SproutProjection, @SproutLargeDataField fields, Long id
  // -------------------------------------------------------------------------

  @Test
  public void parse_department_returnsCorrectClassName() throws Exception {
    assertEquals("Department", parseEntity("Department.java").className());
  }

  @Test
  public void parse_department_returnsPackageWithoutEntitySubpackage() throws Exception {
    EntityMetadata meta = parseEntity("Department.java");

    assertEquals("org.aminesidki.test", meta.packageName());
  }

  @Test
  public void parse_department_hasSproutProjectionIsTrue() throws Exception {
    assertTrue(parseEntity("Department.java").hasProjection());
  }

  @Test
  public void parse_department_idFieldNameIsId() throws Exception {
    assertEquals("id", parseEntity("Department.java").id().name());
  }

  @Test
  public void parse_department_idTypeIsLong() throws Exception {
    assertEquals("Long", parseEntity("Department.java").id().type().regularName());
  }

  @Test
  public void parse_department_sproutLDFFieldsExcludedFromLightFields() throws Exception {
    EntityMetadata meta = parseEntity("Department.java");

    boolean buildingInLight =
        meta.lightFields().stream().anyMatch(f -> f.name().equals("building"));
    boolean dateInLight =
        meta.lightFields().stream().anyMatch(f -> f.name().equals("establishedDate"));

    assertFalse(
        "@SproutLargeDataField field 'building' should be excluded from lightFields",
        buildingInLight);
    assertFalse(
        "@SproutLargeDataField field 'establishedDate' should be excluded from lightFields",
        dateInLight);
  }

  @Test
  public void parse_department_nonLDFFieldPresentInLightFields() throws Exception {
    EntityMetadata meta = parseEntity("Department.java");

    boolean nameInLight = meta.lightFields().stream().anyMatch(f -> f.name().equals("name"));

    assertTrue("Non-LDF field 'name' should be present in lightFields", nameInLight);
  }

  @Test
  public void parse_department_allFieldsContainsBuildingAndDate() throws Exception {
    EntityMetadata meta = parseEntity("Department.java");

    boolean buildingInFields = meta.fields().stream().anyMatch(f -> f.name().equals("building"));
    boolean dateInFields = meta.fields().stream().anyMatch(f -> f.name().equals("establishedDate"));

    assertTrue("'building' should still appear in full fields list", buildingInFields);
    assertTrue("'establishedDate' should still appear in full fields list", dateInFields);
  }

  @Test
  public void parse_department_plainFieldsHaveDefaultAssociation() throws Exception {
    assertEquals(Association.DEFAULT, field(parseEntity("Department.java"), "name").association());
  }

  // -------------------------------------------------------------------------
  // Doctor.java — no special Sprout annotations, Long id, ManyToOne relation
  // -------------------------------------------------------------------------

  @Test
  public void parse_doctor_returnsCorrectClassName() throws Exception {
    assertEquals("Doctor", parseEntity("Doctor.java").className());
  }

  @Test
  public void parse_doctor_returnsPackageWithoutEntitySubpackage() throws Exception {
    assertEquals("org.aminesidki.test", parseEntity("Doctor.java").packageName());
  }

  @Test
  public void parse_doctor_hasLightDTOIsFalse() throws Exception {
    assertFalse(parseEntity("Doctor.java").hasProjection());
  }

  @Test
  public void parse_doctor_isPaginatedIsFalse() throws Exception {
    assertFalse(parseEntity("Doctor.java").isPaginated());
  }

  @Test
  public void parse_doctor_isIgnoredIsFalse() throws Exception {
    assertFalse(parseEntity("Doctor.java").isIgnored());
  }

  @Test
  public void parse_doctor_isCachedIsFalse() throws Exception {
    assertFalse(parseEntity("Doctor.java").isCached());
  }

  @Test
  public void parse_doctor_idFieldIsCorrect() throws Exception {
    EntityMetadata meta = parseEntity("Doctor.java");

    assertEquals("id", meta.id().name());
    assertEquals("Long", meta.id().type().regularName());
  }

  @Test
  public void parse_doctor_fieldsArePopulated() throws Exception {
    EntityMetadata meta = parseEntity("Doctor.java");

    assertNotNull(meta.fields());
    assertFalse(meta.fields().isEmpty());
  }

  @Test
  public void parse_doctor_lightFieldsEqualFieldsWhenNoLDF() throws Exception {
    EntityMetadata meta = parseEntity("Doctor.java");

    // No @SproutLargeDataField annotations, so lightFields should match fields exactly
    assertEquals(meta.fields().size(), meta.lightFields().size());
  }

  @Test
  public void parse_doctor_departmentIsManyToOne() throws Exception {
    FieldMetadata department = field(parseEntity("Doctor.java"), "department");

    assertEquals(Association.MANY_TO_ONE, department.association());
    assertEquals("Department", department.type().regularName());
  }

  // -------------------------------------------------------------------------
  // MedicalRecord.java — UUID id (non-Long), OneToOne relation
  // -------------------------------------------------------------------------

  @Test
  public void parse_medicalRecord_returnsCorrectClassName() throws Exception {
    assertEquals("MedicalRecord", parseEntity("MedicalRecord.java").className());
  }

  @Test
  public void parse_medicalRecord_idFieldNameIsRecordUuid() throws Exception {
    assertEquals("recordUuid", parseEntity("MedicalRecord.java").id().name());
  }

  @Test
  public void parse_medicalRecord_idTypeIsUUID() throws Exception {
    assertEquals("UUID", parseEntity("MedicalRecord.java").id().type().regularName());
  }

  @Test
  public void parse_medicalRecord_fieldsContainDiagnosisAndTreatmentPlan() throws Exception {
    EntityMetadata meta = parseEntity("MedicalRecord.java");

    boolean hasDiagnosis = meta.fields().stream().anyMatch(f -> f.name().equals("diagnosis"));
    boolean hasTreatmentPlan =
        meta.fields().stream().anyMatch(f -> f.name().equals("treatmentPlan"));

    assertTrue(hasDiagnosis);
    assertTrue(hasTreatmentPlan);
  }

  @Test
  public void parse_medicalRecord_patientIsOneToOne() throws Exception {
    assertEquals(
        Association.ONE_TO_ONE, field(parseEntity("MedicalRecord.java"), "patient").association());
  }

  // -------------------------------------------------------------------------
  // Patient.java — ManyToMany relation, Long id
  // -------------------------------------------------------------------------

  @Test
  public void parse_patient_returnsCorrectClassName() throws Exception {
    assertEquals("Patient", parseEntity("Patient.java").className());
  }

  @Test
  public void parse_patient_idFieldIsCorrect() throws Exception {
    EntityMetadata meta = parseEntity("Patient.java");

    assertEquals("id", meta.id().name());
    assertEquals("Long", meta.id().type().regularName());
  }

  @Test
  public void parse_patient_fieldsContainNameAndBloodType() throws Exception {
    EntityMetadata meta = parseEntity("Patient.java");

    boolean hasName = meta.fields().stream().anyMatch(f -> f.name().equals("name"));
    boolean hasBloodType = meta.fields().stream().anyMatch(f -> f.name().equals("bloodType"));

    assertTrue(hasName);
    assertTrue(hasBloodType);
  }

  @Test
  public void parse_patient_primaryDoctorsIsManyToMany() throws Exception {
    FieldMetadata doctors = field(parseEntity("Patient.java"), "primaryDoctors");

    assertEquals(Association.MANY_TO_MANY, doctors.association());
    assertEquals("List<Doctor>", doctors.type().regularName());
  }

  // -------------------------------------------------------------------------
  // Entity package resolution — the declared package minus the entity sub-package
  // -------------------------------------------------------------------------

  private String resolvedPackage(String declaredPackage, String entityPackageName)
      throws Exception {
    CompilationUnit cu =
        parseInline(
            "package "
                + declaredPackage
                + ";\n"
                + "@Entity public class Sample { @Id private Long id; }");

    return parser.parse(cu, "Sample.java", entityPackageName).packageName();
  }

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
  public void packageResolution_deeplyNestedBasePackage_onlyStripsTrailingSubpackage()
      throws Exception {
    assertEquals(
        "com.example.shop.core", resolvedPackage("com.example.shop.core.entity", "entity"));
  }

  @Test
  public void packageResolution_entityNameAlsoInMiddle_onlyStripsTrailingOccurrence()
      throws Exception {
    assertEquals("com.entity.shop", resolvedPackage("com.entity.shop.entity", "entity"));
  }

  @Test
  public void packageResolution_nonMatchingSubpackageName_leavesPackageUntouched()
      throws Exception {
    assertEquals("com.example.entity", resolvedPackage("com.example.entity", "model"));
  }

  @Test
  public void packageResolution_suffixWithoutDotBoundary_leavesPackageUntouched() throws Exception {
    // "subentity" ends with "entity" but is not the "entity" sub-package
    assertEquals("com.example.subentity", resolvedPackage("com.example.subentity", "entity"));
  }

  @Test
  public void packageResolution_entityNameOnlyInMiddle_leavesPackageUntouched() throws Exception {
    assertEquals("com.entity.shop", resolvedPackage("com.entity.shop", "entity"));
  }

  // -------------------------------------------------------------------------
  // Class-level Sprout annotations (inline source)
  // -------------------------------------------------------------------------

  @Test
  public void parse_sproutPaginated_setsIsPaginated() throws Exception {
    CompilationUnit cu =
        parseInline(
            "package com.example.entity;\n"
                + "@Entity @SproutPaginated public class Paged { @Id private Long id; }");

    EntityMetadata meta = parser.parse(cu, "Paged.java", ENTITY_PACKAGE);

    assertTrue(meta.isPaginated());
    assertFalse(meta.isIgnored());
    assertFalse(meta.isCached());
    assertFalse(meta.hasProjection());
  }

  @Test
  public void parse_sproutIgnore_setsIsIgnored() throws Exception {
    CompilationUnit cu =
        parseInline(
            "package com.example.entity;\n"
                + "@Entity @SproutIgnore public class Skipped { @Id private Long id; }");

    assertTrue(parser.parse(cu, "Skipped.java", ENTITY_PACKAGE).isIgnored());
  }

  @Test
  public void parse_sproutCached_setsIsCached() throws Exception {
    CompilationUnit cu =
        parseInline(
            "package com.example.entity;\n"
                + "@Entity @SproutCached public class Remembered { @Id private Long id; }");

    assertTrue(parser.parse(cu, "Remembered.java", ENTITY_PACKAGE).isCached());
  }

  // -------------------------------------------------------------------------
  // Id handling (inline source)
  // -------------------------------------------------------------------------

  @Test
  public void parse_embeddedId_isUsedWhenNoIdAnnotationPresent() throws Exception {
    CompilationUnit cu =
        parseInline(
            "package com.example.entity;\n"
                + "@Entity public class Order { @EmbeddedId private OrderKey key; private String label; }");

    EntityMetadata meta = parser.parse(cu, "Order.java", ENTITY_PACKAGE);

    assertEquals("key", meta.id().name());
    assertEquals("OrderKey", meta.id().type().regularName());
  }

  @Test
  public void parse_multipleVariablesInOneDeclaration_producesOneFieldEach() throws Exception {
    CompilationUnit cu =
        parseInline(
            "package com.example.entity;\n"
                + "@Entity public class Person { @Id private Long id; private String firstName, lastName; }");

    EntityMetadata meta = parser.parse(cu, "Person.java", ENTITY_PACKAGE);

    assertEquals("firstName", field(meta, "firstName").name());
    assertEquals("lastName", field(meta, "lastName").name());
  }

  // -------------------------------------------------------------------------
  // Failure paths (inline source, no file needed)
  // -------------------------------------------------------------------------

  @Test(expected = ParsingException.class)
  public void parse_missingPackage_throwsParsingException() throws Exception {
    CompilationUnit cu = parseInline("@Entity public class NoPackage { @Id private Long id; }");

    parser.parse(cu, "NoPackage.java", ENTITY_PACKAGE);
  }

  @Test(expected = ParsingException.class)
  public void parse_missingIdAnnotation_throwsParsingException() throws Exception {
    CompilationUnit cu =
        parseInline("package com.example;\n" + "@Entity public class NoId { private Long id; }");

    parser.parse(cu, "NoId.java", ENTITY_PACKAGE);
  }
}
