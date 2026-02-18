package org.AmineSidki.handler;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import lombok.RequiredArgsConstructor;
import org.AmineSidki.exception.ParsingException;
import org.AmineSidki.model.EntityMetadata;
import org.AmineSidki.model.HelperMetadata;
import org.AmineSidki.parser.EntityParser;
import org.AmineSidki.parser.HelperParser;
import org.AmineSidki.util.ParserUtil;
import picocli.CommandLine;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;

@RequiredArgsConstructor
public class ParsingHandler {
    private final Map<String, EntityMetadata> emm;
    private final Map<String, HelperMetadata> hmm;
    private final File calculatedSourceRoot;

    public void parse(File[] files){
        //Resource initialization
        final ThreadLocal<EntityParser> entityParser = ThreadLocal.withInitial(EntityParser::new);
        final ThreadLocal<HelperParser> helperParser = ThreadLocal.withInitial(HelperParser::new);
        final ThreadLocal<JavaParser> parser = ThreadLocal.withInitial(() -> {
            CombinedTypeSolver typeSolver = new CombinedTypeSolver();
            typeSolver.add(new ReflectionTypeSolver());
            typeSolver.add(new JavaParserTypeSolver(calculatedSourceRoot));

            JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);

            ParserConfiguration parserConfig = new ParserConfiguration();
            parserConfig.setSymbolResolver(symbolSolver);
            return new JavaParser(parserConfig);
        });

        Arrays.stream(files).parallel().forEach(
                entity -> {
                    if(!entity.isFile() || !entity.getName().endsWith(".java")){
                        return;
                    }

                    try {
                        //Parsing java --> AST
                        ParseResult<CompilationUnit> pr = parser.get().parse(entity);
                        CompilationUnit cu = pr.getResult().orElseThrow(() -> new ParsingException("Java Parsing failed for file :" + entity.getName()));

                        //Parsing AST --> EntityMetadata
                        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|faint " + LocalDateTime.now() + "|@ @|bold,blue  INFO|@ --- @|magenta [Sprout]|@ : Parsing " + entity.getName()));
                        if(ParserUtil.isEntity(cu, entity.getName())){
                            EntityMetadata em = entityParser.get().parse(cu , entity.getName());
                            emm.put(em.className() , em);
                        }else{
                            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|faint " + LocalDateTime.now() + "|@ @|bold,yellow  WARN|@ --- @|magenta [Sprout]|@ : No @Entity annotation in file " + entity.getName()));
                            HelperMetadata hm = helperParser.get().parse(cu , entity.getName());
                            hmm.put(hm.className() , hm);
                        }
                    } catch (ParsingException | FileNotFoundException e){
                        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|faint " + LocalDateTime.now() + "|@ @|bold,red  ERROR|@ --- @|magenta [Sprout]|@ : Parsing failed for file " + entity.getName()));
                        e.printStackTrace();
                    }
                }
        );
    }
}
