package org.aminesidki.model;

import java.util.List;

/**
 * @param packageName package name of the entity, doesn't contain the last .entity part
 * @param className class name of the entity
 * @param id metadata for the @Id-annotated field in the entity
 * @param hasProjection defines whether a field has to have a lightDTO generated
 * @param isPaginated defines whether an entity has to be paginated or not
 * @param isIgnored defines whether an entity has to be ignored at generation time
 * @param fields list containing the metadata of the fields in the entity
 * @param lightFields list containing (in case entity has a lightDTO) the lightDTO fields
 */
public record EntityMetadata(String packageName,
                             String className,
                             FieldMetadata id,
                             boolean hasProjection,
                             boolean isPaginated,
                             boolean isIgnored,
                             boolean isCached,
                             List<FieldMetadata> fields,
                             List<FieldMetadata> lightFields) {
}
