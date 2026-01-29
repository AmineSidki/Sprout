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
    public record AssociationView(FieldMetadata field , FieldMetadata id , boolean multiple) {};

    public void generate(SproutImportGenerator importsGenerator, EntityMetadata entityMetadata, Mustache mustache , String defDir) throws IOException , FileSystemException{
        //Create the mapper package if it doesn't exist yet
        File mapperPackage = new File(defDir + "/mapper");
        if(!mapperPackage.exists() && !mapperPackage.mkdir()){
            throw new FileSystemException("");
        }

        File mapperFile = new File(defDir + "/mapper/" + entityMetadata.getClassName() + "Mapper.java");

        if(!mapperFile.exists() && !mapperFile.createNewFile()){
            throw new FileSystemException("");
        }

        List<FieldMetadata> fields = entityMetadata.getFields().stream()
                .filter(f -> f.getAssociation().equals(Association.DEFAULT))
                .map(f -> new FieldMetadata(f.getType() ,
                        f.getName().substring(0,1).toUpperCase() + f.getName().substring(1) ,
                        f.getAssociation()))
                .collect(Collectors.toList());


        List<FieldMetadata> associationFields = entityMetadata
                .getFields()
                .stream()
                .filter(f -> !f.getAssociation().equals(Association.DEFAULT))
                .toList();

        List<AssociationView> associations = associationFields.stream().map(f -> {
           FieldMetadata fieldMetadata = new FieldMetadata(f.getType() ,
                   f.getName() ,
                   f.getAssociation());

            f = new FieldMetadata(
                        new TypeMetadata((f.getAssociation() == Association.ONE_TO_MANY || f.getAssociation() == Association.MANY_TO_MANY)?
                                ParserUtil.extractCollectionGenericType(f.getType().getRegularName().toLowerCase()) : f.getType().getRegularName().toLowerCase(),
                                f.getType().getFullQualifiedName()),
                        f.getName().substring(0,1).toUpperCase() + f.getName().substring(1) ,
                        f.getAssociation());

            String cleanField = fieldMetadata.getType().getRegularName();
            boolean multiple = false;

            if(f.getAssociation() == Association.ONE_TO_MANY || f.getAssociation() == Association.MANY_TO_MANY){
                cleanField = ParserUtil.extractCollectionGenericType(cleanField);
                multiple = true;
            }

            if(persistenceMap.get(cleanField) != null){
                return new AssociationView(f , persistenceMap.get(cleanField).getId() , multiple );
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

            mapperContext.put("PackageName", entityMetadata.getPackageName());
            mapperContext.put("ClassName", entityMetadata.getClassName());
            mapperContext.put("IdType", entityMetadata.getId().getType().getRegularName());
            mapperContext.put("Fields" , fields);
            mapperContext.put("Imports" , imports);
            mapperContext.put("Dependencies" , dependencies);
            mapperContext.put("Associations" , associations);

            mustache.execute(writer, mapperContext);
        }
    }
}

// List <ID> --> List<Project>
// list.stream.map(id -> repo.get)
