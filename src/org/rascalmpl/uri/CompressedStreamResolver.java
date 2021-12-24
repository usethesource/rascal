package org.rascalmpl.uri;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

import com.github.luben.zstd.ZstdInputStream;
import com.github.luben.zstd.ZstdOutputStream;

import io.usethesource.vallang.ISourceLocation;

public class CompressedStreamResolver implements ISourceLocationInputOutput {
    private static final String ZSTD_COMPRESSION = "ZSTD";
    private final URIResolverRegistry registry;
    
    public CompressedStreamResolver(URIResolverRegistry registry) {
        this.registry = registry;
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

	private static final InputStream getInputStream(String compressionMethod, InputStream original) throws IOException, CompressorException {
	    if (compressionMethod == ZSTD_COMPRESSION) {
	        return new ZstdInputStream(original);
	    }
	    return new CompressorStreamFactory().createCompressorInputStream(compressionMethod, original);
	}
	
	@Override
	public InputStream getInputStream(ISourceLocation uri) throws IOException {
		InputStream result = registry.getInputStream(getActualURI(uri));
		if (result != null) {
			try {
				String detectedCompression = detectCompression(uri);
				if (detectedCompression == null) {
					// let's use the automatic detection of the Compressor Stream library factory
					return new CompressorStreamFactory().createCompressorInputStream(result);
				}
				return getInputStream(detectedCompression, result);
			} catch (CompressorException e) {
				result.close();
				throw new IOException("We cannot decompress this file.", e);
			}
		}
		return null;
	}
	
	private static final OutputStream getOutputStream(String compressionMethod, OutputStream original) throws IOException, CompressorException {
	    if (compressionMethod == ZSTD_COMPRESSION) {
	        return new ZstdOutputStream(original);
	    }
	    return new CompressorStreamFactory().createCompressorOutputStream(compressionMethod, original);
	}
	
	@Override
	public OutputStream getOutputStream(ISourceLocation uri, boolean append)
			throws IOException {
		OutputStream result = registry.getOutputStream(getActualURI(uri), append);
		if (result != null) {
			String detectedCompression = detectCompression(uri);
			if (detectedCompression == null) {
				throw new IOException("We could not detect the compression based on the extension.");
			}
			try {
				return new BufferedOutputStream(getOutputStream(detectedCompression, result));
			} catch (CompressorException e) {
				result.close();
				throw new IOException("We cannot compress this kind of file. (Only gz,xz,bz2 have write support)",e);
			}

		}
		return null;
	}
	
	
	private static String findExtension(String path) {
	    // combine lastIndex of and substring, and stop after 5 chars
	    char[] result = new char[5];
	    int slen = path.length();
	    int end = Math.min(5, slen);

	    path.getChars(slen - end, slen, result, 0);
	    for (int c = end - 1; c >= 0; c--) {
	        if (result[c] == '.') {
	            return new String(result, c + 1, end - (c + 1));
	        }
	    }
	    return null;
	}
	
	private String detectCompression(ISourceLocation uri) throws IOException {
	    String extension = findExtension(uri.getPath());
		if (extension != null) {
			switch (extension) {
			case "gz": return CompressorStreamFactory.GZIP;
			case "bz": return CompressorStreamFactory.BZIP2;
			case "bz2": return CompressorStreamFactory.BZIP2;
			case "lzma" : return CompressorStreamFactory.LZMA;
			case "Z" : return CompressorStreamFactory.Z;
			case "xz": return CompressorStreamFactory.XZ;
			case "zst": return ZSTD_COMPRESSION;
			case "7z":
			case "zip":
			case "rar":
			case "tar":
			case "jar":
					throw new IOException("We only support compression formats. The extension " + extension + " is an archive format, which could decompress to more than one file.");
			}
		}
		return null;
	}

	@Override
	public boolean exists(ISourceLocation uri) {
		try {
			return registry.exists(getActualURI(uri));
		} catch (IOException e) {
			return false;
		}
	}
	
	@Override
	public Charset getCharset(ISourceLocation uri) throws IOException {
		return registry.getCharset(getActualURI(uri));
	}
	
	@Override
	public boolean isDirectory(ISourceLocation uri) {
		try {
			return registry.isDirectory(getActualURI(uri));
		} catch (IOException e) {
			return false;
		}
	}
	
	@Override
	public boolean isFile(ISourceLocation uri) {
		try {
			return registry.isFile(getActualURI(uri));
		} catch (IOException e) {
			return false;
		}
	}
	
	@Override
	public long lastModified(ISourceLocation uri) throws IOException {
		return registry.lastModified(getActualURI(uri));
	}
	
	@Override
	public void setLastModified(ISourceLocation uri, long timestamp) throws IOException {
	    registry.setLastModified(getActualURI(uri), timestamp);
	}
	
	@Override
	public String[] list(ISourceLocation uri) throws IOException {
		return registry.listEntries(getActualURI(uri));
	}
	
	@Override
	public void mkDirectory(ISourceLocation uri) throws IOException {
	    registry.mkDirectory(getActualURI(uri));
	}
	
	@Override
	public void remove(ISourceLocation uri) throws IOException {
	    registry.remove(getActualURI(uri), false);
	}
	
	@Override
	public boolean supportsHost() {
		return true;
	}
}
