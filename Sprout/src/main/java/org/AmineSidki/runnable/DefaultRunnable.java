package org.AmineSidki.runnable;

import com.github.javaparser.JavaParser;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.AmineSidki.exception.FileSystemException;
import org.AmineSidki.exception.NotAnEntityException;
import org.AmineSidki.exception.ParsingException;
import org.AmineSidki.generator.DtoGenerator;
import org.AmineSidki.generator.MapperGenerator;
import org.AmineSidki.generator.RepositoryGenerator;
import org.AmineSidki.generator.ServiceGenerator;
import org.AmineSidki.model.EntityMetadata;
import org.AmineSidki.util.BannerPrinter;
import org.AmineSidki.util.EntityParser;
import picocli.CommandLine;

import java.io.*;
import java.time.LocalDateTime;
import java.util.HashMap;

@CommandLine.Command(name="default" , version = "0.1" , description = "Default Sprout workflow")
public class DefaultRunnable implements Runnable{

    @CommandLine.Option(names = "-d , --dir")
    private String defaultDir = ".";

    @Override
    public void run() {
        BannerPrinter.print();


        File entityPackage = new File(defaultDir + "/entity");
        File[] files = entityPackage.listFiles();

        if(files == null){
            throw new RuntimeException("Couldn't resolve entity package directory !");
        }

        JavaParser parser = new JavaParser();
        MustacheFactory mf = new DefaultMustacheFactory();

        //TODO: Change this into a generator list

        //Compiling templates
        Mustache repoMustache = mf.compile("templates/RepositoryTemplate.mustache");
        Mustache serviceMustache = mf.compile("templates/ServiceTemplate.mustache");
        Mustache dtoMustache = mf.compile("templates/DtoTemplate.mustache");
        Mustache mapperMustache = mf.compile("templates/MapperTemplate.mustache");

        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold Pass 1/2 : Parsing Java |@ \n"));

        HashMap<String , EntityMetadata> eml = new HashMap<>();

        //Parsing the entirety of the files in the directory
        for(File entity : files){
            if(!entity.isFile()){
                continue;
            }

            try {
                EntityMetadata em = EntityParser.parse(parser , entity);
                System.out.println(CommandLine.Help.Ansi.AUTO.string("@|faint " + LocalDateTime.now() + "|@ @|bold,blue  INFO|@ --- @|magenta [Sprout]|@ : Parsing " + em.getClassName()));
                eml.put( em.getClassName() , em);
            } catch (ParsingException | FileNotFoundException e){
                System.out.println(CommandLine.Help.Ansi.AUTO.string("@|faint " + LocalDateTime.now() + "|@ @|bold,red  ERROR|@ --- @|magenta [Sprout]|@ : Parsing failed for file " + entity.getName()));
            } catch (NotAnEntityException e){
                System.out.println(CommandLine.Help.Ansi.AUTO.string("@|faint " + LocalDateTime.now() + "|@ @|bold,yellow  WARNING|@ --- @|magenta [Sprout]|@ : No @Entity annotation in file " + entity.getName() + ", skipping.."));
            }
        }

        RepositoryGenerator repoGen = new RepositoryGenerator();
        ServiceGenerator serviceGen = new ServiceGenerator();
        DtoGenerator dtoGen = new DtoGenerator(eml);
        MapperGenerator mapperGen = new MapperGenerator();

        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold Pass 2/2 : Generating classes |@ \n"));

        //Generating files
        for(EntityMetadata em : eml.values()){

            try {
                repoGen.generate(em , repoMustache , defaultDir);
                System.out.println(CommandLine.Help.Ansi.AUTO.string("@|faint " + LocalDateTime.now() + "|@ @|bold,blue  INFO|@ --- @|magenta [Sprout]|@ : Generating Repository for " + em.getClassName()));
                dtoGen.generate(em , dtoMustache , defaultDir);
                System.out.println(CommandLine.Help.Ansi.AUTO.string("@|faint " + LocalDateTime.now() + "|@ @|bold,blue  INFO|@ --- @|magenta [Sprout]|@ : Generating DTO for " + em.getClassName()));
                mapperGen.generate(em , mapperMustache , defaultDir);
                System.out.println(CommandLine.Help.Ansi.AUTO.string("@|faint " + LocalDateTime.now() + "|@ @|bold,blue  INFO|@ --- @|magenta [Sprout]|@ : Generating Mapper for " + em.getClassName()));
                serviceGen.generate(em , serviceMustache , defaultDir);
                System.out.println(CommandLine.Help.Ansi.AUTO.string("@|faint " + LocalDateTime.now() + "|@ @|bold,blue  INFO|@ --- @|magenta [Sprout]|@ : Generating Service for " + em.getClassName()));

            } catch (IOException e) {
                throw new FileSystemException("");
            } catch (FileSystemException fsE){
                System.out.println(CommandLine.Help.Ansi.AUTO.string("@|faint " + LocalDateTime.now() + "|@ @|bold,red  ERROR|@ --- @|magenta [Sprout]|@ : File generation failed for class " + em.getClassName()));
            }
        }

        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold \nGeneration service ended successfully, shutting down ! |@ \n"));
    }
}
