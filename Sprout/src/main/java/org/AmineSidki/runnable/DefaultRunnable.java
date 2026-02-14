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
import org.AmineSidki.exception.NotAnEntityException;
import org.AmineSidki.exception.ParsingException;
import org.AmineSidki.generator.DependencyGenerator.MapperDependencyGenerator;
import org.AmineSidki.generator.FileGenerator.*;
import org.AmineSidki.generator.GenerationScheduler;
import org.AmineSidki.generator.ImportsGenerator.DtoImportsGenerator;
import org.AmineSidki.generator.ImportsGenerator.GenericImportsGenerator;
import org.AmineSidki.generator.ImportsGenerator.MapperImportsGenerator;
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

@CommandLine.Command(name="default" , version = "1.4" , description = "Sprout scaffolding engine")
public class DefaultRunnable implements Runnable{

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    @CommandLine.Option(names = "--dir")
    private String defaultDir = ".";

    static class DependentFlags {
        @CommandLine.Option(names = {"-p" , "--partial"}, required = true, description = "Partial generation")
        boolean pFlag;

        @CommandLine.Option(names = {"-r" , "--repository"}) boolean rFlag;
        @CommandLine.Option(names = {"-d" , "--dto"}) boolean dFlag;
        @CommandLine.Option(names = {"-s" , "--service"}) boolean sFlag;
        @CommandLine.Option(names = {"-m" , "--mapper"}) boolean mFlag;
        @CommandLine.Option(names = {"-c" , "--controller"}) boolean cFlag;
        @CommandLine.Option(names = {"-e" , "--exception"}) boolean eFlag;
    }

    @CommandLine.ArgGroup(exclusive = false)
    DependentFlags pGroup;

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

        //Compiling templates
        Mustache repoMustache =
                pGroup == null || pGroup.rFlag ?
                        mf.compile("templates/RepositoryTemplate.mustache") : null;
        Mustache serviceMustache =
                pGroup == null || pGroup.sFlag ?
                        mf.compile("templates/ServiceTemplate.mustache") : null;
        Mustache dtoMustache =
                pGroup == null || pGroup.dFlag ?
                        mf.compile("templates/DtoTemplate.mustache") : null;
        Mustache mapperMustache =
                pGroup == null || pGroup.mFlag ?
                        mf.compile("templates/MapperTemplate.mustache") : null;
        Mustache controllerMustache =
                pGroup == null || pGroup.cFlag ?
                        mf.compile("templates/ControllerTemplate.mustache") : null;
        Mustache exceptionMustache =
                pGroup == null || pGroup.eFlag ?
                        mf.compile("templates/ExceptionTemplate.mustache") : null;

        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold Pass 2/3 : Parsing Java |@ \n"));

        ConcurrentHashMap<String , EntityMetadata> emm = new ConcurrentHashMap<>();
        ConcurrentHashMap<String , HelperMetadata> hmm = new ConcurrentHashMap<>();

        ThreadLocal<EntityParser> entityParser = ThreadLocal.withInitial(EntityParser::new);
        ThreadLocal<HelperParser> helperParser = ThreadLocal.withInitial(HelperParser::new);

        //Parsing
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
                            if(cu == null){
                                throw new ParsingException("");
                            }

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

        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold \nPass 3/3 : Generating classes |@ \n"));
        GenerationScheduler scheduler = new GenerationScheduler(defaultDir , emm);

        //I hate this, but it seems like the only option for now :(
        if(pGroup == null || pGroup.cFlag || pGroup.rFlag || pGroup.sFlag){

            GenericImportsGenerator genericImportsGenerator = new GenericImportsGenerator();

            if(pGroup == null || pGroup.rFlag){
                scheduler.add(new RepositoryGenerator(genericImportsGenerator, emm, hmm),
                        repoMustache,
                        CommandLine.Help.Ansi.AUTO.string("@|faint " + LocalDateTime.now() + "|@ @|bold,blue  INFO|@ --- @|magenta [Sprout]|@ : Generating repository for "));
            }
            if(pGroup == null || pGroup.sFlag){
                scheduler.add(new ServiceGenerator(genericImportsGenerator, emm, hmm),
                        serviceMustache,
                        CommandLine.Help.Ansi.AUTO.string("@|faint " + LocalDateTime.now() + "|@ @|bold,blue  INFO|@ --- @|magenta [Sprout]|@ : Generating service for "));
            }
            if(pGroup == null || pGroup.cFlag){
                scheduler.add(new ControllerGenerator(genericImportsGenerator, emm, hmm),
                        controllerMustache,
                        CommandLine.Help.Ansi.AUTO.string("@|faint " + LocalDateTime.now() + "|@ @|bold,blue  INFO|@ --- @|magenta [Sprout]|@ : Generating controller for "));
            }
        }
        if(pGroup == null || pGroup.mFlag){
            MapperImportsGenerator mapperImportGen = new MapperImportsGenerator();
            MapperDependencyGenerator mapperDependencyGen = new MapperDependencyGenerator();
           scheduler.add( new MapperGenerator(mapperImportGen , mapperDependencyGen, emm, hmm),
                   mapperMustache,
                   CommandLine.Help.Ansi.AUTO.string("@|faint " + LocalDateTime.now() + "|@ @|bold,blue  INFO|@ --- @|magenta [Sprout]|@ : Generating mapper for "));
        }
        if(pGroup == null || pGroup.dFlag){
            DtoImportsGenerator dtoImportGen = new DtoImportsGenerator();
            scheduler.add(new DtoGenerator(dtoImportGen, emm, hmm),
                    dtoMustache,
                    CommandLine.Help.Ansi.AUTO.string("@|faint " + LocalDateTime.now() + "|@ @|bold,blue  INFO|@ --- @|magenta [Sprout]|@ : Generating DTO for "));
        }
        if(pGroup == null || pGroup.eFlag){
            scheduler.add(new ExceptionGenerator(),
                    exceptionMustache,
                    CommandLine.Help.Ansi.AUTO.string("@|faint " + LocalDateTime.now() + "|@ @|bold,blue  INFO|@ --- @|magenta [Sprout]|@ : Generating exception for "));
        }

        //Generation
        scheduler.generate();

        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold \nGeneration service ended successfully, shutting down ! |@ \n"));
    }
}
