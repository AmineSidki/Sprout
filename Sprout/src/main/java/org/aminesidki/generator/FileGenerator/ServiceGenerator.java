package org.aminesidki.generator.FileGenerator;

import com.github.mustachejava.Mustache;
import lombok.RequiredArgsConstructor;
import org.aminesidki.exception.FileSystemException;
import org.aminesidki.generator.ImportsGenerator.GenericImportsGenerator;
import org.aminesidki.generator.SproutFileGenerator;
import org.aminesidki.model.EntityMetadata;
import org.aminesidki.model.HelperMetadata;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ServiceGenerator implements SproutFileGenerator {
    private final GenericImportsGenerator genericImportsGenerator;
    private final Map<String , EntityMetadata> em;
    private final Map<String , HelperMetadata> hm;

    public void generate(EntityMetadata entityMetadata, Mustache mustache , String defDir) throws IOException ,  FileSystemException {
        //Create the Service package if it doesn't exist yet
        File servicePackage = new File(defDir + File.separator +"service");
        if(!servicePackage.exists() && !servicePackage.mkdir()){
            throw new FileSystemException("Failed to generate service for " + entityMetadata.className());
        }

        File serviceFile = new File(defDir + File.separator +"service"+ File.separator + entityMetadata.className() + "Service.java");

        if(!serviceFile.exists() && !serviceFile.createNewFile()){
            throw new FileSystemException("Failed to generate service for " + entityMetadata.className());
        }

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(serviceFile))) {
            HashMap<String, Object> serviceContext = new HashMap<>();

            serviceContext.put("Imports" , genericImportsGenerator.generate(entityMetadata, em, hm));
            serviceContext.put("PackageName", entityMetadata.packageName());
            serviceContext.put("ClassName", entityMetadata.className());
            serviceContext.put("className", entityMetadata.className().substring(0,1).toLowerCase() + entityMetadata.className().substring(1));
            serviceContext.put("hasLightDTO" , entityMetadata.hasLightDTO());
            serviceContext.put("IdType", entityMetadata.id().type().regularName());
            serviceContext.put("Id", entityMetadata.id().name());
            mustache.execute(writer, serviceContext);
        }
    }
}
