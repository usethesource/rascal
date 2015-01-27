package org.rascalmpl.uri;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.eclipse.imp.pdb.facts.ISourceLocation;

public class CompressedStreamResolver implements ISourceLocationInputOutput {
	
	private final URIResolverRegistry ctx;

	public CompressedStreamResolver(URIResolverRegistry ctx) {
		this.ctx = ctx;
	}
	
	@Override
	public String scheme() {
		return "compressed";
	}
	
	private ISourceLocation getActualURI(ISourceLocation wrappedURI) throws IOException {
		String scheme = wrappedURI.getScheme();
		if (scheme.length() <= "compressed+".length()) {
			throw new IOException("Invalid scheme: \"" + scheme +"\"");
		}
		String actualScheme = scheme.substring("compressed+".length());
		try {
			return URIUtil.changeScheme(wrappedURI, actualScheme);
		} catch (URISyntaxException e) {
			throw new IOException("Invalid URI: \"" + wrappedURI +"\"", e);
		}
	}
	
	@Override
	public InputStream getInputStream(ISourceLocation uri) throws IOException {
		InputStream result = ctx.getInputStream(getActualURI(uri));
		if (result != null) {
			try {
				String detectedCompression = detectCompression(uri);
				if (detectedCompression == null) {
					// let's use the automatic detection of the Compressor Stream library factory
					return new CompressorStreamFactory().createCompressorInputStream(new BufferedInputStream(result));
				}
				return new CompressorStreamFactory().createCompressorInputStream(detectedCompression, result);
			} catch (CompressorException e) {
				result.close();
				throw new IOException("We cannot decompress this file.", e);
			}
		}
		return null;
	}
	
	@Override
	public OutputStream getOutputStream(ISourceLocation uri, boolean append)
			throws IOException {
		OutputStream result = ctx.getOutputStream(getActualURI(uri), append);
		if (result != null) {
			String detectedCompression = detectCompression(uri);
			if (detectedCompression == null) {
				throw new IOException("We could not detect the compression based on the extension.");
			}
			try {
				return new BufferedOutputStream(new CompressorStreamFactory().createCompressorOutputStream(detectedCompression, result));
			} catch (CompressorException e) {
				result.close();
				throw new IOException("We cannot compress this kind of file. (Only gz,xz,bz2 have write support)",e);
			}

		}
		return null;
	}
	
	
	private final Pattern getExtension = Pattern.compile("\\.([a-zA-Z0-9\\-]*)$");
	private String detectCompression(ISourceLocation uri) throws IOException {
		Matcher m = getExtension.matcher(uri.getPath());
		if (m.find()) {
			switch (m.group(1)) {
			case "gz": return CompressorStreamFactory.GZIP;
			case "bz": return CompressorStreamFactory.BZIP2;
			case "bz2": return CompressorStreamFactory.BZIP2;
			case "lzma" : return CompressorStreamFactory.LZMA;
			case "Z" : return CompressorStreamFactory.Z;
			case "xz": return CompressorStreamFactory.XZ;
			case "7z":
			case "zip":
			case "rar":
			case "tar":
			case "jar":
					throw new IOException("We only support compression formats. The extension " + m.group(1) + " is an archive format, which could decompress to more than one file.");
			}
		}
		return null;
	}

	@Override
	public boolean exists(ISourceLocation uri) {
		try {
			return ctx.exists(getActualURI(uri));
		} catch (IOException e) {
			return false;
		}
	}
	
	@Override
	public Charset getCharset(ISourceLocation uri) throws IOException {
		return ctx.getCharset(getActualURI(uri));
	}
	
	@Override
	public boolean isDirectory(ISourceLocation uri) {
		try {
			return ctx.isDirectory(getActualURI(uri));
		} catch (IOException e) {
			return false;
		}
	}
	
	@Override
	public boolean isFile(ISourceLocation uri) {
		try {
			return ctx.isFile(getActualURI(uri));
		} catch (IOException e) {
			return false;
		}
	}
	
	@Override
	public long lastModified(ISourceLocation uri) throws IOException {
		return ctx.lastModified(getActualURI(uri));
	}
	
	@Override
	public String[] list(ISourceLocation uri) throws IOException {
		return ctx.listEntries(getActualURI(uri));
	}
	
	@Override
	public void mkDirectory(ISourceLocation uri) throws IOException {
		ctx.mkDirectory(getActualURI(uri));
	}
	
	@Override
	public void remove(ISourceLocation uri) throws IOException {
		ctx.remove(getActualURI(uri));
	}
	
	@Override
	public boolean supportsHost() {
		return true;
	}
}
