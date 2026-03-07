package org.aminesidki.runnable;

import com.github.javaparser.JavaParser;
import org.aminesidki.handler.GenerationHandler;
import org.aminesidki.handler.ParsingHandler;
import org.aminesidki.provider.JavaParserProvider;
import org.aminesidki.util.*;
import org.aminesidki.model.EntityMetadata;
import org.aminesidki.model.HelperMetadata;
import org.aminesidki.model.PartialGenerationFlags;
import picocli.CommandLine;

import java.io.*;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@CommandLine.Command(name="default" , description = "Sprout scaffolding engine")
public class DefaultRunnable implements Runnable{

    @CommandLine.Option(names={"--help" , "-h"})
    boolean help = false;

    @CommandLine.Option(names={"--version" , "-v"})
    boolean version = false;

    @CommandLine.Option(names = "--dir")
    private String defaultDir = ".";

    @CommandLine.ArgGroup(exclusive = false)
    PartialGenerationFlags pGroup;

    @Override
    public void run() {
        if(help){
            new HelpRunnable().run();
            System.exit(0);
        }

        if(version){
            new VersionRunnable().run();
            System.exit(0);
        }

        BannerPrinter.print();
        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold Pass 1/3 : Computing project root |@\n"));

        File entityPackage = new File(defaultDir + File.separator + "entity");
        File[] files = entityPackage.listFiles();

        if(files == null){
            throw new RuntimeException("Couldn't resolve entity package directory !");
        }

        File calculatedSourceRoot = ProjectRootDirectoryResolver.calculateProjectRootDirectory(files , new JavaParser());
        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|faint " + LocalDateTime.now() + "|@ @|bold,blue  INFO|@ --- @|magenta [Sprout]|@ : Successfully resolved directory"));
        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold Pass 2/3 : Parsing Java |@ \n"));

        ConcurrentHashMap<String , EntityMetadata> emm = new ConcurrentHashMap<>();
        ConcurrentHashMap<String , HelperMetadata> hmm = new ConcurrentHashMap<>();

        //Parsing
        JavaParserProvider javaParserProvider = new JavaParserProvider(calculatedSourceRoot);
        ParsingHandler parsingHandler = new ParsingHandler(emm, hmm, javaParserProvider);
        parsingHandler.parse(files);

        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold \nPass 3/3 : Generating classes |@ \n"));

        //Generation pre-processing
        GenerationHandler generationHandler = new GenerationHandler(defaultDir , emm);
        GenerationHandlerInitializer initializer = new GenerationHandlerInitializer(emm, hmm, pGroup, generationHandler);

        //Generation
        initializer.initialize();
        generationHandler.generate();

        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold \nGeneration service ended successfully, shutting down ! |@ \n"));
    }
}
