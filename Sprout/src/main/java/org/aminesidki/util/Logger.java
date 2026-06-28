package org.aminesidki.util;

import picocli.CommandLine;

import java.time.LocalDateTime;

/**
 * Provides properly formatted info messages
 */
public class Logger {
    private static Logger instance;

    public static Logger getInstance(){
        if(instance == null){
            instance = new Logger();
        }
        return instance;
    }

    public String infoMessage(String message){
        return CommandLine.Help.Ansi.AUTO.string("@|faint " + LocalDateTime.now() + "|@ @|bold,blue  INFO|@ --- @|magenta [Sprout]|@ : " + message);
    }

    public String errorMessage(String message){
        return CommandLine.Help.Ansi.AUTO.string("@|faint " + LocalDateTime.now() + "|@ @|bold,red  ERROR|@ --- @|magenta [Sprout]|@ : " + message);
    }

    public String stepMessage(int current, int end, String step){
        return CommandLine.Help.Ansi.AUTO.string("@|bold Pass " + current + "/" + end + " : " + step + " |@ \n");
    }

    public String endMessage(){
        return infoMessage("Shutting down..");
    }

    public String warningMessage(String message) {
        return CommandLine.Help.Ansi.AUTO.string("@|faint " + LocalDateTime.now() + "|@ @|bold,yellow  WARN|@ --- @|magenta [Sprout]|@ : " + message);
    }
}
