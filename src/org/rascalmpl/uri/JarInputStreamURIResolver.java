package org.rascalmpl.uri;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class JarInputStreamURIResolver implements IURIInputStreamResolver {
	private URIResolverRegistry registry;
	private URI jarURI;
	private String scheme;

	public JarInputStreamURIResolver(URI jarURI, URIResolverRegistry registry) {
		this.jarURI = jarURI;
		this.registry = registry;
		this.scheme = "jarstream+\"" + jarURI.toASCIIString() + "\"";
	}
	
	private InputStream getJarStream() throws IOException {
		return registry.getInputStream(jarURI);
	}

	@Override
	public InputStream getInputStream(URI uri) throws IOException {
		try (JarInputStream stream = new JarInputStream(getJarStream())) {
			JarEntry next = null;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			
			while ((next = stream.getNextJarEntry()) != null) {
				if (next.getName().equals(uri.getPath())) {
					byte[] buf = new byte[1024];
					int len;
					while ((len = stream.read(buf)) > 0) {
						out.write(buf, 0, len);
					}
					
					return new ByteArrayInputStream(out.toByteArray());
				}
			}
		}
		
		return null;
	}

	@Override
	public Charset getCharset(URI uri) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exists(URI uri) {
		try (JarInputStream stream = new JarInputStream(getJarStream())) {
			JarEntry je = null;

			while ((je = stream.getNextJarEntry()) != null) {
				if (je.getName().equals(uri.getPath())) {
					return true;
				}
			}
		} catch (IOException e) {
			return false;
		} 
		
		return false;
	}

	@Override
	public long lastModified(URI uri) throws IOException {
		try (JarInputStream stream = new JarInputStream(getJarStream())) {
			JarEntry je = null;

			while ((je = stream.getNextJarEntry()) != null) {
				if (je.getName().equals(uri.getPath())) {
					return je.getTime();
				}
			}
		} 
		
		throw new FileNotFoundException(uri.toString());
	}

	@Override
	public boolean isDirectory(URI uri) {
		try (JarInputStream stream = new JarInputStream(getJarStream())) {
			JarEntry je = null;

			while ((je = stream.getNextJarEntry()) != null) {
				if (je.getName().equals(uri.getPath())) {
					return je.isDirectory();
				}
			}
		} catch (IOException e) {
			return false;
		}
		
		return false;
	}

	@Override
	public boolean isFile(URI uri) {
		try (JarInputStream stream = new JarInputStream(getJarStream())) {
			JarEntry je = null;

			while ((je = stream.getNextJarEntry()) != null) {
				if (je.getName().equals(uri.getPath())) {
					return !je.isDirectory();
				}
			}
		} catch (IOException e) {
			return false;
		}
		
		return false;
	}

	@Override
	public String[] listEntries(URI uri) throws IOException {
		String path = uri.getPath();

		if (!path.endsWith("/") && !path.isEmpty()) {
			path = path + "/";
		}

		ArrayList<String> matchedEntries = new ArrayList<String>();

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
						matchedEntries.add(result);
					} else {
						result = result.substring(0, index);
						boolean entryPresent = false;
						for (Iterator<String> it = matchedEntries.iterator(); it.hasNext(); ) {
							if (result.equals(it.next())) {
								entryPresent = true;
								break;
							}
						}
						if (!entryPresent) {
							matchedEntries.add(result);
						}
					}
				}
			}
		}

		String[] listedEntries = new String[matchedEntries.size()];
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
