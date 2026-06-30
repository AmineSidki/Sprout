package org.aminesidki.generator.filegenerator;

import com.github.mustachejava.Mustache;
import lombok.RequiredArgsConstructor;
import org.aminesidki.enumeration.Association;
import org.aminesidki.exception.FileSystemException;
import org.aminesidki.exception.ParsingException;
import org.aminesidki.generator.dependencygenerator.MapperDependencyGenerator;
import org.aminesidki.generator.importsgenerator.MapperImportsGenerator;
import org.aminesidki.generator.SproutFileGenerator;
import org.aminesidki.model.EntityMetadata;
import org.aminesidki.model.FieldMetadata;
import org.aminesidki.model.HelperMetadata;
import org.aminesidki.model.TypeMetadata;
import org.aminesidki.util.FileCreator;
import org.aminesidki.util.Ledger;
import org.aminesidki.util.ParserUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Specific implementation for mapper classes generation of the <code>SproutFileGenerator</code>
 */
@RequiredArgsConstructor
public class MapperGenerator implements SproutFileGenerator {
    private final MapperImportsGenerator mapperImportsGenerator;
    private final MapperDependencyGenerator mapperDependencyGen;
    private final Map<String, EntityMetadata> persistenceMap;
    private final Map<String, HelperMetadata> helperMap;

    public record DependencyView(String type, String name) {};
    public record AssociationView(FieldMetadata field , FieldMetadata id , boolean multiple , String className) {};

    public void generate(EntityMetadata entityMetadata, Mustache mustache, String defDir, FileCreator fileCreator, Ledger ledger) throws IOException, FileSystemException {
        File mapperFile = fileCreator.createFile(entityMetadata.className(), "Mapper", defDir);

        List<FieldMetadata> fields = entityMetadata.fields().stream()
                .filter(f -> f.association().equals(Association.DEFAULT))
                .map(f -> new FieldMetadata(f.type() ,
                        f.name().substring(0,1).toUpperCase() + f.name().substring(1) ,
                        f.association()))
                .collect(Collectors.toList());


        List<FieldMetadata> associationFields = entityMetadata
                .fields()
                .stream()
                .filter(f -> !f.association().equals(Association.DEFAULT))
                .toList();

        final Set<String> imports = mapperImportsGenerator.generate(entityMetadata ,persistenceMap , helperMap);

        List<AssociationView> associations = associationFields.stream().map(f -> {
            FieldMetadata fieldMetadata = new FieldMetadata(f.type() ,
                    f.name() ,
                    f.association());

            f = new FieldMetadata(
                    new TypeMetadata((f.association() == Association.ONE_TO_MANY || f.association() == Association.MANY_TO_MANY)?
                            ParserUtil.extractCollectionGenericType(f.type().regularName().toLowerCase()) : f.type().regularName().toLowerCase(),
                            f.type().fullQualifiedName()),
                    f.name().substring(0,1).toUpperCase() + f.name().substring(1) ,
                    f.association());

            String cleanField = fieldMetadata.type().regularName();
            boolean multiple = false;

            if(f.association() == Association.ONE_TO_MANY || f.association() == Association.MANY_TO_MANY){
                cleanField = ParserUtil.extractCollectionGenericType(cleanField);
                multiple = true;
            }

            if(persistenceMap.get(cleanField) != null){
                FieldMetadata idMetadata = persistenceMap.get(cleanField).id();
                idMetadata = new FieldMetadata(idMetadata.type(),
                        idMetadata.name().substring(0,1).toUpperCase() + idMetadata.name().substring(1) ,
                        idMetadata.association());
                return new AssociationView(f , idMetadata , multiple , cleanField);
            }
            throw new ParsingException("Error whilst parsing association " + cleanField);
        }).collect(Collectors.toList());

        List<DependencyView> dependencies = new ArrayList<>(mapperDependencyGen.generate(entityMetadata, imports)
                .stream()
                .map(s -> {
                    String[] parts = s.split(" ");
                    return new DependencyView(parts[0], parts[1]);
                })
                .toList());

        DependencyView lastDependency = !dependencies.isEmpty() ? dependencies.remove(dependencies.size() - 1) : null;

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(mapperFile))) {
            HashMap<String, Object> mapperContext = new HashMap<>();

            mapperContext.put("ClassName", entityMetadata.className());
            mapperContext.put("PackageName", entityMetadata.packageName());
            mapperContext.put("IdType", entityMetadata.id().type().regularName());
            mapperContext.put("Fields" , fields);
            mapperContext.put("Imports" , imports);
            mapperContext.put("hasDependencies", !dependencies.isEmpty());
            mapperContext.put("Associations" , associations);
            mapperContext.put("Dependencies" , dependencies);
            mapperContext.put("LastDependency", lastDependency);
            mapperContext.put("hasProjection" , entityMetadata.hasProjection());

            mustache.execute(writer, mapperContext);
        }
    }
}