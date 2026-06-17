package org.aminesidki.handler;

import com.github.mustachejava.Mustache;
import lombok.RequiredArgsConstructor;
import org.aminesidki.exception.FileSystemException;
import org.aminesidki.generator.SproutFileGenerator;
import org.aminesidki.model.EntityMetadata;
import org.aminesidki.util.FileCreator;
import org.aminesidki.util.Logger;
import picocli.CommandLine;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class GenerationHandler {
    private final String defaultDir;
    private final Map<String, EntityMetadata> emm;
    private final FileCreator fileCreator;

    private record GeneratorView (SproutFileGenerator generator , Mustache mustache , String generationMessage){};
    private final List<GeneratorView> generatorList = new ArrayList<>();

    public void add(SproutFileGenerator generator , Mustache mustache , String generationMessage){
        generatorList.add(new GeneratorView(generator , mustache , generationMessage));
    }

    public void generate(){
        for(EntityMetadata em : emm.values()){
            if(em.isIgnored()) continue;
            try {
                for(GeneratorView gen : generatorList){
                    gen.generator().generate(em , gen.mustache(), defaultDir, fileCreator);
                    System.out.println(gen.generationMessage() + em.className());
                }
            } catch (IOException e) {
                    throw new FileSystemException("Failure in class " + em.className() + "\n" + e);
            } catch (FileSystemException fsE){
                System.out.println(Logger.getInstance().errorMessage("File generation failed for class " + em.className()));
                fsE.printStackTrace();
            }
        }
    }
}
