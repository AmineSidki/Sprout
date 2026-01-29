package org.AmineSidki.generator.FileGenerator;

import com.github.mustachejava.Mustache;
import lombok.RequiredArgsConstructor;
import org.AmineSidki.enumeration.Association;
import org.AmineSidki.exception.FileSystemException;
import org.AmineSidki.exception.ParsingException;
import org.AmineSidki.generator.DependencyGenerator.MapperDependencyGenerator;
import org.AmineSidki.generator.SproutFileGenerator;
import org.AmineSidki.generator.SproutImportGenerator;
import org.AmineSidki.model.EntityMetadata;
import org.AmineSidki.model.FieldMetadata;
import org.AmineSidki.model.HelperMetadata;
import org.AmineSidki.model.TypeMetadata;
import org.AmineSidki.util.ParserUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MapperGenerator implements SproutFileGenerator {
    private final Map<String, EntityMetadata> persistenceMap;
    private final Map<String, HelperMetadata> helperMap;
    private final MapperDependencyGenerator mapperDependencyGen;

    public record DependencyView(String type, String name) {};
    public record AssociationView(FieldMetadata field , FieldMetadata id , boolean multiple , String className) {};

    public void generate(SproutImportGenerator importsGenerator, EntityMetadata entityMetadata, Mustache mustache , String defDir) throws IOException , FileSystemException{
        //Create the mapper package if it doesn't exist yet
        File mapperPackage = new File(defDir + "/mapper");
        if(!mapperPackage.exists() && !mapperPackage.mkdir()){
            throw new FileSystemException("");
        }

        File mapperFile = new File(defDir + "/mapper/" + entityMetadata.className() + "Mapper.java");

        if(!mapperFile.exists() && !mapperFile.createNewFile()){
            throw new FileSystemException("");
        }

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

        List<AssociationView> associations = associationFields.stream().map(f -> {
           FieldMetadata fieldMetadata = new FieldMetadata(f.type() ,
                   f.name() ,
                   f.association());

            f = new FieldMetadata(
                        new TypeMetadata((f.association() == Association.ONE_TO_MANY || f.association() == Association.MANY_TO_MANY)?
                                ParserUtil.extractCollectionGenericType(f.type().getRegularName().toLowerCase()) : f.type().getRegularName().toLowerCase(),
                                f.type().getFullQualifiedName()),
                        f.name().substring(0,1).toUpperCase() + f.name().substring(1) ,
                        f.association());

            String cleanField = fieldMetadata.type().getRegularName();
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
            throw new ParsingException("");
        }).collect(Collectors.toList());

        Set<String> imports = importsGenerator.generate(entityMetadata ,persistenceMap , helperMap);
        List<DependencyView> dependencies = mapperDependencyGen.generate(entityMetadata, imports)
                .stream()
                .map(s -> {
                    String[] parts = s.split(" ");
                    return new DependencyView(parts[0], parts[1]);
                })
                .toList();

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(mapperFile))) {
            HashMap<String, Object> mapperContext = new HashMap<>();

            mapperContext.put("PackageName", entityMetadata.packageName());
            mapperContext.put("ClassName", entityMetadata.className());
            mapperContext.put("IdType", entityMetadata.id().type().getRegularName());
            mapperContext.put("Fields" , fields);
            mapperContext.put("Imports" , imports);
            mapperContext.put("Dependencies" , dependencies);
            mapperContext.put("Associations" , associations);

            mustache.execute(writer, mapperContext);
        }
    }
}
