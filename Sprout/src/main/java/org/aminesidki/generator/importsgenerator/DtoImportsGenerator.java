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

public class DtoImportsGenerator implements SproutImportsGenerator {
    @Override
    public HashSet<String> generate(EntityMetadata entityMetadata, Map<String, EntityMetadata> pm, Map<String, HelperMetadata> hm) {
        HashSet<String> imports = new HashSet<>();

        for( FieldMetadata fm : entityMetadata.fields()){
            TypeMetadata fieldType = fm.type();

            if(fieldType.fullQualifiedName().isEmpty()) continue;

            if(!fm.association().equals(Association.DEFAULT) ){
                // if it is one of these associations, then it is mandatory to import java.util.Set
                if(fm.association().equals(Association.ONE_TO_MANY) || fm.association().equals(Association.MANY_TO_MANY)){
                    imports.add("java.util.List");
                    fieldType = new TypeMetadata(ParserUtil.extractCollectionGenericType(fieldType.regularName()) , fieldType.fullQualifiedName());
                }
                //check if the type is an entity
                EntityMetadata entity = pm.get(fieldType.regularName());

                if(entity != null){
                    if((entity.id().type().isImportNeeded())){
                        imports.add(entity.id().type().fullQualifiedName());
                    }
                }else{
                    //if it isn't an entity then it must be a helper
                    HelperMetadata helper = hm.get(fieldType.regularName());
                    if(helper == null){
                        imports.add(fieldType.fullQualifiedName());
                        continue;
                    }
                    imports.add(helper.packageName() + "." + helper.className());
                }
            }else{
                // check if their type needs to be imported
                if(pm.get(fieldType.regularName()) == null
                        && hm.get(fieldType.regularName()) == null
                        && fieldType.isImportNeeded()){
                    //if they are neither an entity nor a helper class, they are outside the package and need to be imported
                    imports.add(fieldType.fullQualifiedName());
                }
            }
        }

        return imports;
    }
}