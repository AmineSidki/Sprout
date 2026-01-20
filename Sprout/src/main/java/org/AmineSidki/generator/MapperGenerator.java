package org.AmineSidki.generator;

import com.github.mustachejava.Mustache;
import org.AmineSidki.enumeration.Association;
import org.AmineSidki.exception.FileSystemException;
import org.AmineSidki.model.EntityMetadata;
import org.AmineSidki.model.FieldMetadata;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MapperGenerator implements SproutGenerator{

    public void generate(EntityMetadata entityMetadata, Mustache mustache , String defDir) throws IOException , FileSystemException{
        //Create the mapper package if it doesn't exist yet
        File mapperPackage = new File(defDir + "/mapper");
        if(!mapperPackage.exists() && !mapperPackage.mkdir()){
            throw new FileSystemException("");
        }

        File mapperFile = new File(defDir + "/mapper/" + entityMetadata.getClassName() + "Mapper.java");

        if(!mapperFile.exists() && !mapperFile.createNewFile()){
            throw new FileSystemException("");
        }

        List<FieldMetadata> fields = entityMetadata.getFields()
                .stream()
                .map(f -> new FieldMetadata(f.getType() ,
                        f.getName().substring(0,1).toUpperCase() + f.getName().substring(1) ,
                        f.getAssociation())).filter(f -> f.getAssociation().equals(Association.DEFAULT))
                .collect(Collectors.toList());

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(mapperFile))) {
            HashMap<String, Object> mapperContext = new HashMap<>();

            mapperContext.put("PackageName", entityMetadata.getPackageName());
            mapperContext.put("ClassName", entityMetadata.getClassName());
            mapperContext.put("IdType", entityMetadata.getIdType());
            mapperContext.put("Fields" , fields);

            mustache.execute(writer, mapperContext);
        }
    }
}
