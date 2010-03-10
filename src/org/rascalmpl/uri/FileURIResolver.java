package org.rascalmpl.uri;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

public class FileURIResolver implements IURIInputStreamResolver, IURIOutputStreamResolver {
	public final static URI STDIN_URI;
	static{ // Stupid construction to fool the compiler (twice).
		URI stdURI = null;
		try{
			stdURI = new URI("file://-");
		}catch(URISyntaxException usex){
			// Ignore, never happens.
		}finally{
			STDIN_URI = stdURI;
		}
	}
	
	public static URI constructFileURI(String filename){
		try{
			if (filename == "-") {
				return FileURIResolver.STDIN_URI;
			}
			return new URI("file://" + (filename.startsWith("/") ? filename : "/"+filename));
		}catch(URISyntaxException usex){
			throw new BadURIException(usex);
		}
	}
	
	public FileURIResolver(){
		super();
	}
	
	public InputStream getInputStream(URI uri) throws IOException {
		String path = uri.getPath();
		if (path != null) {
			return new FileInputStream(path);
		}
		throw new IOException("uri has no path: " + uri);
	}
	
	public OutputStream getOutputStream(URI uri, boolean append) throws IOException {
		return new BufferedOutputStream(new FileOutputStream(uri.getPath(), append));
	}
	
	public String scheme() {
		return "file";
	}
}
