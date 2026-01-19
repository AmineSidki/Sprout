package org.AmineSidki.util;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import org.AmineSidki.model.EntityMetadata;
import org.AmineSidki.model.FieldMetadata;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class EntityParser {
    public static EntityMetadata parse(JavaParser parser, File entity) throws FileNotFoundException {
        ParseResult<CompilationUnit> pr = parser.parse(entity);
        CompilationUnit cu = pr.getResult().isPresent() ? pr.getResult().get() : null;

        if(cu == null){
            throw new RuntimeException("An error occurred whilst parsing file : " + entity.getName());
        }

        String idType,packageName ;

        // Leave this one for when you can implement Dto generation smh...
        List<FieldMetadata> fields = new ArrayList<>();

        //Get the full Package name: com.example.packageName.etc.entity
        packageName = ParserUtil.getPackageName(cu.getPackageDeclaration()
                .map(PackageDeclaration::getNameAsString)
                .orElseThrow(()-> new RuntimeException("No package found !")));

        List<FieldDeclaration> fdList =  cu.findAll(FieldDeclaration.class);

        FieldDeclaration idFd = fdList.stream()
                .filter(f -> f.isAnnotationPresent("Id"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No @Id Annotation present in supposed entity "+ entity.getName() + "."));

        idType = idFd.getElementType().toString();

        fdList.forEach(f -> fields.addAll(ParserUtil.getFieldMetadata(f)));

        //If it gets to this point then the parsing was successful.
        //TODO: Don't forget to implement other multiplicity annotations' support, but that will be important once you implement DTOs

        String className = entity.getName().replaceAll(Pattern.quote(".java") , "");
        return new EntityMetadata(packageName , className , idType , fields);
    }
}
