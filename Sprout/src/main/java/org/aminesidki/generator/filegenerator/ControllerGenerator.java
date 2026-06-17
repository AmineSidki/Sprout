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
 * Specific implementation for controller classes generation of the <code>SproutFileGenerator</code>
 */
@RequiredArgsConstructor
public class ControllerGenerator implements SproutFileGenerator {
    private final GenericImportsGenerator genericImportsGenerator;
    private final Map<String , EntityMetadata> em;
    private final Map<String , HelperMetadata> hm;

    @Override
    public void generate(EntityMetadata entityMetadata, Mustache mustache, String defDir, FileCreator fileCreator) throws IOException, FileSystemException {
        File controllerFile = fileCreator.createFile(entityMetadata.className(), "Controller", defDir);

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(controllerFile))) {
            HashMap<String, Object> controllerContext = new HashMap<>();

            controllerContext.put("Imports", genericImportsGenerator.generate(entityMetadata, em, hm));
            controllerContext.put("Paginated" , entityMetadata.isPaginated());
            controllerContext.put("PackageName", entityMetadata.packageName());
            controllerContext.put("ClassName", entityMetadata.className());
            controllerContext.put("className", entityMetadata.className().substring(0,1).toLowerCase() + entityMetadata.className().substring(1));
            controllerContext.put("hasProjection" , entityMetadata.hasProjection());
            controllerContext.put("IdType", entityMetadata.id().type().regularName());

            mustache.execute(writer, controllerContext);
        }
    }
}
