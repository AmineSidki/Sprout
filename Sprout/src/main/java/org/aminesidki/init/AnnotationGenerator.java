package org.aminesidki.init;

import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import lombok.RequiredArgsConstructor;
import org.aminesidki.enumeration.ElementType;
import org.aminesidki.exception.FileSystemException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class AnnotationGenerator {
    private record AnnotationView(String name, ElementType type){};

    private final MustacheFactory mf;
    private final String defDir;
    private final String packageName;

    private final List<AnnotationView> annotations = List.of(new AnnotationView("SproutProjection", ElementType.CLASS),
            new AnnotationView("SproutLargeDataField", ElementType.FIELD),
            new AnnotationView("SproutCached", ElementType.CLASS),
            new AnnotationView("SproutPaginated", ElementType.CLASS),
            new AnnotationView("SproutIgnore", ElementType.CLASS));

    public void generate() throws IOException {
        String finalPackageName = defDir + File.separator + "sproutannotation";
        File annotationsPackage = new File(finalPackageName);

        if(!annotationsPackage.exists() && !annotationsPackage.mkdir()){
            throw new FileSystemException("Failed to generate annotations !");
        }

        Mustache template =  mf.compile("templates/AnnotationTemplate.mustache");

        for(AnnotationView annotation : annotations){
            File annotationFile = new File(finalPackageName + File.separator  + annotation.name + ".java");

            if(!annotationFile.exists() && !annotationFile.createNewFile()){
                throw new FileSystemException("Failed to generate file for annotation " + annotation.name());
            }

            try(BufferedWriter writer = new BufferedWriter(new FileWriter(annotationFile))){
                Map<String, Object> context = Map.of("PackageName", packageName, "ClassName", annotation.name(), "ElementType", annotation.type());
                template.execute(writer, context);
            }
        }
    }
}
