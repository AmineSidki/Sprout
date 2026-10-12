package org.aminesidki.provider;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import java.io.File;
import lombok.RequiredArgsConstructor;

/** Specific JavaParser implementation of the <code>SproutProvider</code> interface */
@RequiredArgsConstructor
public class JavaParserProvider implements SproutProvider<JavaParser> {
  private final File calculatedRootString;

  @Override
  public JavaParser provide() {
    CombinedTypeSolver typeSolver = new CombinedTypeSolver();
    typeSolver.add(new ReflectionTypeSolver());
    typeSolver.add(new JavaParserTypeSolver(calculatedRootString));

    JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);

    ParserConfiguration parserConfig = new ParserConfiguration();
    parserConfig.setSymbolResolver(symbolSolver);
    return new JavaParser(parserConfig);
  }
}
