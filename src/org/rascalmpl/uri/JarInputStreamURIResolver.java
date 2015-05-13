package org.rascalmpl.uri;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
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

public class JarInputStreamURIResolver extends JarURIResolver {
	private final URIResolverRegistry registry = URIResolverRegistry.getInstance();
	private final ISourceLocation jarURI;
	private final String scheme;
	private final JarInputStreamTreeHierachy index;

	public JarInputStreamURIResolver(ISourceLocation jarURI) throws IOException {
		this.jarURI = jarURI;
		scheme = "jarstream+" + UUID.randomUUID();
	  index = new JarInputStreamTreeHierachy(getJarStream());
	}
	
	@Override
	protected File getJar(ISourceLocation uri) throws IOException {
	  return new File(jarURI.getPath());
	}
	
	@Override
	protected String getPath(ISourceLocation uri) {
	  String path = uri.getPath();
	  if (path == null || path.isEmpty() || path.equals("/")) {
	    return "";
	  }
    while (path.startsWith("/")) {
      path = path.substring(1);
    }
    return path;
	}
	
	@Override
	protected JarTreeHierachy getFileHierchyCache(File jar) {
	  return index;
	}
	
	public InputStream getJarStream() throws IOException {
		return registry.getInputStream(jarURI);
	}

	
	private JarEntry getEntry(JarInputStream stream, ISourceLocation subPath) throws IOException {
		int pos = index.getPosition(getPath(subPath));
		
		if (pos != -1) {
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
		final JarInputStream stream = new JarInputStream(getJarStream());
		if (getEntry(stream, uri) != null) {
		  // ideally we could just return the stream
		  // except that the JarInputStream doesn't hold the InputStream contract
		  // mainly, the read(byte[],int,int) works correctly, as that it stops at the boundary of the file
		  // but the read() doesn't limit itself to the end of the nested file, but works on the whole stream.
			return new InputStream() {

        @Override
        public int read() throws IOException {
          byte[] result = new byte[1];
          if (stream.read(result, 0, 1) != -1) {
            return result[0] & 0xFF;
          }
          return -1;
        }
        @Override
        public int read(byte[] b, int off, int len) throws IOException {
          return stream.read(b, off, len);
        }
        @Override
        public int read(byte[] b) throws IOException {
          return stream.read(b, 0, b.length);
        }
        @Override
        public void close() throws IOException {
          stream.close();
        }
        @Override
        public int available() throws IOException {
          return stream.available();
        }
        @Override
        public long skip(long n) throws IOException {
          return stream.skip(n);
        }
			};
		}
		return null;
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
