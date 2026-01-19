package org.AmineSidki.util;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AnnotationExpr;
import org.AmineSidki.enumeration.Association;
import org.AmineSidki.model.FieldMetadata;

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.List;

public class ParserUtil {
    public static String getPackageName(String entityPackageName) {
        if (entityPackageName.endsWith(".entity")) {
            return entityPackageName.substring(0, entityPackageName.lastIndexOf(".entity"));
        }
        return entityPackageName;
    }

    public static List<FieldMetadata> getFieldMetadata(FieldDeclaration fd) {
        final String annotations = "OneToOne;OneToMany;ManyToOne;ManyToMany";
        Association association = Association.DEFAULT;

        for(AnnotationExpr a : fd.getAnnotations())
            if(annotations.contains(a.getNameAsString())){
                return new ArrayList<>();
/*
                I will uncomment this block when I am ready to tackle the multiplicities :)
                switch (a.getNameAsString()) {
                    case "OneToOne":
                        association = Association.ONE_TO_ONE;
                        break;
                    case "OneToMany":
                        association = Association.ONE_TO_MANY;
                        break;
                    case "ManyToOne":
                        association = Association.MANY_TO_ONE;
                        break;
                    case "ManyToMany":
                        association = Association.MANY_TO_MANY;
                        break;
                }
*/
            }

        List<FieldMetadata> lfm = new ArrayList<>();

        for(VariableDeclarator v :  fd.getVariables()){
            lfm.add(new FieldMetadata(v.getTypeAsString(), v.getNameAsString(), association));
        }

        return lfm;
    }
}
