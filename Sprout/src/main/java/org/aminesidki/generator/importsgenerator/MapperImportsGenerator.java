package org.aminesidki.generator.importsgenerator;

import org.aminesidki.enumeration.Association;
import org.aminesidki.generator.SproutImportsGenerator;
import org.aminesidki.model.EntityMetadata;
import org.aminesidki.model.FieldMetadata;
import org.aminesidki.model.HelperMetadata;
import org.aminesidki.model.TypeMetadata;
import org.aminesidki.util.ParserUtil;

import java.util.HashSet;
import java.util.Map;

public class MapperImportsGenerator implements SproutImportsGenerator {
    @Override
    public HashSet<String> generate(EntityMetadata entityMetadata, Map<String, EntityMetadata> persistenceMap , Map<String, HelperMetadata> helperMap ){
        HashSet<String> imports = new HashSet<>();

        for(FieldMetadata fm : entityMetadata.fields()){
            TypeMetadata fieldType = fm.type();

            if(fieldType.fullQualifiedName().isEmpty()) continue;

            if(fieldType.regularName().startsWith("Set<")) imports.add("java.util.Set");
            if(fieldType.regularName().startsWith("List<")) imports.add("java.util.List");

            //In case it is an association, add the destination class(if entity add its Id)
            if(!fm.association().equals(Association.DEFAULT)){
                if(fm.association().equals(Association.ONE_TO_MANY) || fm.association().equals(Association.MANY_TO_MANY)){
                    fieldType = new TypeMetadata(ParserUtil.extractCollectionGenericType(fieldType.regularName()),
                            fieldType.fullQualifiedName());
                }

                //check if the type is an entity
                EntityMetadata entity = persistenceMap.get(fieldType.regularName());

                if(entity != null){
                    if(entity.id().type().isImportNeeded()){
                        imports.add(entity.id().type().fullQualifiedName());
                    }

                    imports.add(entity.packageName() + ".entity." +entity.className());
                    imports.add(entity.packageName() + ".repository." + entity.className() + "Repository");
                }else{
                    //if it isn't an entity then it must be a helper
                    HelperMetadata helper = helperMap.get(fieldType.regularName());
                    if(helper == null){
                        imports.add(fieldType.fullQualifiedName());
                        continue;
                    }
                    imports.add(helper.packageName() + "." + helper.className());
                }
            }else{
                // check if their type needs to be imported
                if(persistenceMap.get(fieldType.regularName()) == null
                        && helperMap.get(fieldType.regularName()) == null
                        && fieldType.isImportNeeded()){
                    //if they are neither an entity nor a helper class, they are outside the package and need to be imported
                    imports.add(fieldType.fullQualifiedName());
                }
            }
        }
        return imports;
    }
}
