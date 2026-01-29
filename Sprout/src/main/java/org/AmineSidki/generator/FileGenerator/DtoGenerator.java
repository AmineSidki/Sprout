package org.AmineSidki.generator.FileGenerator;

import com.github.mustachejava.Mustache;
import lombok.RequiredArgsConstructor;
import org.AmineSidki.exception.FileSystemException;
import org.AmineSidki.generator.SproutFileGenerator;
import org.AmineSidki.generator.SproutImportGenerator;
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
import java.util.Map;

@RequiredArgsConstructor
public class DtoGenerator implements SproutFileGenerator {
    private final Map<String, EntityMetadata> persistenceMetadata;
    private final Map <String, HelperMetadata> helperMetadata;

    public void generate(SproutImportGenerator importsGenerator, EntityMetadata entityMetadata, Mustache mustache, String defDir) throws IOException, FileSystemException {
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
            HashMap <String,Object> dtoContext = new HashMap <>();

            HashSet<String> imports = importsGenerator.generate(entityMetadata, persistenceMetadata, helperMetadata);

            dtoContext.put("PackageName", entityMetadata.getPackageName());
            dtoContext.put("ClassName", entityMetadata.getClassName());
            dtoContext.put("IdType", entityMetadata.getId().getType().getRegularName());
            dtoContext.put("Fields", fields);
            dtoContext.put("Imports" , imports);

            mustache.execute(writer, dtoContext);
        }
    }
}