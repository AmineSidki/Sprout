package org.aminesidki.runnable;

import com.github.javaparser.JavaParser;
import lombok.RequiredArgsConstructor;
import org.aminesidki.handler.GenerationHandler;
import org.aminesidki.handler.ParsingHandler;
import org.aminesidki.model.EntityMetadata;
import org.aminesidki.model.HelperMetadata;
import org.aminesidki.model.PartialGenerationFlags;
import org.aminesidki.provider.JavaParserProvider;
import org.aminesidki.util.*;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class MainRunnable implements Runnable{
    private final String defaultDir;
    private final PartialGenerationFlags partialGenerationFlags;

    @Override
    public void run() {
        // Total steps of the pipeline
        int TOTAL_STEPS = 3;

        BannerPrinter.print();
        System.out.println(Logger.getInstance().stepMessage(1, TOTAL_STEPS, "Computing project root"));

        File entityPackage = new File(defaultDir + File.separator + "entity");
        File[] files = entityPackage.listFiles();

        if(files == null){
            throw new RuntimeException("Couldn't resolve entity package directory !");
        }

        File calculatedSourceRoot = ProjectRootDirectoryResolver.calculateProjectRootDirectory(files , new JavaParser());
        System.out.println(Logger.getInstance().infoMessage("Successfully resolved directory"));
        System.out.println(Logger.getInstance().stepMessage(2, TOTAL_STEPS, "Parsing java"));

        ConcurrentHashMap<String , EntityMetadata> entityMetadataMap = new ConcurrentHashMap<>();
        ConcurrentHashMap<String , HelperMetadata> helperMetadataMap = new ConcurrentHashMap<>();

        //Parsing
        JavaParserProvider javaParserProvider = new JavaParserProvider(calculatedSourceRoot);
        ParsingHandler parsingHandler = new ParsingHandler(entityMetadataMap, helperMetadataMap, javaParserProvider);
        parsingHandler.parse(files);

        System.out.println(Logger.getInstance().infoMessage("Successfully parsed project classes"));
        System.out.println(Logger.getInstance().stepMessage(3, TOTAL_STEPS, "Generating files"));

        //Generation pre-processing
        GenerationHandler generationHandler = new GenerationHandler(defaultDir , entityMetadataMap, new FileCreator());
        GenerationHandlerInitializer initializer = new GenerationHandlerInitializer(entityMetadataMap, helperMetadataMap, generationHandler, partialGenerationFlags);

        //Generation
        initializer.initialize();
        generationHandler.generate();

        System.out.println(Logger.getInstance().infoMessage("Successfully generated files"));
        System.out.println(Logger.getInstance().endMessage());
    }
}
