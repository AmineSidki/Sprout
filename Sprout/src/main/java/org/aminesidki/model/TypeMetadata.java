package org.aminesidki.model;

public record TypeMetadata (String regularName,
                            String fullQualifiedName)
{
    public boolean isImportNeeded(){
        try{
            return !fullQualifiedName.isEmpty() && !fullQualifiedName().startsWith("java.lang.")
                    || fullQualifiedName().substring(10).contains(".");
        } catch (Exception e) {
            return false;
        }
    }
}
