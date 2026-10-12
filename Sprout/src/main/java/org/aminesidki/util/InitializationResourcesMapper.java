package org.aminesidki.util;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import lombok.RequiredArgsConstructor;
import org.aminesidki.model.InitializationResources;
import org.aminesidki.model.PartialGenerationFlags;

/**
 * Maps resources properly depending on the given flags
 */
@RequiredArgsConstructor
public class InitializationResourcesMapper {
    private final MustacheFactory mf;

    public InitializationResources map(PartialGenerationFlags pGroup){
        //Compiling templates
        Mustache helpMdMustache = mf.compile("templates/HelpMdTemplate.mustache");

        Mustache repoMustache =
                pGroup.rFlag ?
                        mf.compile("templates/RepositoryTemplate.mustache") : null;
        Mustache serviceMustache =
                pGroup.sFlag ?
                        mf.compile("templates/ServiceTemplate.mustache") : null;
        Mustache dtoMustache =
                pGroup.dFlag ?
                        mf.compile("templates/DtoTemplate.mustache") : null;
        Mustache mapperMustache =
                pGroup.mFlag ?
                        mf.compile("templates/MapperTemplate.mustache") : null;
        Mustache controllerMustache =
                pGroup.cFlag ?
                        mf.compile("templates/ControllerTemplate.mustache") : null;
        Mustache exceptionMustache =
                pGroup.eFlag ?
                        mf.compile("templates/ExceptionTemplate.mustache") : null;

        return new InitializationResources(repoMustache, dtoMustache, mapperMustache,
                serviceMustache, controllerMustache, exceptionMustache, helpMdMustache);
    }
}
