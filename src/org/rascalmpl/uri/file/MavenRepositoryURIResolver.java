package org.rascalmpl.uri.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.uri.jar.JarURIResolver;

import io.usethesource.vallang.ISourceLocation;

/**
 * Finds jar files (and what's inside) relative to the root of the LOCAL Maven repository.
 * 
 * We use `mvn://<groupid>!<name>!<version>/<path-inside-jar>` as the general scheme;
 * also `mvn://<groupid>!<name>!<version>/!/<path-inside-jar>` is allowed to make sure the
 * root `mvn://<groupid>!<name>!<version>/` remains a jar file unambiguously.
 * 
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
    private final Pattern authorityRegEx 
        = Pattern.compile("^([a-zA-Z0-9-_.]+?)[!]([a-zA-Z0-9-_.]+)([!][a-zA-Z0-9\\-_.]+)$");
    //                               groupId         !  artifactId      ! optionAlComplexVersionString

    public MavenRepositoryURIResolver() throws IOException {
        super("mvn", inferMavenRepositoryLocation());
    }

    /**
     * This code is supposed to run very quickly in most cases, but still deal with 
     * the situation that the actual location of the local repository may be configured
     * in an XML file in the maven installation folder. In that case we run
     * `mvn help:evaluate` (once) to retrieve the actual location. If the `mvn` command
     * is not on the OS search PATH, tough luck.
     */
    private static String inferMavenRepositoryLocation() {
        String property = System.getProperty("maven.repo.local");
        if (property != null) {
            return property;
        }

        String m2HomeFolder = System.getProperty("user.home") + "/.m2/repository";
        if (!new File(m2HomeFolder).exists()) {
            // only then we go for the expensive option and retrieve it from maven itself
            String configuredLocation = getLocalRepositoryLocationFromMavenCommand();

            if (configuredLocation != null) {
                return configuredLocation;
            }
            
            // if the above fails we default to the home folder anyway.
            // note that since it does not exist this will make all downstream resolutions fail
            // to "file does not exist"
        }
        
        return m2HomeFolder;
    }

    /**
     * Maven has a different name on Windows; this computes the right name.
     */
    private static String computeMavenCommandName() {
        if (System.getProperty("os.name", "generic").startsWith("Windows")) {
            return "mvn.cmd";
        }
        else {
            return "mvn";
        }
    }

    /**
     * This (slow) code runs only if the ~/.m2 folder does not exist and nobody -D'ed its location either.
     * That is not necessarily how mvn prioritizes its configuration steps, but it is the way we can 
     * get a quick enough answer most of the time.
     */
    private static String getLocalRepositoryLocationFromMavenCommand() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(computeMavenCommandName(), 
                "-q", 
                "help:evaluate",
                "-Dexpression=settings.localRepository",
                "-DforceStdout"
               );
            processBuilder.environment().put("JAVA_HOME", System.getProperty("java.home", System.getenv("JAVA_HOME")));

            Process process = processBuilder.start();
            if (process.waitFor() != 0) {
                throw new IOException("could not run mvn to retrieve local repository location");
            }
            
            try (var reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                return reader.lines().collect(Collectors.joining()).trim(); 
            }
        }
        catch (IOException | InterruptedException e) {
            // it's ok to fail. that just happens.
            return null;
        }
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
            var m = authorityRegEx.matcher(authority);

            if (m.matches()) {
                String group = m.group(1);
                String name = m.group(2);
                String version = m.group(3);

                version = version == null ? "" : version.substring(1);
              
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
                    + ".jar"
                    ;

                var jarLocation = URIUtil.getChildLocation(root, jarPath);

                // convenience feature: `/!` path switches to jarified version (helps with auto-completion)
                var pathIsJarRoot = "/!".equals(path);

                // compensate for additional !'s produced by the previous
                if (!pathIsJarRoot && path.startsWith("/!")) {
                    path = path.substring(2);
                }

                // if the path is non-empty we mean to look inside of the jar
                    if ((!path.isEmpty() && !path.equals("/")) || pathIsJarRoot) {
                    path = pathIsJarRoot ? "" : path;
                    // go make a location that points _inside_ of the jar
                    jarLocation = JarURIResolver.jarify(jarLocation);
                    jarLocation = URIUtil.getChildLocation(jarLocation, path);
                }

                return jarLocation;
            }
            else {
                return null; // signal resolution has failed.
            }
        }    
    }
}
