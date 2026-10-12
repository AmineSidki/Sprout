package org.aminesidki.handler;

import com.github.mustachejava.Mustache;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.aminesidki.exception.FileSystemException;
import org.aminesidki.generator.SproutFileGenerator;
import org.aminesidki.generator.SproutOncePerPipelineGenerator;
import org.aminesidki.model.EntityMetadata;
import org.aminesidki.util.Logger;

@RequiredArgsConstructor
public class GenerationHandler {
  private final Map<String, EntityMetadata> emm;
  private final String packageName;

  private record OncePerPipelineGeneratorView(
      SproutOncePerPipelineGenerator generator, Mustache mustache, String generationMessage) {}
  ;

  private record FileGeneratorView(
      SproutFileGenerator generator, Mustache mustache, String generationMessage) {}
  ;

  private final List<FileGeneratorView> fileGeneratorList = new ArrayList<>();
  private final List<OncePerPipelineGeneratorView> oncePerPipelineGeneratorList = new ArrayList<>();

  public void add(SproutFileGenerator generator, Mustache mustache, String generationMessage) {
    fileGeneratorList.add(new FileGeneratorView(generator, mustache, generationMessage));
  }

  public void add(
      SproutOncePerPipelineGenerator generator, Mustache mustache, String generationMessage) {
    oncePerPipelineGeneratorList.add(
        new OncePerPipelineGeneratorView(generator, mustache, generationMessage));
  }

  public void generate() {
    for (OncePerPipelineGeneratorView gen : oncePerPipelineGeneratorList) {
      try {
        gen.generator().generate(gen.mustache(), packageName);
        if (gen.generationMessage != null) System.out.println(gen.generationMessage());
      } catch (IOException e) {
        throw new FileSystemException(
            "Failure while running the following operation : " + gen.generationMessage());
      } catch (FileSystemException fsE) {
        System.out.println(
            Logger.getInstance()
                .errorMessage(
                    "Failure while running the following operation : " + gen.generationMessage()));
        fsE.printStackTrace();
      }
    }

    for (EntityMetadata em : emm.values()) {
      if (em.isIgnored()) continue;
      try {
        for (FileGeneratorView gen : fileGeneratorList) {
          gen.generator().generate(em, gen.mustache());
          System.out.println(gen.generationMessage() + em.className());
        }
      } catch (IOException e) {
        throw new FileSystemException("Failure in class " + em.className() + "\n" + e);
      } catch (FileSystemException fsE) {
        System.out.println(
            Logger.getInstance()
                .errorMessage("File generation failed for class " + em.className()));
        fsE.printStackTrace();
      }
    }
  }
}
