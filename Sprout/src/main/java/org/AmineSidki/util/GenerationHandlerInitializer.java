package org.AmineSidki.util;

import lombok.RequiredArgsConstructor;
import org.AmineSidki.generator.DependencyGenerator.MapperDependencyGenerator;
import org.AmineSidki.generator.FileGenerator.*;
import org.AmineSidki.generator.ImportsGenerator.DtoImportsGenerator;
import org.AmineSidki.generator.ImportsGenerator.GenericImportsGenerator;
import org.AmineSidki.generator.ImportsGenerator.MapperImportsGenerator;
import org.AmineSidki.handler.GenerationHandler;
import org.AmineSidki.model.EntityMetadata;
import org.AmineSidki.model.HelperMetadata;
import org.AmineSidki.model.InitializationResources;
import org.AmineSidki.model.PartialGenerationFlags;
import picocli.CommandLine;

import java.time.LocalDateTime;
import java.util.Map;

@RequiredArgsConstructor
public class GenerationHandlerInitializer {
    private final Map<String , EntityMetadata> emm;
    private final Map<String , HelperMetadata> hmm;
    private final PartialGenerationFlags pGroup;
    private final GenerationHandler handler;

    public void initialize(){
        final InitializationResources resources = new InitializationResourcesMapper().map(pGroup);

        //I hate this, but it seems like the only option for now :(
        if(pGroup == null || pGroup.cFlag || pGroup.rFlag || pGroup.sFlag){

            GenericImportsGenerator genericImportsGenerator = new GenericImportsGenerator();

            if(pGroup == null || pGroup.rFlag){
                handler.add(new RepositoryGenerator(genericImportsGenerator, emm, hmm),
                        resources.repoMustache(),
                        CommandLine.Help.Ansi.AUTO.string("@|faint " + LocalDateTime.now() + "|@ @|bold,blue  INFO|@ --- @|magenta [Sprout]|@ : Generating repository for "));
            }
            if(pGroup == null || pGroup.sFlag){
                handler.add(new ServiceGenerator(genericImportsGenerator, emm, hmm),
                        resources.serviceMustache(),
                        CommandLine.Help.Ansi.AUTO.string("@|faint " + LocalDateTime.now() + "|@ @|bold,blue  INFO|@ --- @|magenta [Sprout]|@ : Generating service for "));
            }
            if(pGroup == null || pGroup.cFlag){
                handler.add(new ControllerGenerator(genericImportsGenerator, emm, hmm),
                        resources.controllerMustache(),
                        CommandLine.Help.Ansi.AUTO.string("@|faint " + LocalDateTime.now() + "|@ @|bold,blue  INFO|@ --- @|magenta [Sprout]|@ : Generating controller for "));
            }
        }
        if(pGroup == null || pGroup.mFlag){
            MapperImportsGenerator mapperImportGen = new MapperImportsGenerator();
            MapperDependencyGenerator mapperDependencyGen = new MapperDependencyGenerator();
            handler.add( new MapperGenerator(mapperImportGen , mapperDependencyGen, emm, hmm),
                    resources.mapperMustache(),
                    CommandLine.Help.Ansi.AUTO.string("@|faint " + LocalDateTime.now() + "|@ @|bold,blue  INFO|@ --- @|magenta [Sprout]|@ : Generating mapper for "));
        }
        if(pGroup == null || pGroup.dFlag){
            DtoImportsGenerator dtoImportGen = new DtoImportsGenerator();
            handler.add(new DtoGenerator(dtoImportGen, emm, hmm),
                    resources.dtoMustache(),
                    CommandLine.Help.Ansi.AUTO.string("@|faint " + LocalDateTime.now() + "|@ @|bold,blue  INFO|@ --- @|magenta [Sprout]|@ : Generating DTO for "));
        }
        if(pGroup == null || pGroup.eFlag){
            handler.add(new ExceptionGenerator(),
                    resources.exceptionMustache(),
                    CommandLine.Help.Ansi.AUTO.string("@|faint " + LocalDateTime.now() + "|@ @|bold,blue  INFO|@ --- @|magenta [Sprout]|@ : Generating exception for "));
        }
    }
}
