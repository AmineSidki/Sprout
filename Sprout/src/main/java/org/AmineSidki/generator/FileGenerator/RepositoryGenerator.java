package org.AmineSidki.generator.FileGenerator;

import com.github.mustachejava.Mustache;
import org.AmineSidki.exception.FileSystemException;
import org.AmineSidki.generator.SproutFileGenerator;
import org.AmineSidki.generator.SproutImportGenerator;
import org.AmineSidki.model.EntityMetadata;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class RepositoryGenerator implements SproutFileGenerator {

    public void generate(SproutImportGenerator importsGenerator, EntityMetadata entityMetadata, Mustache mustache , String defDir) throws IOException , FileSystemException{
        //Create the Repository package if it doesn't exist yet
        File repoPackage = new File(defDir + "/repository");
        if(!repoPackage.exists() && !repoPackage.mkdir()){
            throw new FileSystemException("");
        }

        File repoFile = new File(defDir + "/repository/" + entityMetadata.className() + "Repository.java");

        if(!repoFile.exists() && !repoFile.createNewFile()){
            throw new FileSystemException("");
        }

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(repoFile))) {
            HashMap<String, Object> repoContext = new HashMap<>();

            repoContext.put("PackageName", entityMetadata.packageName());
            repoContext.put("ClassName", entityMetadata.className());
            repoContext.put("IdType", entityMetadata.id().type().getRegularName());
            repoContext.put("Imports" , importsGenerator.generate(entityMetadata , null , null ));

            mustache.execute(writer, repoContext);
        }
    }
}
