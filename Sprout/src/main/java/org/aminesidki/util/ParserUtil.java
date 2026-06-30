package org.aminesidki.util;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.types.ResolvedType;
import org.aminesidki.enumeration.Association;
import org.aminesidki.model.*;

import java.util.*;

/**
 * Provides a multitude of services for the parsing layer such as :
 *  <ul>
    <li>Checking the presence of an annotation in a class</li>
 *  <li>Extracting the package name away from the entity package</li>
 *  <li>Extracting field metadata out of the field declaration</li>
 *  </ul>
 */
public class ParserUtil {
    public static boolean hasAnnotation(CompilationUnit cu , String entity, String annotation){
        ClassOrInterfaceDeclaration classDeclaration = cu.getClassByName(entity
                .substring(0 , entity.lastIndexOf(".java"))).orElse(null);

        if(classDeclaration == null){
            return false;
        }

        Object nullable = classDeclaration
                                .getAnnotations().stream().filter(f -> f.getNameAsString().equals(annotation))
                                .findFirst()
                                .orElse(null);

        return nullable != null;
    }

    public static String getPackageName(String entityPackageName) {
        if (entityPackageName.endsWith(".entity")) {
            return entityPackageName.substring(0, entityPackageName.lastIndexOf(".entity"));
        }
        return entityPackageName;
    }

    public static List<FieldMetadata> getFieldMetadata(FieldDeclaration fd) {
        Association association = getAssociation(fd);
        List<FieldMetadata> lfm = new ArrayList<>();
        String qualifierName, regularName;

        try{
            ResolvedType resolvedType = fd.getVariable(0).getType().resolve();

            if(!resolvedType.isReference()){
                qualifierName = "";
            }else{
                qualifierName = resolvedType.asReferenceType().getQualifiedName();
            }

        }catch(UnsolvedSymbolException e){
            qualifierName = "";
        }


        regularName = fd.getVariable(0).getTypeAsString();

        for(VariableDeclarator v :  fd.getVariables()){
            lfm.add(new FieldMetadata(new TypeMetadata(regularName , qualifierName),
                    v.getNameAsString(),
                    association));
        }

        return lfm;
    }

    private static Association getAssociation(FieldDeclaration fd) {
        final Set<String> annotations = Set.of("OneToOne","OneToMany","ManyToOne","ManyToMany");
        Association association = Association.DEFAULT;

        for(AnnotationExpr a : fd.getAnnotations())
            if(annotations.contains(a.getNameAsString())){
                association = switch (a.getNameAsString()) {
                    case "OneToOne" -> Association.ONE_TO_ONE;
                    case "OneToMany" -> Association.ONE_TO_MANY;
                    case "ManyToOne" -> Association.MANY_TO_ONE;
                    case "ManyToMany" -> Association.MANY_TO_MANY;
                    default -> association;
                };
            }
        return association;
    }

    public static String extractCollectionGenericType(String collection){
        return collection.substring(collection.indexOf("<") + 1 , collection.lastIndexOf(">"));
    }

}
