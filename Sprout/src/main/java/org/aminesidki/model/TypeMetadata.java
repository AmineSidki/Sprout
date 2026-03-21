package org.aminesidki.model;

/**
 * @param regularName contains class name of type
 * @param fullQualifiedName contains package + class name of the type
 */
public record TypeMetadata (String regularName,
                            String fullQualifiedName)
{
    //Checks if an import is needed by excluding java.lang.<class> packages, and allowing subclasses
    public boolean isImportNeeded(){
        if (fullQualifiedName.isEmpty()) return false;
        if (!fullQualifiedName.startsWith("java.lang.")) return true;
        return fullQualifiedName.indexOf('.', 10) >= 0;
    }
}
