package org.AmineSidki.generator;

import com.github.mustachejava.Mustache;
import org.AmineSidki.model.EntityMetadata;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class DtoGenerator implements SproutGenerator{

    public void generate(EntityMetadata entityMetadata, Mustache mustache , String defDir) throws IOException {
        //Create the 2 Dto sub-packages if they don't exist yet
        File requestDtoPackage = new File(defDir +  "/dto/requestDto");
        File responseDtoPackage = new File(defDir + "/dto/responseDto");
        if(!requestDtoPackage.exists() && !requestDtoPackage.mkdirs() || !responseDtoPackage.exists() && !responseDtoPackage.mkdirs() ){
            throw new RuntimeException("An error occurred whilst generating files.");
        }


        //Building the context, since they will be both basically identical for now (whilst I get to tackle associations)
        HashMap<String, Object> dtoContext = new HashMap<>();

        dtoContext.put("PackageName", entityMetadata.getPackageName());
        dtoContext.put("ClassName", entityMetadata.getClassName());
        dtoContext.put("IdType", entityMetadata.getIdType());
        dtoContext.put("Fields" , entityMetadata.getFields());

        //Request Dto
        File requestDtoFile = new File(defDir + "/dto/requestDto/" + entityMetadata.getClassName() + "RequestDTO.java");
        //Response Dto
        File responseDtoFile = new File(defDir + "/dto/responseDto/" + entityMetadata.getClassName() + "ResponseDTO.java");

        if((!requestDtoFile.exists() && !requestDtoFile.createNewFile()) || (!responseDtoFile.exists() && !responseDtoFile.createNewFile())){
            throw new RuntimeException("An error occurred whilst generating files.");
        }


        try(BufferedWriter writer = new BufferedWriter(new FileWriter(requestDtoFile))) {
            HashMap<String, Object> temp = new HashMap<>(dtoContext);
            temp.put("ReqRespU" , "request");
            temp.put("ReqRespL" , "Request");
            mustache.execute(writer, temp);
        }

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(responseDtoFile))) {
            HashMap<String, Object> temp = new HashMap<>(dtoContext);
            temp.put("ReqRespU" , "response");
            temp.put("ReqRespL" , "Response");
            mustache.execute(writer, temp);
        }
    }
}
