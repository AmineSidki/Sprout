package org.AmineSidki.generator;

import com.github.mustachejava.Mustache;
import org.AmineSidki.exception.FileSystemException;
import org.AmineSidki.model.EntityMetadata;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class RepositoryGenerator implements SproutGenerator{

    public void generate(EntityMetadata entityMetadata, Mustache mustache , String defDir) throws IOException , FileSystemException{
        //Create the Repository package if it doesn't exist yet
        File repoPackage = new File(defDir + "/repository");
        if(!repoPackage.exists() && !repoPackage.mkdir()){
            throw new FileSystemException("");
        }

        File repoFile = new File(defDir + "/repository/" + entityMetadata.getClassName() + "Repository.java");

        if(!repoFile.exists() && !repoFile.createNewFile()){
            throw new FileSystemException("");
        }

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(repoFile))) {
            HashMap<String, Object> repoContext = new HashMap<>();

            repoContext.put("PackageName", entityMetadata.getPackageName());
            repoContext.put("ClassName", entityMetadata.getClassName());
            repoContext.put("IdType", entityMetadata.getIdType());

            mustache.execute(writer, repoContext);
        }
    }
}
