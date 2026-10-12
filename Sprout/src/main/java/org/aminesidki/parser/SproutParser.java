package org.aminesidki.parser;

import com.github.javaparser.ast.CompilationUnit;
import java.io.FileNotFoundException;
import org.aminesidki.exception.ParsingException;

/**
 * Parses compilation unit into a specific data structure to make use of the extracted data
 *
 * @param <T> Output of the parsing
 */
public interface SproutParser<T> {
  T parse(CompilationUnit cu, String entity, String entityPackageName)
      throws FileNotFoundException, ParsingException;
}
