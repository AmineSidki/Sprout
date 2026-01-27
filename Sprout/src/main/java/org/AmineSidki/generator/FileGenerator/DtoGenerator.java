package org.AmineSidki.generator.FileGenerator;

import com.github.mustachejava.Mustache;
import lombok.RequiredArgsConstructor;
import org.AmineSidki.exception.FileSystemException;
import org.AmineSidki.generator.SourceGenerator.ImportGenerator;
import org.AmineSidki.generator.SproutFileGenerator;
import org.AmineSidki.model.EntityMetadata;
import org.AmineSidki.model.FieldMetadata;
import org.AmineSidki.model.HelperMetadata;
import org.AmineSidki.util.ParserUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class DtoGenerator implements SproutFileGenerator {
    private final ConcurrentHashMap<String, EntityMetadata> persistenceMetadata;
    private final ConcurrentHashMap <String, HelperMetadata> helperMetadata;

    public void generate(ImportGenerator importGenerator , EntityMetadata entityMetadata, Mustache mustache, String defDir) throws IOException, FileSystemException {
        //Create dto package if it doesn't exist yet
        File dtoPackage = new File(defDir + "/dto");
        if (!dtoPackage.exists() && !dtoPackage.mkdir()) {
            throw new FileSystemException("");
        }

        File dtoFile = new File(defDir + "/dto/" + entityMetadata.getClassName() + "DTO.java");

        if ((!dtoFile.exists() && !dtoFile.createNewFile())) {
            throw new FileSystemException("");
        }

        List <FieldMetadata> fields = ParserUtil.mapToDtoField(entityMetadata , persistenceMetadata, helperMetadata);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dtoFile))) {
            HashMap <String,Object> dtoContext = new HashMap <> ();

            HashSet<String> imports = importGenerator.generate(entityMetadata, persistenceMetadata, helperMetadata , "dto");

            dtoContext.put("PackageName", entityMetadata.getPackageName());
            dtoContext.put("ClassName", entityMetadata.getClassName());
            dtoContext.put("IdType", entityMetadata.getIdType().getClassName());
            dtoContext.put("Fields", fields);
            dtoContext.put("Imports" , imports);

            mustache.execute(writer, dtoContext);
        }
    }
}