package org.AmineSidki.generator;

import com.github.mustachejava.Mustache;
import lombok.RequiredArgsConstructor;
import org.AmineSidki.exception.FileSystemException;
import org.AmineSidki.model.EntityMetadata;
import picocli.CommandLine;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class GenerationScheduler {
    private final String defaultDir;
    private final Map<String, EntityMetadata> emm;

    private record GeneratorView (SproutFileGenerator generator , Mustache mustache , String generationMessage){};
    private final List<GeneratorView> generatorList = new ArrayList<>();

    public void add(SproutFileGenerator generator , Mustache mustache , String generationMessage){
        generatorList.add(new GeneratorView(generator , mustache , generationMessage));
    }

    public void generate(){
        for(EntityMetadata em : emm.values()){
            try {
                for(GeneratorView gen : generatorList){
                    gen.generator().generate(em , gen.mustache(), defaultDir);
                    System.out.println(gen.generationMessage());
                }
            } catch (IOException e) {
                throw new FileSystemException("");
            } catch (FileSystemException fsE){
                System.out.println(CommandLine.Help.Ansi.AUTO.string("@|faint " + LocalDateTime.now() + "|@ @|bold,red  ERROR|@ --- @|magenta [Sprout]|@ : File generation failed for class " + em.className()));
            }
        }
    }
}
