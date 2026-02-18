package org.AmineSidki.parser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.types.ResolvedType;
import org.AmineSidki.enumeration.Association;
import org.AmineSidki.exception.ParsingException;
import org.AmineSidki.model.EntityMetadata;
import org.AmineSidki.model.FieldMetadata;
import org.AmineSidki.model.TypeMetadata;
import org.AmineSidki.util.ParserUtil;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class EntityParser implements SproutParser<EntityMetadata>{

    @Override
    public EntityMetadata parse(CompilationUnit cu , String entity) throws FileNotFoundException , ParsingException{

        String packageName ,idQualifiedName;
        TypeMetadata idType;

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

        try{
            ResolvedType idResolved = idFd.getVariable(0).getType().resolve();
            if(idResolved.isReference()){
                idQualifiedName = idResolved.asReferenceType().getQualifiedName();
            }else{
                idQualifiedName = "";
            }

            // remove the package name if it belongs to java.lang.*
            if(idQualifiedName.startsWith("java.lang.") && idQualifiedName.substring(10).contains(".")){
                idQualifiedName = "";
            }
        }catch(UnsolvedSymbolException e){
            idQualifiedName = "";
        }

        idType = new TypeMetadata(idFd.getElementType().toString() , idQualifiedName);
        FieldMetadata idField = new FieldMetadata(idType , idFd.getVariable(0).getNameAsString() , Association.DEFAULT);

        fdList.forEach(f -> fields.addAll(ParserUtil.getFieldMetadata(f)));

        String className = entity.replaceAll(Pattern.quote(".java") , "");
        return new EntityMetadata(packageName , className , idField , fields);
    }
}
