package org.aminesidki.generator.filegenerator.configfilegenerator;

import com.github.mustachejava.Mustache;
import org.aminesidki.exception.FileSystemException;
import org.aminesidki.generator.filegenerator.ConfigFileGenerator;
import org.aminesidki.model.EntityMetadata;
import org.aminesidki.util.FileCreator;
import org.aminesidki.util.Ledger;

import java.io.IOException;

public class CacheConfigGenerator implements ConfigFileGenerator {
    @Override
    public void generate(EntityMetadata entityMetadata, Mustache mustache, String defDir, FileCreator fileCreator, Ledger ledger) throws IOException, FileSystemException {
        // What should the ledger hold for the configs ??
        // imo, it should see whether a config package existed before the generation started,
        // and it should see if each of the configs that will be generated have been already, to avoid overstepping them.
    }
}
