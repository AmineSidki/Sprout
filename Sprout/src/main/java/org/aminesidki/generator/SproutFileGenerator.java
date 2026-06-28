package org.aminesidki.generator;

import com.github.mustachejava.Mustache;
import org.aminesidki.exception.FileSystemException;
import org.aminesidki.model.EntityMetadata;
import org.aminesidki.util.FileCreator;
import org.aminesidki.util.Ledger;

import java.io.IOException;

/**
 * Generates source files for given entity
 */
public interface SproutFileGenerator {
    void generate(EntityMetadata entityMetadata , Mustache mustache , String defDir, FileCreator fileCreator, Ledger ledger) throws IOException , FileSystemException;
}
