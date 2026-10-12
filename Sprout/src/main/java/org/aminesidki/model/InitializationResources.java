package org.aminesidki.model;

import com.github.mustachejava.Mustache;

/**
 * @param repoMustache repository template, is defined when needed
 * @param dtoMustache dto template, is defined when needed
 * @param mapperMustache mapper template, is defined when needed
 * @param serviceMustache service template, is defined when needed
 * @param controllerMustache controller template, is defined when needed
 * @param exceptionMustache exception template, is defined when needed
 */
public record InitializationResources(Mustache repoMustache,
                                      Mustache dtoMustache,
                                      Mustache mapperMustache,
                                      Mustache serviceMustache,
                                      Mustache controllerMustache,
                                      Mustache exceptionMustache,
                                      Mustache helpMdMustache) {
}
