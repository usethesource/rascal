package org.rascalmpl.uri.file;

import java.io.IOException;
import java.util.regex.Pattern;

import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.uri.jar.JarURIResolver;

import io.usethesource.vallang.ISourceLocation;

/**
 * Finds jar files (and what's inside) relative to the root of the LOCAL Maven repository.
 * 
 * We use `mvn://<groupid>.<name>-<version>/<path-inside-jar>` as the general scheme.
 * So the authority encodes the identity of the maven project and the path encodes
 * what's inside the respective jar file.
 * 
 * Here version is [0-9]+ major . [0-9]+ minor .[0-9]+ path -[A-Za-z\-]* optionalTag
 * 
 * Locations with the `mvn` scheme are typically produced by configuration code that uses 
 * Maven to resolve dependencies. Once the group id, name and version are known, any
 * `mvn:///` location is easily constructed and uses. It can be seen as a transparent
 * short-hand for an absolute `file:///` location that points into the (deeply nested)
 * .m2 local repository. The prime benefits are:
 *    1. much shorter location than `file:///`
 *    2. full transparency, more so than `lib:///`
 *    3. unique identification, modulo the (configured) location of the local repository.
 * 
 * It is a logical resolver in order to allow for short names for frequently
 * occurring paths, without loss of transparancy. We always know which jar file is meant,
 * and the encoding is one-to-one. 
 * 
 * This resolver does NOT find the "latest" version or any version of a package without an explicit
 * version reference in the authority pattern, by design. Any automated resolution here would
 * make the dependency management downstream less transparant and possibly error-prone. 
 * 
 * This resolver also does not implement any download or installation procedure, by design.
 * It should simply reflect what has been downloaded and installed into the LOCAL maven
 * repository.
 * 
 * This resolver is to replace for the large part the use of the deprecate `lib` scheme
 * from {@see RascalLibraryURIResolver} which leaves too much implicit and automates too 
 * much to obtain any transparancy in dependency resolution. 
 */
public class MavenRepositoryURIResolver extends AliasedFileResolver {
    private final Pattern namePattern 
        = Pattern.compile("^([a-zA-Z0-9.]+)\\.([a-zA-Z0-9]+)-([0-9]+\\.[0-9]+\\.[0-9]+)(-[A-Z0-9-]+)?$");

    public MavenRepositoryURIResolver() throws IOException {
        super("mvn", inferMavenRepositoryLocation());
    }

    private static String inferMavenRepositoryLocation() {
        // TODO add -D and maven home settings resolution
        return System.getProperty("user.home") + "/.m2/repository";
    }

    @Override
    public ISourceLocation resolve(ISourceLocation input) throws IOException {
        String authority = input.getAuthority();
        String path = input.getPath();

        if (authority.isEmpty()) {
            // we simply see this as a path relative to the root of the .m2 folder
            return URIUtil.getChildLocation(root, path);
        }
        else {
            // the authority encodes the group, name and version of a maven dependency
            // org.rascalmpl.rascal-34.2.0-RC2
            var m = namePattern.matcher(authority);

            if (m.matches()) {
                String group = m.group(1);
                String name = m.group(2);
                String version = m.group(3);
                String tag = m.group(4);

                // tags are optional
                tag = tag == null ? "" : tag;

                String jarPath 
                    = group.replaceAll("\\.", "/")
                    + "/"
                    + name
                    + "/"
                    + version
                    + "/"
                    + name
                    + "-"
                    + version
                    + tag
                    + ".jar"
                    ;

                var jarLocation = URIUtil.getChildLocation(root, jarPath);

                if (!path.isEmpty()) {
                    // go make a location that points _inside_ of the jar
                    jarLocation = JarURIResolver.jarify(jarLocation);
                    jarLocation = URIUtil.getChildLocation(jarLocation, path);
                }

                return jarLocation;
            }
            else {
                throw new IOException("Could not parse authority into Maven Project group, name and version: " + authority);
            }
        }    
    }
}
