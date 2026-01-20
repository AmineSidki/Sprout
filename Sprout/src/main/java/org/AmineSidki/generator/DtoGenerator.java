package org.AmineSidki.generator;

import com.github.mustachejava.Mustache;
import lombok.RequiredArgsConstructor;
import org.AmineSidki.enumeration.Association;
import org.AmineSidki.exception.FileSystemException;
import org.AmineSidki.model.EntityMetadata;
import org.AmineSidki.model.FieldMetadata;
import org.AmineSidki.util.ParserUtil;

import javax.swing.text.html.parser.Parser;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DtoGenerator implements SproutGenerator {
    private final Map <String, EntityMetadata> persistenceMetadata;

    public void generate(EntityMetadata entityMetadata, Mustache mustache, String defDir) throws IOException, FileSystemException {
        //Create dto package if it doesn't exist yet
        File dtoPackage = new File(defDir + "/dto");
        if (!dtoPackage.exists() && !dtoPackage.mkdir()) {
            throw new FileSystemException("");
        }

        File dtoFile = new File(defDir + "/dto/" + entityMetadata.getClassName() + "DTO.java");

        if ((!dtoFile.exists() && !dtoFile.createNewFile())) {
            throw new FileSystemException("");
        }

        List <FieldMetadata> fields = ParserUtil.mapToDtoField(entityMetadata , persistenceMetadata);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dtoFile))) {
            HashMap < String, Object > dtoContext = new HashMap < > ();

            dtoContext.put("PackageName", entityMetadata.getPackageName());
            dtoContext.put("ClassName", entityMetadata.getClassName());
            dtoContext.put("IdType", entityMetadata.getIdType());
            dtoContext.put("Fields", fields);

            mustache.execute(writer, dtoContext);
        }
    }
}