package org.rascalmpl.interpreter.load;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.rascalmpl.interpreter.Configuration;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.uri.BadURIException;
import org.rascalmpl.uri.IURIInputStreamResolver;
import org.rascalmpl.uri.IURIOutputStreamResolver;
import org.rascalmpl.uri.UnsupportedSchemeException;

/**
 * This class implements the rascal:// scheme. If the path component of a given URI represents a module name, then
 * this resolver will look through the Rascal search path and find the proper input stream if it exists.
 */
public class RascalURIResolver implements IURIInputStreamResolver, IURIOutputStreamResolver {
	private final ArrayList<IRascalSearchPathContributor> contributors;
	private final IEvaluatorContext ctx;
	
	public RascalURIResolver(IEvaluatorContext ctx) {
		this.contributors = new ArrayList<IRascalSearchPathContributor>();
		this.ctx = ctx;
	}
	
	public void addPathContributor(IRascalSearchPathContributor contrib) {
		contributors.add(0, contrib);
	}
	
	/**
	 * Returns a URI that does not have the rascal scheme, or null if the URI is not found.
	 */
	public URI resolve(URI uri) {
		try {
			for (URI dir : collect()) {
				URI full = getFullURI(getPath(uri), dir);
				if (ctx.getResolverRegistry().exists(full)) {
					return full;
				}
			}
			
			return null;
		} catch (URISyntaxException e) {
			return null;
		}
	}

	public boolean exists(URI uri) {
		try {
			if (uri.getScheme().equals(scheme())) {
				String path = getPath(uri);
				
				for (URI dir : collect()) {
					URI full = getFullURI(path, dir);
					if (ctx.getResolverRegistry().exists(full)) {
						return true;
					}
				}
			}
			return false;
		} 
		catch (URISyntaxException e) {
			return false;
		}
	}

	private List<URI> collect() {
		// collect should run the contributors in reverse order
		List<URI> paths = new LinkedList<URI>();
//		List<IRascalSearchPathContributor> reversed = (List<IRascalSearchPathContributor>) contributors.clone();
//		Collections.reverse(reversed);
		for (IRascalSearchPathContributor c : contributors) {
			c.contributePaths(paths);
		}
		
		for (URI uri : paths) {
			if (uri.getScheme().equals(scheme())) {
				throw new IllegalArgumentException("The rascal scheme can not be contributed to the Rascal path:" + uri);
			}
		}
		return paths;
	}

	private URI getFullURI(String path, URI dir) throws URISyntaxException {
		String dirPath = dir.getPath() != null ? dir.getPath() : "/";
		if (dirPath.length() > 0 && !dirPath.startsWith("/")) {
			dirPath = dirPath + "/";
		}
		while (path.startsWith("/")) {
			path = path.substring(1);
		}
		if (!dirPath.endsWith("/")) {
			path = "/" + path;
		}
		return new URI(dir.getScheme(), dir.getHost() != null ? dir.getHost() : "", dirPath + path, null);
	}

	private String getPath(URI uri) {
		String path = uri.getPath();
		if (!path.startsWith("/")) {
			path = path.concat("/");
		}
		if (!path.endsWith(Configuration.RASCAL_FILE_EXT)) {
			path = path.concat(Configuration.RASCAL_FILE_EXT);
		}
		path = path.replaceAll(Configuration.RASCAL_MODULE_SEP, Configuration.RASCAL_PATH_SEP);
		return path;
	}
	
	private String getBinaryPath(URI uri) {
		String path = getPath(uri);
		
		if (!path.endsWith(Configuration.RASCAL_BIN_FILE_EXT)) {
			path = path.concat(Configuration.RASCAL_BIN_FILE_EXT);
		}
		
		return path;
	}
	
	public InputStream getInputStream(URI uri) throws IOException {
		try {
			if (uri.getScheme().equals(scheme())) {
				String path = getPath(uri);

				for (URI dir : collect()) {
					URI full = getFullURI(path, dir);
					if (ctx.getResolverRegistry().exists(full)) {
						return ctx.getResolverRegistry().getInputStream(full);
					}
				}
			}
			throw new IOException("Module " + uri.getPath() + " not found");
		} 
		catch (URISyntaxException e) {
			throw new IOException(e.getMessage(), e);
		}
	}
	
	public InputStream getBinaryInputStream(URI uri) throws IOException {
		try {
			if (uri.getScheme().equals(scheme())) {
				String path = getPath(uri);

				for (URI dir : collect()) {
					URI full = getFullURI(path, dir);
					if (ctx.getResolverRegistry().exists(full)) {
						return ctx.getResolverRegistry().getInputStream(getFullURI(getBinaryPath(uri), dir));
					}
				}
			}
			throw new IOException("Module " + uri.getPath() + " not found");
		} 
		catch (URISyntaxException e) {
			throw new IOException(e.getMessage(), e);
		}
	}

	public String scheme() {
		return "rascal";
	}

	public OutputStream getOutputStream(URI uri, boolean append)
			throws IOException {
		try {
			if (uri.getScheme().equals(scheme())) {
				String path = getPath(uri);

				for (URI dir : collect()) {
					URI full = getFullURI(path, dir);
					if (ctx.getResolverRegistry().exists(full)) {
						return ctx.getResolverRegistry().getOutputStream(full, append);
					}
				}
			}
			throw new IOException("Module " + uri.getPath() + " not found");
		} 
		catch (URISyntaxException e) {
			throw new IOException(e.getMessage(), e);
		}
	}
	
	public OutputStream getBinaryOutputStream(URI uri) throws IOException {
		try {
			if (uri.getScheme().equals(scheme())) {
				String path = getPath(uri);

				for (URI dir : collect()) {
					URI full = getFullURI(path, dir);
					if (ctx.getResolverRegistry().exists(full)) {
						return ctx.getResolverRegistry().getOutputStream(getFullURI(getBinaryPath(uri), dir), false);
					}
				}
			}
			throw new IOException("Module " + uri.getPath() + " not found");
		} 
		catch (URISyntaxException e) {
			throw new IOException(e.getMessage(), e);
		}
	}
	
	public boolean isDirectory(URI uri) {
		System.err.println("isDirectory: " + uri.getPath());
		try {
			if (uri.getScheme().equals(scheme())) {
				String path = getPath(uri);
				
				for (URI dir : collect()) {
					URI full = getFullURI(path, dir);
					System.err.println("full = " + full.getPath());
					if (ctx.getResolverRegistry().exists(full) &&
						ctx.getResolverRegistry().isDirectory(full)) {
						return true;
					}
				}
			}
			return false;
		} 
		catch (URISyntaxException e) {
			return false;
		}
	}
	
	public boolean isFile(URI uri) {
		try {
			if (uri.getScheme().equals(scheme())) {
				String path = getPath(uri);
				
				for (URI dir : collect()) {
					URI full = getFullURI(path, dir);
					if (ctx.getResolverRegistry().exists(full) &&
					    ctx.getResolverRegistry().isFile(full)) {
						return true;
					}
				}
			}
			return false;
		} 
		catch (URISyntaxException e) {
			return false;
		}
	}
	
	public long lastModified(URI uri) throws IOException {
		try {
			if (uri.getScheme().equals(scheme())) {
				String path = getPath(uri);
				
				for (URI dir : collect()) {
					URI full = getFullURI(path, dir);
					if (ctx.getResolverRegistry().exists(full)) {
						return ctx.getResolverRegistry().lastModified(full);
					}
				}
			}
			throw new UnsupportedSchemeException(uri.toString());
		} 
		catch (URISyntaxException e) {
			throw new BadURIException(e);
		}
	}
	
	public String[] listEntries(URI uri) throws IOException {
		try {
			if (uri.getScheme().equals(scheme())) {
				String path = getPath(uri);
				
				for (URI dir : collect()) {
					URI full = getFullURI(path, dir);
					if (ctx.getResolverRegistry().exists(full)) {
						return ctx.getResolverRegistry().listEntries(full);
					}
				}
			}
			throw new UnsupportedSchemeException(uri.toString());
		} 
		catch (URISyntaxException e) {
			throw new BadURIException(e);
		}
	}

	public boolean mkDirectory(URI uri) throws IOException {
		try {
			if (uri.getScheme().equals(scheme())) {
				String path = getPath(uri);
				
				for (URI dir : collect()) {
					URI full = getFullURI(path, dir);
						return ctx.getResolverRegistry().mkDirectory(full);
				}
			}
			return false;
		} 
		catch (URISyntaxException e) {
			return false;
		}
	}

	public String absolutePath(URI uri) {
		try {
			return ctx.getResolverRegistry().absolutePath(uri);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
