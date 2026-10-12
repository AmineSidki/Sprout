package org.aminesidki.generator.onceperpipelinegenerator;

import com.github.mustachejava.Mustache;
import lombok.RequiredArgsConstructor;
import org.aminesidki.exception.FileSystemException;
import org.aminesidki.generator.SproutOncePerPipelineGenerator;
import org.aminesidki.util.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
public class CacheConfigurationGenerator implements SproutOncePerPipelineGenerator {
    private final String defaultDir;

    @Override
    public void generate(Mustache mustache, String packageName) throws IOException, FileSystemException {
        //Check if there is a package for configurations. yes / no -> checkedDirectory = true
        File configPackage = new File(defaultDir + File.separator + "Config");
        if(!configPackage.exists() && !configPackage.mkdir()){
            throw new FileSystemException("Failed to generate config for cache");
        }
        //Check file existence. no -> create new file ; yes -> show warning
        File configFile = new File(defaultDir + File.separator + "config" + File.separator + "CacheConfig.java");
        if(configFile.exists()) Logger.getInstance().warningMessage("A configuration file for cache already exists, so it will not be generated !");
        else if(!configFile.createNewFile()){
            throw new FileSystemException("Failed to generate config for cache");
        }

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(configFile))) {
            mustache.execute(writer, Map.of("PackageName", packageName));
        }
    }
}
