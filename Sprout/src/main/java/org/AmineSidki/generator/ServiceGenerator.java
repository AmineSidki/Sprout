package org.AmineSidki.generator;

import com.github.mustachejava.Mustache;
import org.AmineSidki.exception.FileSystemException;
import org.AmineSidki.model.EntityMetadata;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class ServiceGenerator implements SproutGenerator{

    public void generate(EntityMetadata entityMetadata, Mustache mustache , String defDir) throws IOException ,  FileSystemException {
        //Create the Service package if it doesn't exist yet
        File servicePackage = new File(defDir + "/service");
        if(!servicePackage.exists() && !servicePackage.mkdir()){
            throw new FileSystemException("");
        }

        File serviceFile = new File(defDir + "/service/" + entityMetadata.getClassName() + "Service.java");

        if(!serviceFile.exists() && !serviceFile.createNewFile()){
            throw new FileSystemException("");
        }

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(serviceFile))) {
            HashMap<String, Object> serviceContext = new HashMap<>();

            serviceContext.put("PackageName", entityMetadata.getPackageName());
            serviceContext.put("ClassName", entityMetadata.getClassName());
            serviceContext.put("IdType", entityMetadata.getIdType());

            mustache.execute(writer, serviceContext);
        }
    }
}
