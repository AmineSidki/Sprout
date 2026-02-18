package org.AmineSidki.util;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithName;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.types.ResolvedType;
import org.AmineSidki.enumeration.Association;
import org.AmineSidki.exception.FileSystemException;
import org.AmineSidki.model.*;

import java.io.File;
import java.util.*;

public class ParserUtil {
    public static boolean isEntity(CompilationUnit cu , String entity){
        ClassOrInterfaceDeclaration classDeclaration = cu.getClassByName(entity
                .substring(0 , entity.lastIndexOf(".java"))).orElse(null);

        if(classDeclaration == null){
            return false;
        }

        Object nullable = classDeclaration
                                .getAnnotations().stream().filter(f -> f.getNameAsString().equals("Entity"))
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

    public static String extractCollectionGenericType(String collection){
        return collection.substring(collection.indexOf("<") + 1 , collection.lastIndexOf(">"));
    }

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
            throw new RuntimeException("An error occurred whilst computing project root directory !");
        }
    }

    //Adapts fields with associations into their Java counterpart
    public static List<FieldMetadata> mapToDtoField(EntityMetadata em , Map<String , EntityMetadata> persistenceMap , Map<String , HelperMetadata> helperMap){
        List<FieldMetadata> output = new ArrayList<>();

        for(FieldMetadata fm : em.fields()){
            FieldMetadata f = fm;
            if (!fm.association().equals(Association.DEFAULT)) {
                TypeMetadata idType ;
                String fieldType = "";

                switch (fm.association()) {
                    case ONE_TO_MANY:
                    case MANY_TO_MANY:
                        String typeName = extractCollectionGenericType(fm.type().getRegularName());
                        if(persistenceMap.containsKey(typeName)){
                            idType = persistenceMap.get(typeName).id().type();
                        }else if(helperMap.containsKey(typeName)){
                            idType = new TypeMetadata(helperMap.get(typeName).className() ,
                                    helperMap.get(typeName).packageName() + "." + helperMap.get(typeName).className()) ;
                        }else{
                            idType = new TypeMetadata(typeName , "");
                        }


                        fieldType = "List<" + idType.getRegularName() + ">";
                        break;

                    case ONE_TO_ONE:
                    case MANY_TO_ONE:
                        if(persistenceMap.containsKey(fm.type().getRegularName())){
                            idType = persistenceMap.get(fm.type().getRegularName()).id().type();
                        }else if(helperMap.containsKey(fm.type().getRegularName())){
                            idType = new TypeMetadata(helperMap.get(fm.type().getRegularName()).className() ,
                                    helperMap.get(fm.type().getRegularName()).packageName() + "." + helperMap.get(fm.type().getRegularName()).className());
                        }else{
                            idType = fm.type();
                        }

                        fieldType = idType.getRegularName();
                        break;

                    default:
                        idType = fm.type();
                }

                f = new FieldMetadata(new TypeMetadata(fieldType , idType.getFullQualifiedName()) ,
                        fm.name() , fm.association());
            }

            output.add(f);
        }

        return output;
    }
}
