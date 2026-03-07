package org.aminesidki.generator.filegenerator;

import com.github.mustachejava.Mustache;
import lombok.RequiredArgsConstructor;
import org.aminesidki.exception.FileSystemException;
import org.aminesidki.generator.importsgenerator.GenericImportsGenerator;
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
public class RepositoryGenerator implements SproutFileGenerator {
    private final GenericImportsGenerator genericImportsGenerator;
    private final Map<String , EntityMetadata> em;
    private final Map<String , HelperMetadata> hm;

    @Override
    public void generate(EntityMetadata entityMetadata, Mustache mustache , String defDir) throws IOException , FileSystemException{
        //Create the Repository package if it doesn't exist yet
        File repoPackage = new File(defDir + File.separator +"repository");
        if(!repoPackage.exists() && !repoPackage.mkdir()){
            throw new FileSystemException("Failed to generate repository for " + entityMetadata.className());
        }

        File repoFile = new File(defDir + File.separator +"repository"+ File.separator + entityMetadata.className() + "Repository.java");

        if(!repoFile.exists() && !repoFile.createNewFile()){
            throw new FileSystemException("Failed to generate repository for " + entityMetadata.className());
        }

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(repoFile))) {
            HashMap<String, Object> repoContext = new HashMap<>();

            repoContext.put("Imports", genericImportsGenerator.generate(entityMetadata, em, hm));
            repoContext.put("PackageName", entityMetadata.packageName());
            repoContext.put("ClassName", entityMetadata.className());
            repoContext.put("IdType", entityMetadata.id().type().regularName());

            mustache.execute(writer, repoContext);
        }
    }
}
