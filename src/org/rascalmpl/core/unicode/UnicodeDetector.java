/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Davy Landman  - Davy.Landman@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.core.unicode;

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
	 * <b>Warning, don't fallback to UTF8, if this method didn't return UTF8 as a encoding
	 * it really isn't UTF8! Try Charset.defaultCharset().</b>
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
			if (0x01 <= c0 && c0 <= 0x7F) {
				// one byte UTF8
				i++;
				continue;
			}
			int c1 = buffer[i + 1] & 0xff;
			if (0xC0 <= c0 && c0 < 0xE0 
					&& (c1 & 0xC0) == 0x80) {
				// two byte UTF8
				i += 2;
				continue;
			}
			int c2 = buffer[i + 2] & 0xff;
			if (0xE0 <= c0 && c0 < 0xF0 
					&& (c1 & 0xC0) == 0x80
					&& (c2 & 0xC0) == 0x80) {
				if (c0 == 0xED && 0xA0 <= c1 && c1 <= 0xBF && 0x80 <= c2 && c2 <= 0xBF) {
					// this is a UTF-16 surrogate pair, which are not allowed in UTF-8
					match = false;
					break;
				}
				else {
					// three byte UTF8
					i += 3;
					continue;
				}
			}
	
			int c3 = buffer[i + 3] & 0xff;
			if (0xF0 <= c0 && c0 < 0xF8 
					&& (c1 & 0xC0) == 0x80
					&& (c2 & 0xC0) == 0x80
					&& (c3 & 0xC0) == 0x80) {
				// four byte UTF8
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
	
	public static boolean isAmbigiousBOM(Charset a, Charset b) {
		boolean isUTF32LE = a.name().equals("UTF-32LE") || b.name().equals("UTF-32LE");
		boolean isUTF16LE = a.name().equals("UTF-16LE") || b.name().equals("UTF-16LE");
		boolean isUTF16 = a.name().equals("UTF-16") || b.name().equals("UTF-16");
		return isUTF32LE && (isUTF16 || isUTF16LE);

	}

	public static int getMaximumBOMLength() {
		return maximumBOMLength;
	}

	public static int getSuggestedDetectionSampleSize() {
		return 32;
	}

	/**
	 * Try to estimate if the content might be incoded in UTF-8/16/32.
	 * <b>Warning, if this method does not return a charset, it can't be UTF8 or UTF32.
	 * It might be UTF-16 (unlickely) or a strange codepoint.
	 * </b>
	 */
	public static Charset estimateCharset(InputStream in) throws IOException {
		byte[] buffer = new byte[getSuggestedDetectionSampleSize()];
		int totalRead = in.read(buffer);
		ByteOrderMarker bom = detectBom(buffer, totalRead);
		if (bom != null)
			return bom.getCharset();
		return detectByContent(buffer, totalRead);
	}

}
