package org.aminesidki.util;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.nodeTypes.NodeWithName;
import org.aminesidki.exception.FileSystemException;

import java.io.File;
import java.util.Arrays;

public class ProjectRootDirectoryResolver {
    public static File calculateProjectRootDirectory(File[] files , JavaParser parser){
        try{
            //Takes the first file it finds and gets its package
            File firstEntity = Arrays.stream(files)
                    .filter(File::isFile)
                    .findFirst()
                    .orElseThrow(() -> new FileSystemException("No entity files found to parse!"));

            ParseResult<CompilationUnit> result = parser.parse(firstEntity);
            if (result.getResult().isPresent()) {
                CompilationUnit cu = result.getResult().get();
                String packageName = cu.getPackageDeclaration()
                        .map(NodeWithName::getNameAsString)
                        .orElse("");

                File root = firstEntity.getAbsoluteFile().getParentFile();

                if (!packageName.isEmpty()) {
                    String[] parts = packageName.split("\\.");
                    for (String part : parts) {
                        root = root.getParentFile();
                        if (root == null) {
                            // Safety check: if we hit the file system root, abort and use default
                            return new File(".");
                        }
                    }
                    root = root.getParentFile();
                    if (root == null) {
                        // Safety check: if we hit the file system root, abort and use default
                        return new File(".");
                    }
                }
                return root;
            }
            throw new RuntimeException("An error occurred whilst computing project root directory !");
        }catch (Exception e){
            throw new RuntimeException(e.getMessage() + "\n" + e);
        }
    }
}
