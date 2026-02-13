package org.AmineSidki.generator.FileGenerator;

import com.github.mustachejava.Mustache;
import lombok.RequiredArgsConstructor;
import org.AmineSidki.exception.FileSystemException;
import org.AmineSidki.generator.ImportsGenerator.GenericImportsGenerator;
import org.AmineSidki.generator.SproutFileGenerator;
import org.AmineSidki.model.EntityMetadata;
import org.AmineSidki.model.HelperMetadata;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ControllerGenerator implements SproutFileGenerator {
    private final GenericImportsGenerator genericImportsGenerator;
    private final Map<String , EntityMetadata> em;
    private final Map<String , HelperMetadata> hm;

    @Override
    public void generate(EntityMetadata entityMetadata, Mustache mustache, String defDir) throws IOException, FileSystemException {
        //Create the Repository package if it doesn't exist yet
        File controllerPackage = new File(defDir + "/controller");
        if(!controllerPackage.exists() && !controllerPackage.mkdir()){
            throw new FileSystemException("");
        }

        File controllerFile = new File(defDir + "/controller/" + entityMetadata.className() + "Controller.java");

        if(!controllerFile.exists() && !controllerFile.createNewFile()){
            throw new FileSystemException("");
        }

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(controllerFile))) {
            HashMap<String, Object> controllerContext = new HashMap<>();

            controllerContext.put("Imports", genericImportsGenerator.generate(entityMetadata, em, hm));
            controllerContext.put("PackageName", entityMetadata.packageName());
            controllerContext.put("ClassName", entityMetadata.className());
            controllerContext.put("className", entityMetadata.className().substring(0,1).toLowerCase() + entityMetadata.className().substring(1));
            controllerContext.put("IdType", entityMetadata.id().type().getRegularName());

            mustache.execute(writer, controllerContext);
        }
    }
}
