package org.aminesidki.model;

/**
 * @param packageName contains package of helper class, without the .entity part
 * @param className contains the class name of the helper class
 */
public record HelperMetadata(String packageName, String className) {}
