package org.aminesidki.parser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import java.io.FileNotFoundException;
import java.util.regex.Pattern;
import org.aminesidki.exception.ParsingException;
import org.aminesidki.model.HelperMetadata;
import org.aminesidki.util.ParserUtil;

/**
 * Specific helper class parsing implementation for {@link org.aminesidki.parser.SproutParser}
 * interface
 */
public class HelperParser implements SproutParser<HelperMetadata> {

  @Override
  public HelperMetadata parse(CompilationUnit cu, String entity, String entityPackageName)
      throws FileNotFoundException, ParsingException {
    // Get the full Package name: com.example.packageName.etc.entity
    String packageName =
        ParserUtil.getPackageName(
            cu.getPackageDeclaration()
                .map(PackageDeclaration::getNameAsString)
                .orElseThrow(() -> new ParsingException("No package found !")),
            "." + entityPackageName);

    String className = entity.replaceAll(Pattern.quote(".java"), "");
    return new HelperMetadata(packageName, className);
  }
}
