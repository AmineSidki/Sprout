package org.aminesidki.parser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.types.ResolvedType;
import org.aminesidki.enumeration.Association;
import org.aminesidki.exception.ParsingException;
import org.aminesidki.model.EntityMetadata;
import org.aminesidki.model.FieldMetadata;
import org.aminesidki.model.TypeMetadata;
import org.aminesidki.util.ParserUtil;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Specific entity parsing implementation for {@link org.aminesidki.parser.SproutParser} interface
 */
public class EntityParser implements SproutParser<EntityMetadata>{

    @Override
    public EntityMetadata parse(CompilationUnit cu , String entity) throws FileNotFoundException , ParsingException{

        String packageName ,idQualifiedName;
        TypeMetadata idType;

        List<FieldMetadata> fields = new ArrayList<>();
        List<FieldMetadata> lightFields = new ArrayList<>();

        //Get the full Package name: com.example.packageName.etc.entity
        packageName = ParserUtil.getPackageName(cu.getPackageDeclaration()
                .map(PackageDeclaration::getNameAsString)
                .orElseThrow(()-> new ParsingException("No package found !")));

        List<FieldDeclaration> fdList =  cu.findAll(FieldDeclaration.class);
        FieldDeclaration idFd;
        Optional<FieldDeclaration> optIdFd = fdList.stream()
                .filter(f -> f.isAnnotationPresent("Id"))
                .findFirst();

        if(optIdFd.isEmpty()){
            optIdFd = fdList.stream()
                    .filter(f -> f.isAnnotationPresent("EmbeddedId"))
                    .findFirst();

            if(optIdFd.isEmpty()) throw new ParsingException("No @Id or @EmbeddedId Annotation present in supposed entity "+ entity + ".");
        }

        idFd = optIdFd.get();
//                .orElseThrow(() -> new ParsingException("No @Id Annotation present in supposed entity "+ entity + "."));

        //Check for @SproutCached
        boolean cached = ParserUtil.hasAnnotation(cu, entity, "SproutCached");

        //Check for @SproutProjection
        boolean projection = ParserUtil.hasAnnotation(cu, entity, "SproutProjection");

        //Check for @SproutPaginated
        boolean paginated = ParserUtil.hasAnnotation(cu, entity, "SproutPaginated");

        //I honestly don't like this one since, if not used correctly, it will produce non-functional code.
        //Check for @SproutIgnore : Parses and accounts for entity in the Context, but doesn't generate any code for it. it basically acts as a Helper with additional data
        boolean ignored = ParserUtil.hasAnnotation(cu, entity, "SproutIgnore");

        //Check for @SproutLargeDataField : Sprout Large data field, excludes annotated fields from generation in the LightDTO
        Set<FieldDeclaration> lightFieldsDeclaration = fdList.stream()
                .filter(f -> !f.isAnnotationPresent("SproutLargeDataField"))
                .collect(Collectors.toSet());

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

        fdList.forEach(f -> {
            List<FieldMetadata> extractedFields = ParserUtil.getFieldMetadata(f);
            fields.addAll(extractedFields);
            if(lightFieldsDeclaration.contains(f)){
                lightFields.addAll(extractedFields);
            }
        });

        String className = entity.replaceAll(Pattern.quote(".java") , "");
        return new EntityMetadata(packageName , className , idField , projection , paginated , ignored , cached , fields , lightFields);
    }
}
