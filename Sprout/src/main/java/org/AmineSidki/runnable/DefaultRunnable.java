package org.AmineSidki.runnable;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.AmineSidki.exception.FileSystemException;
import org.AmineSidki.exception.NotAnEntityException;
import org.AmineSidki.exception.ParsingException;
import org.AmineSidki.generator.DependencyGenerator.MapperDependencyGenerator;
import org.AmineSidki.generator.FileGenerator.DtoGenerator;
import org.AmineSidki.generator.FileGenerator.MapperGenerator;
import org.AmineSidki.generator.FileGenerator.RepositoryGenerator;
import org.AmineSidki.generator.FileGenerator.ServiceGenerator;
import org.AmineSidki.generator.ImportGenerator.DtoImportGenerator;
import org.AmineSidki.generator.ImportGenerator.MapperImportGenerator;
import org.AmineSidki.generator.ImportGenerator.RepositoryImportGenerator;
import org.AmineSidki.generator.ImportGenerator.ServiceImportGenerator;
import org.AmineSidki.model.EntityMetadata;
import org.AmineSidki.model.HelperMetadata;
import org.AmineSidki.parser.HelperParser;
import org.AmineSidki.util.BannerPrinter;
import org.AmineSidki.parser.EntityParser;
import org.AmineSidki.util.ParserUtil;
import picocli.CommandLine;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

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

        //Takes the first file it finds and gets its package
        File firstEntity = Arrays.stream(files)
                .filter(File::isFile)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No entity files found to parse!"));

        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold Pass 1/3 : Computing project root |@\n"));

        File calculatedSourceRoot = ParserUtil.calculateProjectRootDirectory(firstEntity , new JavaParser());

        System.out.println(CommandLine.Help.Ansi.AUTO.string(
                "@|faint " + LocalDateTime.now() + "|@ @|bold,blue  INFO|@ --- @|magenta [Sprout]|@ : Successfully resolved directory"
        ));

        System.out.println(CommandLine.Help.Ansi.AUTO.string(
                "@|faint " + LocalDateTime.now() + "|@ @|bold,blue  INFO|@ --- @|magenta [Sprout]|@ : @|faint " + calculatedSourceRoot.getAbsolutePath() + "|@\n"
        ));

        //Type solver configuration for Java parser to actually recognize types
        ThreadLocal<JavaParser> parser = ThreadLocal.withInitial(() -> {
            CombinedTypeSolver typeSolver = new CombinedTypeSolver();
            typeSolver.add(new ReflectionTypeSolver());
            typeSolver.add(new JavaParserTypeSolver(calculatedSourceRoot));

            JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);

            ParserConfiguration parserConfig = new ParserConfiguration();
            parserConfig.setSymbolResolver(symbolSolver);
            return new JavaParser(parserConfig);
        });
        MustacheFactory mf = new DefaultMustacheFactory();

        //TODO: Change this into a generator list

        //Compiling templates
        Mustache repoMustache = mf.compile("templates/RepositoryTemplate.mustache");
        Mustache serviceMustache = mf.compile("templates/ServiceTemplate.mustache");
        Mustache dtoMustache = mf.compile("templates/DtoTemplate.mustache");
        Mustache mapperMustache = mf.compile("templates/MapperTemplate.mustache");

        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold Pass 2/3 : Parsing Java |@ \n"));

        ConcurrentHashMap<String , EntityMetadata> emm = new ConcurrentHashMap<>();
        ConcurrentHashMap<String , HelperMetadata> hmm = new ConcurrentHashMap<>();

        ThreadLocal<EntityParser> entityParser = ThreadLocal.withInitial(EntityParser::new);
        ThreadLocal<HelperParser> helperParser = ThreadLocal.withInitial(HelperParser::new);

        //Parsing the entirety of the files in the directory
        Arrays.stream(files).parallel().forEach(
                entity -> {
                    if(!entity.isFile()){
                        return;
                    }

                    CompilationUnit cu = null;
                    try {
                        //Parsing java --> AST
                        ParseResult<CompilationUnit> pr = parser.get().parse(entity);
                        cu = pr.getResult().orElseThrow(() -> new ParsingException(""));

                        //Parsing AST --> EntityMetadata
                        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|faint " + LocalDateTime.now() + "|@ @|bold,blue  INFO|@ --- @|magenta [Sprout]|@ : Parsing " + entity.getName()));
                        EntityMetadata em = entityParser.get().parse(cu , entity.getName());
                        emm.put( em.className() , em);

                    } catch (NotAnEntityException e){
                        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|faint " + LocalDateTime.now() + "|@ @|bold,yellow  WARN|@ --- @|magenta [Sprout]|@ : No @Entity annotation in file " + entity.getName()));
                        try{
                            HelperMetadata hm = helperParser.get().parse(cu , entity.getName());
                            hmm.put(hm.className() , hm);
                        }catch(ParsingException | FileNotFoundException ee){
                            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|faint " + LocalDateTime.now() + "|@ @|bold,red  ERROR|@ --- @|magenta [Sprout]|@ : Parsing failed for file " + entity.getName()));
                        }
                    } catch (ParsingException | FileNotFoundException e){
                        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|faint " + LocalDateTime.now() + "|@ @|bold,red  ERROR|@ --- @|magenta [Sprout]|@ : Parsing failed for file " + entity.getName()));
                    }
                }
        );

        MapperDependencyGenerator mapperDependencyGen = new MapperDependencyGenerator();

        RepositoryGenerator repoGen = new RepositoryGenerator();
        ServiceGenerator serviceGen = new ServiceGenerator();
        DtoGenerator dtoGen = new DtoGenerator(emm, hmm);
        MapperGenerator mapperGen = new MapperGenerator(emm, hmm , mapperDependencyGen);

        DtoImportGenerator dtoImportGen = new DtoImportGenerator();
        MapperImportGenerator mapperImportGen = new MapperImportGenerator();
        RepositoryImportGenerator repoImportGen = new RepositoryImportGenerator();
        ServiceImportGenerator serviceImportGen = new ServiceImportGenerator();

        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold \nPass 3/3 : Generating classes |@ \n"));


        //Generating files
        for(EntityMetadata em : emm.values()){

            try {
                repoGen.generate(repoImportGen ,em , repoMustache , defaultDir);
                System.out.println(CommandLine.Help.Ansi.AUTO.string("@|faint " + LocalDateTime.now() + "|@ @|bold,blue  INFO|@ --- @|magenta [Sprout]|@ : Generating Repository for " + em.className()));
                dtoGen.generate(dtoImportGen, em , dtoMustache , defaultDir);
                System.out.println(CommandLine.Help.Ansi.AUTO.string("@|faint " + LocalDateTime.now() + "|@ @|bold,blue  INFO|@ --- @|magenta [Sprout]|@ : Generating DTO for " + em.className()));
                mapperGen.generate(mapperImportGen, em , mapperMustache , defaultDir);
                System.out.println(CommandLine.Help.Ansi.AUTO.string("@|faint " + LocalDateTime.now() + "|@ @|bold,blue  INFO|@ --- @|magenta [Sprout]|@ : Generating Mapper for " + em.className()));
                serviceGen.generate(serviceImportGen ,em , serviceMustache , defaultDir);
                System.out.println(CommandLine.Help.Ansi.AUTO.string("@|faint " + LocalDateTime.now() + "|@ @|bold,blue  INFO|@ --- @|magenta [Sprout]|@ : Generating Service for " + em.className()));

            } catch (IOException e) {
                throw new FileSystemException("");
            } catch (FileSystemException fsE){
                System.out.println(CommandLine.Help.Ansi.AUTO.string("@|faint " + LocalDateTime.now() + "|@ @|bold,red  ERROR|@ --- @|magenta [Sprout]|@ : File generation failed for class " + em.className()));
            }
        }

        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold \nGeneration service ended successfully, shutting down ! |@ \n"));
    }
}
