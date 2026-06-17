package org.aminesidki.generator.filegenerator;

import com.github.mustachejava.Mustache;
import lombok.RequiredArgsConstructor;
import org.aminesidki.exception.FileSystemException;
import org.aminesidki.generator.importsgenerator.GenericImportsGenerator;
import org.aminesidki.generator.SproutFileGenerator;
import org.aminesidki.model.EntityMetadata;
import org.aminesidki.model.HelperMetadata;
import org.aminesidki.util.FileCreator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Specific implementation for service classes generation of the <code>SproutFileGenerator</code>
 */
@RequiredArgsConstructor
public class ServiceGenerator implements SproutFileGenerator {
    private final GenericImportsGenerator genericImportsGenerator;
    private final Map<String , EntityMetadata> em;
    private final Map<String , HelperMetadata> hm;

    public void generate(EntityMetadata entityMetadata, Mustache mustache , String defDir, FileCreator fileCreator) throws IOException ,  FileSystemException {
        File serviceFile = fileCreator.createFile(entityMetadata.className(), "Service", defDir);

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(serviceFile))) {
            HashMap<String, Object> serviceContext = new HashMap<>();

            serviceContext.put("ClassName", entityMetadata.className());
            serviceContext.put("PackageName", entityMetadata.packageName());
            serviceContext.put("IdType", entityMetadata.id().type().regularName());
            serviceContext.put("Imports" , genericImportsGenerator.generate(entityMetadata, em, hm));
            serviceContext.put("className", entityMetadata.className().substring(0,1).toLowerCase() + entityMetadata.className().substring(1));
            serviceContext.put("Id", entityMetadata.id().name());
            serviceContext.put("Paginated" , entityMetadata.isPaginated());
            serviceContext.put("hasProjection" , entityMetadata.hasProjection());
            mustache.execute(writer, serviceContext);
        }
    }
}
