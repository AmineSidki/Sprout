package org.AmineSidki.util;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AnnotationExpr;
import org.AmineSidki.enumeration.Association;
import org.AmineSidki.model.EntityMetadata;
import org.AmineSidki.model.FieldMetadata;
import org.AmineSidki.model.HelperMetadata;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
            }

        List<FieldMetadata> lfm = new ArrayList<>();

        for(VariableDeclarator v :  fd.getVariables()){
            lfm.add(new FieldMetadata(v.getTypeAsString(), v.getNameAsString(), association));
        }

        return lfm;
    }

    public static String extractCollectionGenericType(String collection){
        return collection.substring(collection.indexOf("<") + 1 , collection.lastIndexOf(">"));
    }

    //Adapts fields with associations into their Java counterpart
    public static List<FieldMetadata> mapToDtoField(EntityMetadata em , Map<String , EntityMetadata> persistenceMap , Map<String , HelperMetadata> helperMap){
        List<FieldMetadata> output = new ArrayList<>();

        for(FieldMetadata fm : em.getFields()){
            FieldMetadata f = fm;
            if (!fm.getAssociation().equals(Association.DEFAULT)) {
                String idType , fieldType;
                switch (fm.getAssociation()) {
                    case ONE_TO_MANY:
                    case MANY_TO_MANY:
                        String typeName = extractCollectionGenericType(fm.getType());
                        if(persistenceMap.containsKey(typeName)){
                            idType = persistenceMap.get(typeName).getIdType().getClassName();
                        }else if(helperMap.containsKey(typeName)){
                            idType = helperMap.get(typeName).getClassName();
                        }else{
                            idType = "Object";
                        }


                        fieldType = "Set<" + idType + ">";
                        break;

                    case ONE_TO_ONE:
                    case MANY_TO_ONE:
                        if(persistenceMap.containsKey(fm.getType())){
                            idType = persistenceMap.get(fm.getType()).getIdType().getClassName();
                        }else if(helperMap.containsKey(fm.getType())){
                            idType = helperMap.get(fm.getType()).getClassName();
                        }else{
                            idType = "Object";
                        }

                        fieldType = idType;
                        break;

                    default:
                        fieldType = "Object";
                }

                f = new FieldMetadata(fieldType , fm.getName() , fm.getAssociation());
            }

            output.add(f);
        }

        return output;
    }
}
