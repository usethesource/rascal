package org.rascalmpl.uri;

import java.io.IOException;

import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.uri.jar.JarURIResolver;
import io.usethesource.vallang.ISourceLocation;

/**
 * Provides transparant access to the source code of the one and only standard library
 * that should be on the source path of the interpreter, which is contained in the same
 * jar as the current interpreter is from.
 * 
 * The std:/// scheme is mainly used by the interpreter, to load library modules; but it is also the location
 * of distributed sources of the standard library for use in the debugger. The references
 * that the type-checker produces for UI feature in the IDE also depend on this scheme.
 * 
 * This is accomplished by:
 * 1. rewriting all the references to the source locations of the library in 
 *   project://rascal/src/org/rascalmpl/library/...` to `std:///...`. This is done by the "packager"
 * 2. copying all the .rsc files of the library to the jar in the right location (mvn resources plugin)
 * 3. resolving std:/// to the same location inside of the jar
 * 4. **not** having more than one standard library in the classpath of the JVM, or more than one
 *    standard library in the libs path of a PathConfig. 
 */
public class StandardLibraryURIResolver implements ILogicalSourceLocationResolver {
    private static final ISourceLocation currentRascalJar = 
        URIUtil.getChildLocation(
            JarURIResolver.jarify(
                resolveCurrentRascalJar()
            ),
            "org/rascalmpl/library"
        );

    private static ISourceLocation resolveCurrentRascalJar() {
        try {
            return PathConfig.resolveCurrentRascalRuntimeJar(); 
        }
        catch (IOException e) {
            // this will be reported elsewhere in PathConfi - 
            return null;
        }
    }

    @Override
    public ISourceLocation resolve(ISourceLocation input) throws IOException {
        return URIUtil.getChildLocation(currentRascalJar, input.getPath()); 
    }

    @Override
    public String scheme() {
        return "std";
    }

    @Override
    public String authority() {
        return "";
    }

}
