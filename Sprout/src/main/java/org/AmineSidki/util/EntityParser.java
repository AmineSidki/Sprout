package org.AmineSidki.util;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import org.AmineSidki.exception.NotAnEntityException;
import org.AmineSidki.exception.ParsingException;
import org.AmineSidki.model.EntityMetadata;
import org.AmineSidki.model.FieldMetadata;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class EntityParser {
    public static EntityMetadata parse(JavaParser parser, File entity) throws FileNotFoundException , ParsingException , NotAnEntityException {
        ParseResult<CompilationUnit> pr = parser.parse(entity);
        CompilationUnit cu = pr.getResult().orElseThrow(() -> new ParsingException(""));

        Optional<ClassOrInterfaceDeclaration> classDeclaration = cu.getClassByName(entity
                .getName()
                .substring(0 , entity.getName().lastIndexOf(".java")));

        classDeclaration.orElseThrow(() -> new NotAnEntityException(""))
                .getAnnotations().stream().filter(f -> f.getNameAsString().equals("Entity"))
                .findFirst()
                .orElseThrow(() -> new NotAnEntityException(""));

        String idType,packageName ;

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

        String className = entity.getName().replaceAll(Pattern.quote(".java") , "");
        return new EntityMetadata(packageName , className , idType , fields);
    }
}
