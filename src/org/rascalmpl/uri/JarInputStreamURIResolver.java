package org.rascalmpl.uri;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.eclipse.imp.pdb.facts.ISourceLocation;

public class JarInputStreamURIResolver implements ISourceLocationInput {
	private URIResolverRegistry registry = URIResolverRegistry.getInstance();
	private ISourceLocation jarURI;
	private String scheme;
	private Map<String, Integer> index;

	public JarInputStreamURIResolver(ISourceLocation jarURI) {
		this.jarURI = jarURI;
		this.scheme = "jarstream+" + UUID.randomUUID();
		buildIndex();
	}
	
	public InputStream getJarStream() throws IOException {
		return registry.getInputStream(jarURI);
	}

	private void buildIndex() {
		index = new HashMap<>();
		
		try (JarInputStream stream = new JarInputStream(getJarStream())) {
			JarEntry next = null;
			int pos = 0;
			
			while ((next = stream.getNextJarEntry()) != null) {
				index.put(next.getName(), pos++);
			}
		} catch (IOException e) {
			e.printStackTrace();
			index = null;
		}		
	}
	
	private JarEntry getEntry(JarInputStream stream, String file) throws IOException {
		if (file.startsWith("/")) {
			file = file.substring(1);
		}
		Integer pos = index == null ? null : index.get(file);
		
		if (pos != null) {
			JarEntry entry;
			do {
				entry = stream.getNextJarEntry();
			} while (pos-- > 0);
			
			return entry;
		}
		
		return null;
	}
	
	@Override
	public InputStream getInputStream(ISourceLocation uri) throws IOException {
		try (JarInputStream stream = new JarInputStream(getJarStream())) {
			if (getEntry(stream, uri.getPath()) != null) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				byte[] buf = new byte[1024];
				int len;
				while ((len = stream.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				
				return new ByteArrayInputStream(out.toByteArray());
			}
		}
		
		return null;
	}

	@Override
	public Charset getCharset(ISourceLocation uri) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exists(ISourceLocation uri) {
		String file = uri.getPath();
		if (file.startsWith("/")) {
			file = file.substring(1);
		}
		return index.containsKey(file);
	}

	@Override
	public long lastModified(ISourceLocation uri) throws IOException {
		try (JarInputStream stream = new JarInputStream(getJarStream())) {
			JarEntry je = getEntry(stream, uri.getPath());
			if (je != null) {
				return je.getTime();
			}
		}
		
		throw new FileNotFoundException(uri.toString());
	}

	@Override
	public boolean isDirectory(ISourceLocation uri) {
		try (JarInputStream stream = new JarInputStream(getJarStream())) {
			JarEntry je = getEntry(stream, uri.getPath());
			if (je != null) {
				return je.isDirectory();
			}
		} catch (IOException e) {
			return false;
		}
		
		return false;
	}

	@Override
	public boolean isFile(ISourceLocation uri) {
		return !isDirectory(uri);
	}

	@Override
	public ISourceLocation[] list(ISourceLocation uri) throws IOException {
		String path = uri.getPath();

		if (!path.endsWith("/") && !path.isEmpty()) {
			path = path + "/";
		}

		ArrayList<ISourceLocation> matchedEntries = new ArrayList<ISourceLocation>();

		try (JarInputStream stream = new JarInputStream(getJarStream())) {
			JarEntry je = null;

			while ((je = stream.getNextJarEntry()) != null) {
				String name = je.getName();

				if (name.equals(path)) {
					continue;
				}
				int index = name.indexOf(path);

				if (index == 0) {
					String result = name.substring(path.length());

					index = result.indexOf("/");

					if (index == -1) {
						matchedEntries.add(URIUtil.getChildLocation(uri, result));
					} else {
						result = result.substring(0, index);
						boolean entryPresent = false;
						for (Iterator<ISourceLocation> it = matchedEntries.iterator(); it.hasNext(); ) {
							if (result.equals(URIUtil.getLocationName(it.next()))) {
								entryPresent = true;
								break;
							}
						}
						if (!entryPresent) {
							matchedEntries.add(URIUtil.getChildLocation(uri, result));
						}
					}
				}
			}
		}

		ISourceLocation[] listedEntries = new ISourceLocation[matchedEntries.size()];
		return matchedEntries.toArray(listedEntries);
	}

	@Override
	public String scheme() {
		return scheme;
	}

	@Override
	public boolean supportsHost() {
		return false;
	}
	
	
}
