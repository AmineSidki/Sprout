package org.aminesidki.runnable;

import com.github.javaparser.JavaParser;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;
import lombok.RequiredArgsConstructor;
import org.aminesidki.handler.GenerationHandler;
import org.aminesidki.handler.ParsingHandler;
import org.aminesidki.init.AnnotationGenerator;
import org.aminesidki.model.EntityMetadata;
import org.aminesidki.model.HelperMetadata;
import org.aminesidki.model.PartialGenerationFlags;
import org.aminesidki.provider.JavaParserProvider;
import org.aminesidki.util.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class MainRunnable implements Runnable{
    private final String defaultDir;
    private final boolean init;
    private final PartialGenerationFlags partialGenerationFlags;

    @Override
    public void run() {
        MustacheFactory mf = new DefaultMustacheFactory();

        // Total steps of the pipeline
        int TOTAL_STEPS = init ? 2 : 3;

        BannerPrinter.print();
        System.out.println(Logger.getInstance().stepMessage(1, TOTAL_STEPS, "Computing project root"));

        List<String> entityPackageNames = List.of("entity", "entities", "domain", "model", "models");
        String packageName = null;
        File[] files = null;

        for(String name : entityPackageNames){
            File entityPackage = new File(defaultDir + File.separator + name);
            files = entityPackage.listFiles();
            if(files != null){
                packageName = name;
                break;
            }
        }

        if(files == null){
            throw new RuntimeException("Couldn't resolve entity package directory !");
        }

        ProjectRootDirectoryResolver.DirectoryPackageView calculatedSourceRoot = ProjectRootDirectoryResolver.calculateProjectRootDirectory(files , new JavaParser());
        System.out.println(Logger.getInstance().infoMessage("Successfully resolved directory"));

        if(init){
            System.out.println(Logger.getInstance().stepMessage(2, TOTAL_STEPS, "Generating annotations"));
            try {
                new AnnotationGenerator(mf,defaultDir, calculatedSourceRoot.packageName()).generate();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Logger.getInstance().infoMessage("Successfully generated files"));
            System.out.println(Logger.getInstance().endMessage());
            return;
        }

        System.out.println(Logger.getInstance().stepMessage(2, TOTAL_STEPS, "Parsing java"));

        ConcurrentHashMap<String , EntityMetadata> entityMetadataMap = new ConcurrentHashMap<>();
        ConcurrentHashMap<String , HelperMetadata> helperMetadataMap = new ConcurrentHashMap<>();

        //Parsing
        JavaParserProvider javaParserProvider = new JavaParserProvider(calculatedSourceRoot.directory());
        ParsingHandler parsingHandler = new ParsingHandler(packageName, entityMetadataMap, helperMetadataMap, javaParserProvider);
        parsingHandler.parse(files);

        System.out.println(Logger.getInstance().infoMessage("Successfully parsed project classes"));
        System.out.println(Logger.getInstance().stepMessage(3, TOTAL_STEPS, "Generating files"));

        //Generation pre-processing
        GenerationHandler generationHandler = new GenerationHandler(entityMetadataMap, calculatedSourceRoot.packageName());
        GenerationHandlerInitializer initializer = new GenerationHandlerInitializer(packageName, defaultDir, entityMetadataMap, helperMetadataMap, generationHandler, partialGenerationFlags, mf);

        //Generation
        initializer.initialize();
        generationHandler.generate();

        System.out.println(Logger.getInstance().infoMessage("Successfully generated files"));
        System.out.println(Logger.getInstance().endMessage());
    }
}
