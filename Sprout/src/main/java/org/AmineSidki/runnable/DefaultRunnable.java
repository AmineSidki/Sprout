package org.AmineSidki.runnable;

import com.github.javaparser.JavaParser;
import org.AmineSidki.exception.FileSystemException;
import org.AmineSidki.handler.GenerationHandler;
import org.AmineSidki.handler.ParsingHandler;
import org.AmineSidki.util.*;
import org.AmineSidki.model.EntityMetadata;
import org.AmineSidki.model.HelperMetadata;
import org.AmineSidki.model.PartialGenerationFlags;
import picocli.CommandLine;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

@CommandLine.Command(name="default" , version = "1.4.5" , description = "Sprout scaffolding engine")
public class DefaultRunnable implements Runnable{

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    @CommandLine.Option(names = "--dir")
    private String defaultDir = ".";

    @CommandLine.ArgGroup(exclusive = false)
    PartialGenerationFlags pGroup;

    @Override
    public void run() {
        BannerPrinter.print();
        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold Pass 1/3 : Computing project root |@\n"));

        File entityPackage = new File(defaultDir + File.separator + "entity");
        File[] files = entityPackage.listFiles();

        if(files == null){
            throw new RuntimeException("Couldn't resolve entity package directory !");
        }

        File calculatedSourceRoot = ParserUtil.calculateProjectRootDirectory(files , new JavaParser());
        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|faint " + LocalDateTime.now() + "|@ @|bold,blue  INFO|@ --- @|magenta [Sprout]|@ : Successfully resolved directory"));
        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold Pass 2/3 : Parsing Java |@ \n"));

        ConcurrentHashMap<String , EntityMetadata> emm = new ConcurrentHashMap<>();
        ConcurrentHashMap<String , HelperMetadata> hmm = new ConcurrentHashMap<>();

        //Parsing
        ParsingHandler parsingHandler = new ParsingHandler(emm, hmm, calculatedSourceRoot);
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
