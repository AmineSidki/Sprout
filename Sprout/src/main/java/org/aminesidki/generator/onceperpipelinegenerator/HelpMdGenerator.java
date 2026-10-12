package org.aminesidki.generator.onceperpipelinegenerator;

import com.github.mustachejava.Mustache;
import lombok.RequiredArgsConstructor;
import org.aminesidki.exception.FileSystemException;
import org.aminesidki.generator.SproutOncePerPipelineGenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
public class HelpMdGenerator implements SproutOncePerPipelineGenerator {
    private final String defaultDir;

    @Override
    public void generate(Mustache mustache, String packageName) throws IOException, FileSystemException {
        File mdFile = new File(defaultDir + File.separator + "HELP.md");
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(mdFile))) {
            mustache.execute(writer, Map.of());
        }
    }
}
