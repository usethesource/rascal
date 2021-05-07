package org.rascalmpl.uri.zip;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import org.rascalmpl.uri.ISourceLocationInput;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.classloaders.IClassloaderLocationResolver;

import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class ZipURIResolver implements ISourceLocationInput, IClassloaderLocationResolver {
	private static final IValueFactory VF = ValueFactoryFactory.getValueFactory();
	private final ZipFileResolver file = new ZipFileResolver();
	private final ZipFileResolver inputStream;
    private final URIResolverRegistry registry;

	public ZipURIResolver(URIResolverRegistry registry) {
	    this.registry = registry;
        inputStream = new ZipInputStreamResolver(registry);
    }
	
    @Override
    public String scheme() {
        return "zip";
    }
    
    private ZipFileResolver getTargetResolver(ISourceLocation uri) {
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
    
    private static String getInsideZipPath(ISourceLocation uri) {
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

    private ISourceLocation getResolvedZipPath(ISourceLocation uri) throws IOException {
        boolean isWrapped = uri.getScheme().startsWith("zip+");
        try {
            String path = uri.getPath();
            if (path != null && !path.isEmpty()) {
                int bang = path.lastIndexOf('!');
                if (bang != -1) {
                    return safeResolve(VF.sourceLocation(
                        isWrapped ? uri.getScheme().substring("zip+".length()) : "file",
                        isWrapped ? uri.getAuthority() : "",
                        path.substring(path.indexOf("/"), bang)));
                }
            }
            throw new IOException("The zip-file and the internal path should be separated with a ! (" + uri.getPath() + ")");
        }
        catch (UnsupportedOperationException | URISyntaxException e) {
			throw new IOException("Invalid URI: \"" + uri +"\"", e);
        }
    }
    
    @Override
    public InputStream getInputStream(ISourceLocation uri) throws IOException {
        ISourceLocation zipUri = getResolvedZipPath(uri);
        return getTargetResolver(zipUri).getInputStream(zipUri, getInsideZipPath(uri));
    }


    @Override
    public boolean isDirectory(ISourceLocation uri) {
        if (uri.getPath() != null && (uri.getPath().endsWith("!") || uri.getPath().endsWith("!/"))) {
            // if the uri is the root of a jar, and it ends with a ![/], it should be considered a
            // directory
            return true;
        }
        try {
            ISourceLocation zipUri = getResolvedZipPath(uri);
            return getTargetResolver(zipUri).isDirectory(zipUri, getInsideZipPath(uri));
        }
        catch (IOException e) {
            return false;
        }
    }
    
    
    @Override
    public boolean exists(ISourceLocation uri) {
        try {
            ISourceLocation zipUri = getResolvedZipPath(uri);
            return getTargetResolver(zipUri).exists(zipUri, getInsideZipPath(uri));
        }
        catch (IOException e) {
            return false;
        }
    }
    
    @Override
    public boolean isFile(ISourceLocation uri) {
        try {
            ISourceLocation zipUri = getResolvedZipPath(uri);
            return getTargetResolver(zipUri).isFile(zipUri, getInsideZipPath(uri));
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
        ISourceLocation zipUri = getResolvedZipPath(uri);
        return getTargetResolver(zipUri).lastModified(zipUri, getInsideZipPath(uri));
    }
    
    @Override
    public String[] list(ISourceLocation uri) throws IOException {
        ISourceLocation zipUri = getResolvedZipPath(uri);
        return getTargetResolver(zipUri).list(zipUri, getInsideZipPath(uri));
    }

    
    @Override
    public boolean supportsHost() {
        return true; // someone we wrap might support host
    }

    @Override
    public ClassLoader getClassLoader(ISourceLocation loc, ClassLoader parent) throws IOException {
        return registry.getClassLoader(getResolvedZipPath(loc), parent);
    }
}
