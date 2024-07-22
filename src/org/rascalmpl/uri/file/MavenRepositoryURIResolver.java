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
 * For a discussion REMOTE repositories see below.
 * 
 * We use `mvn://<groupid>!<name>!<version>/<path-inside-jar>` as the general scheme;
 * also `mvn://<groupid>!<name>!<version>/!/<path-inside-jar>` is allowed to make sure the
 * root `mvn://<groupid>!<name>!<version>/` remains a jar file unambiguously.
 * 
 * So the authority encodes the identity of the maven project and the path encodes
 * what's inside the respective jar file. This is analogous to other schemes for projects
 * and deployed projects such as project://, target://, plugin://, bundle:// and bundleresource://:
 * the authority identifies the container, and the path identifies what is inside.
 * 
 * Here `version` is an arbitrary string with lots of numbers, dots, dashed and underscores.
 * Typically we'd expect the semantic versioning scheme here with some release tag, but
 * real maven projects frequently do not adhere to that standard. Hence we have to be "free"
 * here and allow lots of funny version strings. This is also why we use ! again to separate
 * the version from the artifactId.
 * 
 * Locations with the `mvn` scheme are typically produced by configuration code that uses 
 * a Maven pom.xml to resolve dependencies. Once the group id, name and version are known, any
 * `mvn:///` location is easily constructed and used. It can be seen as a transparent
 * short-hand for an absolute `file:///` location that points into the (deeply nested)
 * .m2 local repository. The prime benefits are:
 *    1. much shorter location than `file:///`
 *    2. full transparency, more so than `lib:///`
 *    3. unique identification, modulo the (configured) location of the local repository.
 * 
 * It is a logical resolver in order to allow for short names for frequently
 * occurring paths, without loss of transparancy. We always know which jar file is meant,
 * and the encoding is (almost) one-to-one. 
 * 
 * This resolver does NOT find the "latest" version or any version of a package without an explicit
 * version reference in the authority pattern, by design. Any automated resolution here would
 * make the dependency management downstream less transparant and possibly error-prone. 
 * The `mvn://` locations are intended to be the _result_ of dependency resolution, not 
 * to implement dependency resolution.
 * 
 * This resolver also does not implement any download or installation procedure, by design.
 * It does not access any REMOTE repositories although it easily could be implemented.
 * This scheme should simply reflect what _has been downloaded and installed_ into the LOCAL maven
 * repository. This is for the sake of transparancy and predictability, but also for _legal_ reasons:
 * Any automated implicit downloading by the `mvn://` scheme could easily result in late/lazy downloading 
 * and linking by end-users who have not been able to diligently vet the legal implications of the 
 * reuse of another library. Therefore this scheme DOES NOT DOWNLOAD stuff, ever.
 * 
 * PLEASE DO NOT EVER ADD AUTOMATIC DOWNLOADING OR ACCESS TO REMOTE REPOSITORIES TO THIS SCHEME,
 * however easy or practical this may seem.
 * Download, installation, linking, bundling, making fat jars, etc. must all be scrutinized by the due 
 * diligence legal processes for open-source dependencies. Those who take responsibility
 * for dependencies on open-source packages, with for example GPL licenses, must have the 
 * opportunity to scrutinize every instance of incorporating such as dependency. Therefore
 * it must not be automated here. Note that these are not necessarily people from the usethesource or
 * Rascal-contributing organizations; but our industrial, educational and academic users 
 * (Rascal language engineers) that we protect here.
 * 
 * This resolver is to replace for the large part the use of the deprecate `lib` scheme
 * from {@see RascalLibraryURIResolver} which left too much implicit and automated too 
 * much to obtain any transparancy in dependency resolution. 
 * 
 * Another pitfall of this scheme is that since it transparantely resolves to file:/// and jar+file:///
 * schemes, it could be used to _write_ to `mvn` jars as well. Of course this is very much not a good 
 * idea; but there is currently no way to register read-only logical schemes.
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
