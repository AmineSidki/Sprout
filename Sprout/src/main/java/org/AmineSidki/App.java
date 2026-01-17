package org.AmineSidki;

import org.AmineSidki.runnable.DefaultRunnable;
import picocli.CommandLine;

public class App
{
    public static void main( String[] args ){
        int exitCode = new CommandLine(new DefaultRunnable()).execute(args);
        System.exit(exitCode);
    }
}