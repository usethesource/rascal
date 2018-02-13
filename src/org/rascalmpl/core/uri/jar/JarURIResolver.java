package org.rascalmpl.core.uri.jar;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import org.rascalmpl.core.uri.ISourceLocationInput;
import org.rascalmpl.core.uri.URIResolverRegistry;
import org.rascalmpl.core.values.ValueFactoryFactory;

import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValueFactory;

public class JarURIResolver implements ISourceLocationInput {
	private static final IValueFactory VF = ValueFactoryFactory.getValueFactory();
	private final JarFileResolver file = new JarFileResolver();
	private final JarFileResolver inputStream;

	public JarURIResolver(URIResolverRegistry registry) {
	    inputStream = new JarInputStreamResolver(registry);
    }
	
    @Override
    public String scheme() {
        return "jar";
    }
    
    private JarFileResolver getTargetResolver(ISourceLocation uri) {
       if (uri.getScheme().startsWith("jar+")) {
           return inputStream;
       }
       return file;
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

    private static ISourceLocation getJarPath(ISourceLocation uri) throws IOException {
        boolean isWrapped = uri.getScheme().startsWith("jar+");
        try {
            String path = uri.getPath();
            if (path != null && !path.isEmpty()) {
                int bang = path.lastIndexOf('!');
                if (bang != -1) {
                    return VF.sourceLocation(
                        isWrapped ? uri.getScheme().substring("jar+".length()) : "file",
                        isWrapped ? uri.getAuthority() : "",
                        path.substring(path.indexOf("/"), bang));
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
        return getTargetResolver(uri).getInputStream(getJarPath(uri), getInsideJarPath(uri));
    }


    @Override
    public boolean isDirectory(ISourceLocation uri) {
        if (uri.getPath() != null && (uri.getPath().endsWith("!") || uri.getPath().endsWith("!/"))) {
            // if the uri is the root of a jar, and it ends with a ![/], it should be considered a
            // directory
            return true;
        }
        try {
            return getTargetResolver(uri).isDirectory(getJarPath(uri), getInsideJarPath(uri));
        }
        catch (IOException e) {
            return false;
        }
    }
    
    
    @Override
    public boolean exists(ISourceLocation uri) {
        try {
            return getTargetResolver(uri).exists(getJarPath(uri), getInsideJarPath(uri));
        }
        catch (IOException e) {
            return false;
        }
    }
    
    @Override
    public boolean isFile(ISourceLocation uri) {
        try {
            return getTargetResolver(uri).isFile(getJarPath(uri), getInsideJarPath(uri));
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
        return getTargetResolver(uri).lastModified(getJarPath(uri), getInsideJarPath(uri));
    }
    
    @Override
    public String[] list(ISourceLocation uri) throws IOException {
        return getTargetResolver(uri).list(getJarPath(uri), getInsideJarPath(uri));
    }

    
    @Override
    public boolean supportsHost() {
        return true; // someone we wrap might support host
    }
}
