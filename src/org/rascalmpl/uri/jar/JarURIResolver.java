package org.rascalmpl.uri.jar;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import org.rascalmpl.uri.ISourceLocationInput;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.uri.classloaders.IClassloaderLocationResolver;

import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class JarURIResolver implements ISourceLocationInput, IClassloaderLocationResolver {
	private static final IValueFactory VF = ValueFactoryFactory.getValueFactory();
	private final JarFileResolver file = new JarFileResolver();
	private final JarFileResolver inputStream;
    private final URIResolverRegistry registry;

	public JarURIResolver(URIResolverRegistry registry) {
	    this.registry = registry;
        inputStream = new JarInputStreamResolver(registry);
	    
    }
	
    @Override
    public String scheme() {
        return "jar";
    }
    
    private JarFileResolver getTargetResolver(ISourceLocation uri) {
       if (uri.getScheme().equals("file")) {
           return file;
       }
       return inputStream;
    }
    
    private ISourceLocation safeResolve(ISourceLocation loc) {
        try {
            return registry.logicalToPhysical(loc);
        }
        catch (Throwable e) {
            return loc;
        }
    }
    
    private static String getInsideJarPath(ISourceLocation uri) {
        String path = uri.getPath();
        if (path != null && !path.isEmpty()) {
            int bang = path.lastIndexOf('!');
            if (bang != -1) {
                path = path.substring(bang + 1);
                while (path.startsWith("/")) { 
                    path = path.substring(1);
                }
                return path;
            }
        }
        return "";
    }

    private ISourceLocation getResolvedJarPath(ISourceLocation uri) throws IOException {
        boolean isWrapped = uri.getScheme().startsWith("jar+");
        try {
            String path = uri.getPath();
            if (path != null && !path.isEmpty()) {
                int bang = path.lastIndexOf('!');
                if (bang != -1) {
                    return safeResolve(VF.sourceLocation(
                        isWrapped ? uri.getScheme().substring("jar+".length()) : "file",
                        isWrapped ? uri.getAuthority() : "",
                        path.substring(path.indexOf("/"), bang)));
                }
            }
            throw new IOException("The jar and the internal path should be separated with a ! (" + uri.getPath() + ")");
        }
        catch (UnsupportedOperationException | URISyntaxException e) {
			throw new IOException("Invalid URI: \"" + uri +"\"", e);
        }
    }
    
    @Override
    public InputStream getInputStream(ISourceLocation uri) throws IOException {
        ISourceLocation jarUri = getResolvedJarPath(uri);
        return getTargetResolver(jarUri).getInputStream(jarUri, getInsideJarPath(uri));
    }


    @Override
    public boolean isDirectory(ISourceLocation uri) {
        if (uri.getPath() != null && (uri.getPath().endsWith("!") || uri.getPath().endsWith("!/"))) {
            // if the uri is the root of a jar, and it ends with a ![/], it should be considered a
            // directory
            return true;
        }
        try {
            ISourceLocation jarUri = getResolvedJarPath(uri);
            return getTargetResolver(jarUri).isDirectory(jarUri, getInsideJarPath(uri));
        }
        catch (IOException e) {
            return false;
        }
    }
    
    
    @Override
    public boolean exists(ISourceLocation uri) {
        try {
            ISourceLocation jarUri = getResolvedJarPath(uri);
            return getTargetResolver(jarUri).exists(jarUri, getInsideJarPath(uri));
        }
        catch (IOException e) {
            return false;
        }
    }
    
    @Override
    public boolean isFile(ISourceLocation uri) {
        try {
            ISourceLocation jarUri = getResolvedJarPath(uri);
            return getTargetResolver(jarUri).isFile(jarUri, getInsideJarPath(uri));
        }
        catch (IOException e) {
            return false;
        }
    }
    
    @Override
    public Charset getCharset(ISourceLocation uri) throws IOException {
        return null; // one day we might read the meta-inf?
    }
    
    @Override
    public long lastModified(ISourceLocation uri) throws IOException {
        ISourceLocation jarUri = getResolvedJarPath(uri);
        return getTargetResolver(jarUri).lastModified(jarUri, getInsideJarPath(uri));
    }
    
    @Override
    public String[] list(ISourceLocation uri) throws IOException {
        ISourceLocation jarUri = getResolvedJarPath(uri);
        return getTargetResolver(jarUri).list(jarUri, getInsideJarPath(uri));
    }

    
    @Override
    public boolean supportsHost() {
        return true; // someone we wrap might support host
    }

    @Override
    public ClassLoader getClassLoader(ISourceLocation loc, ClassLoader parent) throws IOException {
        return registry.getClassLoader(getResolvedJarPath(loc), parent);
    }

    /**
     * Turns the any location of a jar file, as long as it has the extension `.jar`
     * to a location inside of that jarfile (the root).
     * 
     * For example: file:///myJar.jar  becomes jar+file:///myJar.jar!/
     * 
     * After this transformation you can search inside the jar using listEntries
     * or getChildLocation.
     */
    public static ISourceLocation jarify(ISourceLocation loc) {
        if (!loc.getPath().endsWith(".jar")) {
            return loc;
        }
        
        try {
            loc = URIUtil.changeScheme(loc, "jar+" + loc.getScheme());
            loc = URIUtil.changePath(loc, loc.getPath() + "!/");
            return loc;
        }
        catch (URISyntaxException e) {
            assert false;  // this can never happen;
            return loc;
        }
    }

}
