package org.rascalmpl.unicode;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;


public class UnicodeInputStreamReader extends Reader {
	private static final int maximumBOMLength = 4;
	private static final ByteOrderMarker[] boms = {
		ByteOrderMarker.UTF8,
		ByteOrderMarker.UTF32BE, 
		ByteOrderMarker.UTF32LE, // 32 first, to avoid ambituity with the 16 LE
		ByteOrderMarker.UTF16BE,
		ByteOrderMarker.UTF16LE
	};
	private static final Charset fallbackCharset = Charset.forName("UTF8");
	
	private Reader wrapped;
	public UnicodeInputStreamReader(InputStream in) throws IOException {
		wrapped = detectCharset(in);
	}
	public UnicodeInputStreamReader(InputStream in, String encoding) throws IOException {
		wrapped = removeBOM(in, encoding);
	}
	
	private static class ConcatInputStream extends InputStream {
		private final InputStream first;
		private boolean firstEmpty;
		private final InputStream second;

		public ConcatInputStream(InputStream first, InputStream second) {
			this.first = first;
			this.firstEmpty = false;
			this.second = second;
		}

		@Override
		public int read() throws IOException {
			if (!firstEmpty) {
				int firstResult = first.read();
				if (firstResult != -1)  {
					return firstResult;
				} 
				else {
					firstEmpty = true;
				}
			}
			return second.read();
		}
		
		@Override
		public int read(byte[] b, int off, int len) throws IOException {
			// not strictly needed, but can speedup the concatting
			if (!firstEmpty) {
				int readTotal = first.read(b, off, len);
				if (readTotal == len) {
					return readTotal;
				}
				else {
					if (readTotal < 0)
						readTotal = 0;
					firstEmpty = true;
					int secondRead = second.read(b, off + readTotal, len - readTotal);
					if (readTotal == 0 && secondRead <= 0) {
						return -1;
					}
					if (secondRead < 0)
						return readTotal;
					else
						return readTotal + secondRead;
				}
				
			}
			return second.read(b, off, len);
		}
		
	}

	private static Reader removeBOM(InputStream in, String encoding) throws IOException {
		byte[] detectionBuffer = new byte[maximumBOMLength];
		int bufferSize = in.read(detectionBuffer);
		ByteOrderMarker b = detectBom(detectionBuffer, bufferSize);
		if (b != null) {
			Charset ref = Charset.forName(encoding);
			if (b.getCharset().equals(ref) || b.getGroup().equals(ref)) {
				InputStream prefix = new ByteArrayInputStream(detectionBuffer, b.getHeaderLength(), bufferSize - b.getHeaderLength());
				return new InputStreamReader(new ConcatInputStream(prefix, in), b.getCharset());
			}
			else {
				throw new UnsupportedEncodingException("The requested encoding was " + encoding + " but the file contained a BOM for " + b.getCharset().name() + ".");
			}
		}
		else {
			InputStream prefix = new ByteArrayInputStream(detectionBuffer, 0, bufferSize);
			return new InputStreamReader(new ConcatInputStream(prefix, in), encoding);
		}
	}

	private static int detectionBufferSize = 256;
	private static Reader detectCharset(InputStream in) throws IOException {
		byte[] detectionBuffer = new byte[detectionBufferSize];
		int bufferSize = in.read(detectionBuffer);
		ByteOrderMarker b =detectBom(detectionBuffer, bufferSize);
		if (b != null) {
			// we have to remove the BOM from the front
			InputStream prefix = new ByteArrayInputStream(detectionBuffer, b.getHeaderLength(), bufferSize - b.getHeaderLength());
			return new InputStreamReader(new ConcatInputStream(prefix, in), b.getCharset());
		}
		Charset cs = detectByContent(detectionBuffer, bufferSize);
		if (cs == null) {
			cs = fallbackCharset;
		}
		InputStream prefix = new ByteArrayInputStream(detectionBuffer, 0, bufferSize);
		return new InputStreamReader(new ConcatInputStream(prefix, in), cs);
	}

	private static Charset detectByContent(byte[] detectionBuffer, int bufferSize) {
		return null;
	}

	private static ByteOrderMarker detectBom(byte[] detectionBuffer, int bufferSize) {
		for (ByteOrderMarker b: boms) {
			if (b.matches(detectionBuffer, bufferSize))
				return b;
		}
		return null;
	}

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		return wrapped.read(cbuf, off, len);
	}

	@Override
	public void close() throws IOException {
		wrapped.close();
	}
}
