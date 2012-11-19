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

	private static int detectionBufferSize = 32; // for our content detection this should be more than enough
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

	/**
	 * Try to detect an encoding, only UTF8 and UTF32 we can try to detect.
	 * Other detection are to intensive.
	 * @return either the detected Charset or null
	 */
	private static Charset detectByContent(byte[] buffer, int bufferSize) {
		// first, lets see if it might be a valid UTF8 (biggest chance)
		// http://www.w3.org/International/questions/qa-forms-utf-8
		// we translate this Regular Expression to a while loop
		// using the help of
		// http://stackoverflow.com/questions/1031645/how-to-detect-utf-8-in-plain-c
		boolean match= true;
		int i = 0;
		while (match && i + 3 < bufferSize) {
			int c0 = buffer[i] & 0xff;
			if (c0 == 0x09 || c0 == 0x0A || c0 == 0x0D || 
					(0x20 <= c0 && c0 <= 0x7E)) {
				// just plain ASCII
				i++;
				continue;
			}
			int c1 = buffer[i + 1] & 0xff;
			if ((0xC2 <= c0 && c0 <= 0xDF) &&
					(0x80 <= c1 && c1 <= 0xBF)) {
				// non-overlong 2-byte
				i += 2;
				continue;
			}
			int c2 = buffer[i + 2] & 0xff;
	        if( (// excluding overlongs
	                c0 == 0xE0 &&
	                (0xA0 <= c1 && c1 <= 0xBF) &&
	                (0x80 <= c2 && c2 <= 0xBF)
	            ) ||
	            (// straight 3-byte
	                ((0xE1 <= c0 && c0 <= 0xEC) ||
	                    c0 == 0xEE ||
	                    c0 == 0xEF) &&
	                (0x80 <= c1 && c1 <= 0xBF) &&
	                (0x80 <= c2 && c2 <= 0xBF)
	            ) ||
	            (// excluding surrogates
	                c0 == 0xED &&
	                (0x80 <= c1 && c1 <= 0x9F) &&
	                (0x80 <= c2 && c2 <= 0xBF)
	            )
	        ) {
				i += 3;
	            continue;
	        }

			int c3 = buffer[i + 3] & 0xff;
	        if( (// planes 1-3
	                c0 == 0xF0 &&
	                (0x90 <= c1 && c1 <= 0xBF) &&
	                (0x80 <= c2 && c2 <= 0xBF) &&
	                (0x80 <= c3 && c3 <= 0xBF)
	            ) ||
	            (// planes 4-15
	                (0xF1 <= c0 && c0 <= 0xF3) &&
	                (0x80 <= c1 && c1 <= 0xBF) &&
	                (0x80 <= c2 && c2 <= 0xBF) &&
	                (0x80 <= c3 && c3 <= 0xBF)
	            ) ||
	            (// plane 16
	                c0 == 0xF4 &&
	                (0x80 <= c1 && c1 <= 0x8F) &&
	                (0x80 <= c2 && c2 <= 0xBF) &&
	                (0x80 <= c3 && c3 <= 0xBF)
	            )
	        ) {
				i += 4;
	            continue;
	        }	
	        match = false;
	        break;
		}
		if (match) 
			return Charset.forName("UTF8");
		// the other thing we can check if it might be a  UTF32 file
		// they must be of the pattern 0x00 0x10 0x.. 0x.. (BE) or 0x.. 0x.. 0x10 0x00 (LE)
		match = true;
		for (i = 0; i + 1 < bufferSize && match; i+=2) {
			match = (buffer[i] & 0xff) == 0 && (buffer[i + 1] & 0xff) == 0x10;
		}
		if (match)
			return Charset.forName("UTF-32BE");
		match = true;
		for (i = 2; i + 1 < bufferSize && match; i+=2) {
			match = (buffer[i] & 0xff) == 0x10 && (buffer[i + 1] & 0xff) == 0x0;
		}
		if (match)
			return Charset.forName("UTF-32LE");
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
