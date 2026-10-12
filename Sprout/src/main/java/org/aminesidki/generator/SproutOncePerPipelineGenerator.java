package org.aminesidki.generator;

import com.github.mustachejava.Mustache;
import org.aminesidki.exception.FileSystemException;

import java.io.IOException;

/*
*   Generates components that only need to be generated once per project
*/
public interface SproutOncePerPipelineGenerator{
    void generate(Mustache mustache, String packageName) throws IOException, FileSystemException ;
}
