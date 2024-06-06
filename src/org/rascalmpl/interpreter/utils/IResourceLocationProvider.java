package org.rascalmpl.interpreter.utils;

import java.util.Set;

import io.usethesource.vallang.ISourceLocation;

public interface IResourceLocationProvider {
    Set<ISourceLocation> findResources(String fileName);
}
