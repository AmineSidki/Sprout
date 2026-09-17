package org.aminesidki.generator.onceperpipelinegenerator;

import com.github.mustachejava.Mustache;
import lombok.RequiredArgsConstructor;
import org.aminesidki.exception.FileSystemException;
import org.aminesidki.generator.SproutOncePerPipelineGenerator;
import org.aminesidki.util.FileCreator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Specific implementation for ResourceNotFound exception class generation of the <code>SproutOncePerPipelineGenerator</code>
 */
@RequiredArgsConstructor
public class ResourceNotFoundExceptionGenerator implements SproutOncePerPipelineGenerator {

    @Override
    public void generate(Mustache mustache, String defDir, String packageName) throws IOException, FileSystemException {
        File exceptionFile = FileCreator.createFile("ResourceNotFound", "Exception", defDir);

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(exceptionFile))) {
            mustache.execute(writer, Map.of("PackageName", packageName));
        }
    }
}
