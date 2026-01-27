package org.AmineSidki.generator;

import com.github.mustachejava.Mustache;
import org.AmineSidki.exception.FileSystemException;
import org.AmineSidki.generator.SourceGenerator.ImportGenerator;
import org.AmineSidki.model.EntityMetadata;

import java.io.IOException;

public interface SproutFileGenerator {
    void generate(ImportGenerator importGenerator, EntityMetadata entityMetadata , Mustache mustache , String defDir) throws IOException , FileSystemException;
}
