package org.AmineSidki.parser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import org.AmineSidki.exception.NotAnEntityException;
import org.AmineSidki.exception.ParsingException;
import org.AmineSidki.model.EntityMetadata;
import org.AmineSidki.model.FieldMetadata;
import org.AmineSidki.model.IdMetadata;
import org.AmineSidki.util.ParserUtil;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class EntityParser implements SproutParser<EntityMetadata>{

    @Override
    public EntityMetadata parse(CompilationUnit cu , String entity) throws FileNotFoundException , ParsingException , NotAnEntityException {

        Optional<ClassOrInterfaceDeclaration> classDeclaration = cu.getClassByName(entity
                .substring(0 , entity.lastIndexOf(".java")));

        classDeclaration.orElseThrow(() -> new NotAnEntityException(""))
                .getAnnotations().stream().filter(f -> f.getNameAsString().equals("Entity"))
                .findFirst()
                .orElseThrow(() -> new NotAnEntityException(""));

        String packageName ,idQualifiedName;
        IdMetadata idType;

        List<FieldMetadata> fields = new ArrayList<>();

        //Get the full Package name: com.example.packageName.etc.entity
        packageName = ParserUtil.getPackageName(cu.getPackageDeclaration()
                .map(PackageDeclaration::getNameAsString)
                .orElseThrow(()-> new ParsingException("No package found !")));

        List<FieldDeclaration> fdList =  cu.findAll(FieldDeclaration.class);

        FieldDeclaration idFd = fdList.stream()
                .filter(f -> f.isAnnotationPresent("Id"))
                .findFirst()
                .orElseThrow(() -> new ParsingException("No @Id Annotation present in supposed entity "+ entity + "."));

        idQualifiedName = idFd.getVariable(0).getType().resolve().asReferenceType().getQualifiedName();

        // remove the package name if it belongs to java.lang.*
        if(idQualifiedName.startsWith("java.lang") && idQualifiedName.split(Pattern.quote(".")).length == 3){
            idQualifiedName = "";
        }

        idType = new IdMetadata(idFd.getElementType().toString() , idQualifiedName);

        fdList.forEach(f -> fields.addAll(ParserUtil.getFieldMetadata(f)));

        String className = entity.replaceAll(Pattern.quote(".java") , "");
        return new EntityMetadata(packageName , className , idType , fields);
    }
}
