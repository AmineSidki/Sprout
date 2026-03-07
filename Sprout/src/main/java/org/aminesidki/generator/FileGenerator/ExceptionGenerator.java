package org.aminesidki.generator.FileGenerator;

import com.github.mustachejava.Mustache;
import lombok.RequiredArgsConstructor;
import org.aminesidki.exception.FileSystemException;
import org.aminesidki.generator.SproutFileGenerator;
import org.aminesidki.model.EntityMetadata;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

@RequiredArgsConstructor
public class ExceptionGenerator implements SproutFileGenerator {

    @Override
    public void generate(EntityMetadata entityMetadata, Mustache mustache, String defDir) throws IOException, FileSystemException {
        //Create the Repository package if it doesn't exist yet
        File exceptionPackage = new File(defDir + File.separator + "exception");
        if(!exceptionPackage.exists() && !exceptionPackage.mkdir()){
            throw new FileSystemException("Failed to generate exception for " + entityMetadata.className());
        }

        File exceptionFile = new File(defDir + File.separator + "exception"+ File.separator + entityMetadata.className() + "NotFoundException.java");

        if(!exceptionFile.exists() && !exceptionFile.createNewFile()){
            throw new FileSystemException("Failed to generate exception for " + entityMetadata.className());
        }

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(exceptionFile))) {
            HashMap<String, Object> exceptionContext = new HashMap<>();

            exceptionContext.put("PackageName", entityMetadata.packageName());
            exceptionContext.put("ClassName", entityMetadata.className());

            mustache.execute(writer, exceptionContext);
        }
    }
}
