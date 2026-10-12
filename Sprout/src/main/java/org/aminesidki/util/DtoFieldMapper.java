package org.aminesidki.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.aminesidki.enumeration.Association;
import org.aminesidki.model.EntityMetadata;
import org.aminesidki.model.FieldMetadata;
import org.aminesidki.model.HelperMetadata;
import org.aminesidki.model.TypeMetadata;

/**
 * Static utility that is responsible for mapping fields with associations into collections, or
 * keeping them
 */
public class DtoFieldMapper {
  public static List<FieldMetadata> mapToDtoField(
      List<FieldMetadata> fmList,
      Map<String, EntityMetadata> persistenceMap,
      Map<String, HelperMetadata> helperMap) {
    List<FieldMetadata> output = new ArrayList<>();

    for (FieldMetadata fm : fmList) {
      FieldMetadata f = fm;
      if (!fm.association().equals(Association.DEFAULT)) {
        TypeMetadata idType;
        String fieldType = "";

        switch (fm.association()) {
          case ONE_TO_MANY:
          case MANY_TO_MANY:
            String typeName = ParserUtil.extractCollectionGenericType(fm.type().regularName());
            if (persistenceMap.containsKey(typeName)) {
              idType = persistenceMap.get(typeName).id().type();
            } else if (helperMap.containsKey(typeName)) {
              idType =
                  new TypeMetadata(
                      helperMap.get(typeName).className(),
                      helperMap.get(typeName).packageName()
                          + "."
                          + helperMap.get(typeName).className());
            } else {
              idType = new TypeMetadata(typeName, "");
            }

            fieldType = "List<" + idType.regularName() + ">";
            break;

          case ONE_TO_ONE:
          case MANY_TO_ONE:
            if (persistenceMap.containsKey(fm.type().regularName())) {
              idType = persistenceMap.get(fm.type().regularName()).id().type();
            } else if (helperMap.containsKey(fm.type().regularName())) {
              idType =
                  new TypeMetadata(
                      helperMap.get(fm.type().regularName()).className(),
                      helperMap.get(fm.type().regularName()).packageName()
                          + "."
                          + helperMap.get(fm.type().regularName()).className());
            } else {
              idType = fm.type();
            }

            fieldType = idType.regularName();
            break;

          default:
            idType = fm.type();
        }

        f =
            new FieldMetadata(
                new TypeMetadata(fieldType, idType.fullQualifiedName()),
                fm.name(),
                fm.association());
      }

      output.add(f);
    }

    return output;
  }
}
