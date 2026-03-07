package org.aminesidki.handler;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import lombok.RequiredArgsConstructor;
import org.aminesidki.exception.ParsingException;
import org.aminesidki.model.EntityMetadata;
import org.aminesidki.model.HelperMetadata;
import org.aminesidki.parser.EntityParser;
import org.aminesidki.parser.HelperParser;
import org.aminesidki.provider.JavaParserProvider;
import org.aminesidki.util.ParserUtil;
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
    private final JavaParserProvider javaParserProvider;

    public void parse(File[] files){
        //Resource initialization
        final ThreadLocal<EntityParser> entityParser = ThreadLocal.withInitial(EntityParser::new);
        final ThreadLocal<HelperParser> helperParser = ThreadLocal.withInitial(HelperParser::new);
        final ThreadLocal<JavaParser> parser = ThreadLocal.withInitial(javaParserProvider::provide);

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
                        if(ParserUtil.hasAnnotation(cu, entity.getName(), "Entity")){
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
