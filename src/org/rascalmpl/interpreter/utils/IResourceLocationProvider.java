package org.rascalmpl.interpreter.utils;

import java.util.Set;

import io.usethesource.vallang.ISourceLocation;

/**
 * This is a injected configuration parameter of a Rascal run-time
 * environment like IRascalMonitor and IDEServices. The goal is
 * to find all the file names that have the given `fileName` in
 * the current run-time environment. For compiled Rascal this would
 * be ClassLoader.findResources, while for the interpreter it is 
 * typically a search through all the roots of the source folders.
 */
public interface IResourceLocationProvider {
    Set<ISourceLocation> findResources(String fileName);
}
