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
    PartialGenerationFlags partialGenerationFlags;

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

        new MainRunnable(defaultDir, partialGenerationFlags).run();
    }
}
