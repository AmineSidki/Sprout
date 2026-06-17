package org.aminesidki.util;

import org.aminesidki.exception.FileSystemException;

import java.io.File;
import java.io.IOException;

public class FileCreator {
    /**
     * @param name Class name
     * @param componentName Component name in capitalized form
     * @param defDir Default directory passed down by the main
     * @return The target file for the buffered writer
     */
    public File createFile(String name, String componentName, String defDir) throws IOException {
        File componentPackage = new File(defDir + File.separator + componentName.toLowerCase());
        if(!componentPackage.exists() && !componentPackage.mkdir()){
            throw new FileSystemException("Failed to generate " + componentName.toLowerCase() + " for " + name);
        }

        File componentFile = new File(defDir + File.separator +componentName.toLowerCase()+ File.separator + name + componentName + ".java");

        if(!componentFile.exists() && !componentFile.createNewFile()){
            throw new FileSystemException("Failed to generate " + componentName.toLowerCase() + " for " + name);
        }

        return componentFile;
    }
}
