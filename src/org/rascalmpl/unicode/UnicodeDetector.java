package org.rascalmpl.unicode;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class UnicodeDetector {
	private static final int maximumBOMLength = 4;
	private static final ByteOrderMarker[] boms = {
		ByteOrderMarker.UTF8,
		ByteOrderMarker.UTF32BE, 
		ByteOrderMarker.UTF32LE, // 32 first, to avoid ambituity with the 16 LE
		ByteOrderMarker.UTF16BE,
		ByteOrderMarker.UTF16LE
	};
	
	

	/**
	 * Try to detect an encoding, only UTF8 and UTF32 we can try to detect.
	 * Other detection are to intensive.
	 * @return either the detected Charset or null
	 */
	public static Charset detectByContent(byte[] buffer, int bufferSize) {
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

	public static ByteOrderMarker detectBom(byte[] detectionBuffer, int bufferSize) {
		for (ByteOrderMarker b: boms) {
			if (b.matches(detectionBuffer, bufferSize))
				return b;
		}
		return null;
	}

	public static int getMaximumBOMLength() {
		return maximumBOMLength;
	}

	public static int getSuggestedDetectionSampleSize() {
		return 32;
	}

	public static Charset estimateCharset(InputStream in) throws IOException {
		byte[] buffer = new byte[getSuggestedDetectionSampleSize()];
		int totalRead = in.read(buffer);
		ByteOrderMarker bom = detectBom(buffer, totalRead);
		if (bom != null)
			return bom.getCharset();
		return detectByContent(buffer, totalRead);
	}

}
