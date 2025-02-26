package org.rascalmpl.uri.file;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.compress.utils.FileNameUtils;
import org.rascalmpl.library.util.Maven;
import org.rascalmpl.uri.ISourceLocationInput;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.uri.classloaders.IClassloaderLocationResolver;
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
 */
public class MavenRepositoryURIResolver implements ISourceLocationInput, IClassloaderLocationResolver {
    private final ISourceLocation root = inferMavenRepositoryLocation();
    private final URIResolverRegistry reg;
    
    public MavenRepositoryURIResolver(URIResolverRegistry reg) throws IOException, URISyntaxException {
        super();
        this.reg = reg;
    }

    /**
     * This code is supposed to run very quickly in most cases, but still deal with 
     * the situation that the actual location of the local repository may be configured
     * in an XML file in the maven installation folder. In that case we run
     * `mvn help:evaluate` (once) to retrieve the actual location. If the `mvn` command
     * is not on the OS search PATH, tough luck.
     * @throws URISyntaxException 
     */
    private static ISourceLocation inferMavenRepositoryLocation() throws URISyntaxException {
        String property = System.getProperty("maven.repo.local");
        if (property != null) {
            return URIUtil.createFileLocation(property);
        }

        Path m2HomeFolder = Paths.get(System.getProperty("user.home"), ".m2", "repository");
        if (true || !Files.exists(m2HomeFolder)) {
            // only then we go for the expensive option and retrieve it from maven itself
            String configuredLocation = getLocalRepositoryLocationFromMavenCommand();

            if (configuredLocation != null) {
                return URIUtil.createFileLocation(configuredLocation);
            }
            
            // if the above fails we default to the home folder anyway.
            // note that since it does not exist this will make all downstream resolutions fail
            // to "file does not exist"
        }
        
        return URIUtil.createFileLocation(m2HomeFolder);
    }

    /**
     * This (slow) code runs only if the ~/.m2 folder does not exist and nobody -D'ed its location either.
     * That is not necessarily how mvn prioritizes its configuration steps, but it is the way we can 
     * get a quick enough answer most of the time.
     */
    private static String getLocalRepositoryLocationFromMavenCommand() {
        try {
            var tempFile = Maven.getTempFile("classpath");

            Maven.runCommand(List.of(
                "-q", 
                "help:evaluate",
                "-Dexpression=settings.localRepository",
                "-Doutput=" + tempFile
               ), null /* no current pom.xml file */, tempFile);
            
               
            try (var reader = new BufferedReader(new FileReader(tempFile.toString()))) {
                return reader.lines().collect(Collectors.joining()).trim(); 
            }
        }
        catch (IOException e) {
            // it's ok to fail. that just happens.
            return null;
        }
    }
 
    /** 
     * @param input   mvn://groupid!artifactId!version/path
     * @return        a file:/// reference to the jar file that is designated by the authority.
     * @throws IOException when the authority does not designate a jar file
     */
    private ISourceLocation resolveOutside(ISourceLocation input) throws IOException {
        String authority = input.getAuthority();

        if (authority.isEmpty()) {
            throw new IOException("missing mvn://groupid!artifactId!version/ as the authority in " + input);
        }

        var parts = authority.split("!");

        if (parts.length == 3) {
            String group = parts[0];
            String name = parts[1];
            String version = parts[2];
            
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

            // find the right jar file in the .m2 folder
            return URIUtil.getChildLocation(root, jarPath);  
        }
        else {
            throw new IOException("Pattern mvn:///groupId!artifactId!version did not match on " + input);
        }
    }

    private ISourceLocation resolveInside(ISourceLocation input) throws IOException {
        var jarLocation = resolveOutside(input);
        return URIUtil.getChildLocation(JarURIResolver.jarify(jarLocation), input.getPath());
    }

    @Override
    public ClassLoader getClassLoader(ISourceLocation loc, ClassLoader parent) throws IOException {
        // we simply request a classloader for the entire jar file, which will produce
        // eventually one indexed URLClassLoader for all constituents of a classpath
        return reg.getClassLoader(resolveOutside(loc), parent);
    }

    @Override
    public InputStream getInputStream(ISourceLocation uri) throws IOException {
        return reg.getInputStream(resolveInside(uri));
    }

    @Override
    public Charset getCharset(ISourceLocation uri) throws IOException {
        return reg.getCharset(resolveInside(uri));
    }

    @Override
    public boolean exists(ISourceLocation uri) {
        try {
            return reg.exists(resolveInside(uri));
        }
        catch (IOException e) {
            return false;
        }
    }

    @Override
    public long lastModified(ISourceLocation uri) throws IOException {
        return reg.lastModified(resolveInside(uri));
    }

    @Override
    public boolean isDirectory(ISourceLocation uri) {
        try {
            return reg.isDirectory(resolveInside(uri));
        }
        catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean isFile(ISourceLocation uri) {
        try {
            return reg.isFile(resolveInside(uri));
        }
        catch (IOException e) {
            return false;
        }
    }

    @Override
    public String[] list(ISourceLocation uri) throws IOException {
        if (uri.getAuthority().isEmpty() && (uri.getPath().isEmpty() || uri.getPath().equals("/"))) {
            return listAllMainArtifacts();
        }
        else {
            return reg.listEntries(resolveInside(uri));
        }
    }

    /**
     * Allows browsing through the mvn .m2 repository for "main" artifacts only
     * @return array of all authorities encoding jar files in the .m2 repo
     * @throws IOException if the root of the repo is gone or not permitted
     */
    private String[] listAllMainArtifacts() throws IOException {
        try (Stream<Path> files = Files.walk(Paths.get(root.getPath()))) {
            return files.filter(f -> FileNameUtils.getExtension(f).equals("jar"))
            // drop -sources and -javadoc
            .map(f -> {
                var fl = URIUtil.correctLocation("file", "", f.toString());
                var parent = URIUtil.getParentLocation(fl);
                var version = URIUtil.getLocationName(parent);
                var grandParent = URIUtil.getParentLocation(parent);
                var artifact = URIUtil.getLocationName(grandParent);
                var groupId = URIUtil.relativize(root, URIUtil.getParentLocation(grandParent))
                    .getPath()
                    .substring(1)
                    .replaceAll("/", ".");

                if ((artifact + "-" + version + ".jar").equals(URIUtil.getLocationName(fl))) {
                    return groupId + "!" + artifact + "!" + version;
                }
                else {
                    // it was not a main artifact because the file names don't line up
                    return null;
                }
            })
            .filter(f -> f != null)  // other artifacts like sources jars and such drop off here
            .toArray(String[]::new)
            ;          
        }
    }

    @Override
    public String scheme() {
        return "mvn";
    }

    @Override
    public boolean supportsHost() {
        return false;
    }
}
