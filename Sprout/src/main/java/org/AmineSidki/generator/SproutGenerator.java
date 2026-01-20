package org.AmineSidki.generator;

import com.github.mustachejava.Mustache;
import org.AmineSidki.exception.FileSystemException;
import org.AmineSidki.model.EntityMetadata;

import java.io.IOException;

public interface SproutGenerator {
    void generate(EntityMetadata entityMetadata , Mustache mustache , String defDir) throws IOException , FileSystemException;
}
