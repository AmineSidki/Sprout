package org.aminesidki.generator.filegenerator;

import com.github.mustachejava.Mustache;
import lombok.RequiredArgsConstructor;
import org.aminesidki.exception.FileSystemException;
import org.aminesidki.generator.importsgenerator.DtoImportsGenerator;
import org.aminesidki.generator.SproutFileGenerator;
import org.aminesidki.model.EntityMetadata;
import org.aminesidki.model.FieldMetadata;
import org.aminesidki.model.HelperMetadata;
import org.aminesidki.util.DtoFieldMapper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Specific implementation for dto classes generation of the <code>SproutFileGenerator</code>
 */
@RequiredArgsConstructor
public class DtoGenerator implements SproutFileGenerator {
    private final DtoImportsGenerator dtoImportsGenerator;
    private final Map<String, EntityMetadata> persistenceMetadata;
    private final Map <String, HelperMetadata> helperMetadata;

    private record RecordFieldView(FieldMetadata field , boolean last){} ;

    public void generate(EntityMetadata entityMetadata, Mustache mustache, String defDir) throws IOException, FileSystemException {
        //Create dto package if it doesn't exist yet
        File dtoPackage = new File(defDir + File.separator +"dto");
        if (!dtoPackage.exists() && !dtoPackage.mkdir()) {
            throw new FileSystemException("Failed to generate dto for " + entityMetadata.className());
        }

        File lightDtoFile = null;
        File dtoFile = new File(defDir + File.separator +"dto"+ File.separator + entityMetadata.className() + "DTO.java");

        if ((!dtoFile.exists() && !dtoFile.createNewFile())) {
            throw new FileSystemException("Failed to generate dto for " + entityMetadata.className());
        }

        if(entityMetadata.hasLightDTO()){
            lightDtoFile = new File(defDir + File.separator +"dto"+ File.separator + "Light" + entityMetadata.className() + "DTO.java");
            if(!lightDtoFile.exists() && !lightDtoFile.createNewFile()){
                throw new FileSystemException("Failed to generate light dto for " + entityMetadata.className());
            }
        }

        List <FieldMetadata> fields = DtoFieldMapper.mapToDtoField(entityMetadata.fields() , persistenceMetadata, helperMetadata);


        //Generating regular DTO
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dtoFile))) {
            HashMap <String,Object> dtoContext = new HashMap <>();

            HashSet<String> imports = dtoImportsGenerator.generate(entityMetadata, persistenceMetadata, helperMetadata);

            dtoContext.put("Imports" , imports);
            dtoContext.put("PackageName", entityMetadata.packageName());
            dtoContext.put("ClassName", entityMetadata.className());
            dtoContext.put("IdType", entityMetadata.id().type().regularName());
            dtoContext.put("Fields", fields.stream()
                    .map(f -> new RecordFieldView(f, f == fields.get(fields.size() - 1)))
                    .toList());

            mustache.execute(writer, dtoContext);
        }

        //In case of light DTO, generate light DTO too
        if(entityMetadata.hasLightDTO()){
            List<FieldMetadata> lightFields = DtoFieldMapper.mapToDtoField(entityMetadata.lightFields(), persistenceMetadata, helperMetadata);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(lightDtoFile))) {
                HashMap <String,Object> dtoContext = new HashMap <>();

                HashSet<String> imports = dtoImportsGenerator.generate(entityMetadata, persistenceMetadata, helperMetadata);

                dtoContext.put("Imports" , imports);
                dtoContext.put("isLight" , true);
                dtoContext.put("PackageName", entityMetadata.packageName());
                dtoContext.put("ClassName", entityMetadata.className());
                dtoContext.put("IdType", entityMetadata.id().type().regularName());
                dtoContext.put("Fields", lightFields.stream()
                        .map(f -> new RecordFieldView(f, f == lightFields.get(lightFields.size() - 1)))
                        .toList());

                mustache.execute(writer, dtoContext);
            }
        }
    }
}