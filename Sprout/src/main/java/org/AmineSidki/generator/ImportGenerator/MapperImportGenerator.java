package org.AmineSidki.generator.ImportGenerator;

import org.AmineSidki.enumeration.Association;
import org.AmineSidki.generator.SproutImportGenerator;
import org.AmineSidki.model.EntityMetadata;
import org.AmineSidki.model.FieldMetadata;
import org.AmineSidki.model.HelperMetadata;
import org.AmineSidki.model.TypeMetadata;
import org.AmineSidki.util.ParserUtil;

import java.util.HashSet;
import java.util.Map;

public class MapperImportGenerator implements SproutImportGenerator {
    @Override
    public HashSet<String> generate(EntityMetadata entityMetadata, Map<String, EntityMetadata> pm, Map<String, HelperMetadata> hm) {
        HashSet<String> imports = new HashSet<>();

        for(FieldMetadata fm : entityMetadata.fields()){
            TypeMetadata fieldType = fm.type();

            if(fieldType.getFullQualifiedName().isEmpty()) continue;

            if(fieldType.getRegularName().startsWith("Set<")) imports.add("java.util.Set");
            if(fieldType.getRegularName().startsWith("List<")) imports.add("java.util.List");

            //In case it is an association, add the destination class(if entity add its Id)
            if(!fm.association().equals(Association.DEFAULT)){
                if(fm.association().equals(Association.ONE_TO_MANY) || fm.association().equals(Association.MANY_TO_MANY)){
                    fieldType = new TypeMetadata(ParserUtil.extractCollectionGenericType(fieldType.getRegularName()),
                            fieldType.getFullQualifiedName());
                }

                //check if the type is an entity
                EntityMetadata entity = pm.get(fieldType.getRegularName());

                if(entity != null){
                    if((!entity.id().type().getFullQualifiedName().startsWith("java.lang.")
                            || entity.id().type().getFullQualifiedName().substring(10).contains("."))){
                        imports.add(entity.id().type().getFullQualifiedName());
                    }

                    imports.add(entity.packageName() + ".repository." + entity.className() + "Repository");
                }else{
                    //if it isn't an entity then it must be a helper
                    HelperMetadata helper = hm.get(fieldType.getRegularName());
                    if(helper == null){
                        imports.add(fieldType.getFullQualifiedName());
                        continue;
                    }
                    imports.add(helper.packageName() + "." + helper.className());
                }
            }else{
                // check if their type needs to be imported
                if(pm.get(fieldType.getRegularName()) == null
                        && hm.get(fieldType.getRegularName()) == null
                        && (!fieldType.getFullQualifiedName().startsWith("java.lang.")
                        || fieldType.getFullQualifiedName().substring(10).contains("."))){
                    //if they are neither an entity nor a helper class, they are outside the package and need to be imported
                    imports.add(fieldType.getFullQualifiedName());
                }
            }
        }
        return imports;
    }
}
