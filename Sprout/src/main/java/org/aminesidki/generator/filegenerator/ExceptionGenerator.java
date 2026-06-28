package org.aminesidki.generator.filegenerator;

import com.github.mustachejava.Mustache;
import lombok.RequiredArgsConstructor;
import org.aminesidki.exception.FileSystemException;
import org.aminesidki.generator.SproutFileGenerator;
import org.aminesidki.model.EntityMetadata;
import org.aminesidki.util.FileCreator;
import org.aminesidki.util.Ledger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * Specific implementation for exception classes generation of the <code>SproutFileGenerator</code>
 */
@RequiredArgsConstructor
public class ExceptionGenerator implements SproutFileGenerator {

    @Override
    public void generate(EntityMetadata entityMetadata, Mustache mustache, String defDir, FileCreator fileCreator, Ledger ledger) throws IOException, FileSystemException {
        File exceptionFile = fileCreator.createFile(entityMetadata.className(), "Exception", defDir);

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(exceptionFile))) {
            HashMap<String, Object> exceptionContext = new HashMap<>();

            exceptionContext.put("ClassName", entityMetadata.className());
            exceptionContext.put("PackageName", entityMetadata.packageName());

            mustache.execute(writer, exceptionContext);
        }
    }
}
