package org.AmineSidki.generator.SourceGenerator;

import org.AmineSidki.enumeration.Association;
import org.AmineSidki.exception.ParsingException;
import org.AmineSidki.generator.SproutSourceGenerator;
import org.AmineSidki.model.EntityMetadata;
import org.AmineSidki.model.FieldMetadata;
import org.AmineSidki.model.HelperMetadata;
import org.AmineSidki.util.ParserUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ImportGenerator implements SproutSourceGenerator {
    @Override
    public HashSet<String> generate(EntityMetadata entityMetadata, Map<String, EntityMetadata> persistenceMap , Map<String, HelperMetadata> helperMap, String type){
        switch(type){
            case "dto":
                return getDtoImports(entityMetadata.getFields() , persistenceMap , helperMap);
            case "mapper":
                return null;
            case "repository":
                return null;
            case "service":
                return null;
            default:
                throw new ParsingException("");
        }
    }

    public HashSet<String> getDtoImports(List<FieldMetadata> fields , Map<String , EntityMetadata> pm , Map<String, HelperMetadata> hm){
        HashSet<String> imports = new HashSet<>();

        for( FieldMetadata fm : fields){
            String fieldType = fm.getType();

            if(!fm.getAssociation().equals(Association.DEFAULT)){
                if(fm.getAssociation().equals(Association.ONE_TO_MANY) || fm.getAssociation().equals(Association.MANY_TO_MANY)){
                    imports.add("java.util.Set");
                    fieldType = ParserUtil.extractCollectionGenericType(fieldType);
                }
                EntityMetadata entity = pm.get(fieldType);

                if(entity != null){
                    imports.add(entity.getIdType().getPackageName());
                }else{
                    HelperMetadata helper = hm.get(fieldType);
                    if(helper == null){
                        throw new ParsingException("");
                    }
                    imports.add(helper.getPackageName() + "." + helper.getClassName());
                }
            }
        }

        return imports;
    }

    //TODO
    public HashSet<String> getMapperImports();

    //TODO
    public HashSet<String> getRepositoryImports();

    //TODO
    public HashSet<String> getServiceImports();
}
