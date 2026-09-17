package org.aminesidki.runnable;

import org.aminesidki.model.PartialGenerationFlags;
import picocli.CommandLine;

@CommandLine.Command(name="default" , description = "Sprout scaffolding engine")
public class DefaultRunnable implements Runnable{

    @CommandLine.Option(names={"init"})
    boolean init = false;

    @CommandLine.Option(names={"clean"})
    boolean clean = false;

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

        if(clean){
            new CleanRunnable(defaultDir).run();
            System.exit(0);
        }

        new MainRunnable(defaultDir, init, partialGenerationFlags).run();
    }
}
