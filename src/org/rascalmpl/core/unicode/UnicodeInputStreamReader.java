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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;


public class UnicodeInputStreamReader extends Reader {
	private Reader wrapped;
	private InputStream original;
	private String encoding;
	
	public UnicodeInputStreamReader(InputStream in)  {
		original = in;
	}
	
	public UnicodeInputStreamReader(InputStream in, String encoding) {
		original = in;
		this.encoding = encoding;
	}
	
	public UnicodeInputStreamReader(InputStream in, Charset charset) {
		this(in, charset == null ? null : charset.name());
	}
	
	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		if (wrapped == null) {
			if (encoding != null) {
				// we have an encoding, so lets just skip the possible BOM
				wrapped = removeBOM(original, encoding);
				original = null;
			}
			else {
				// we have to try and detect the decoding
				wrapped = detectCharset(original);
				original = null;
			}
		}
		return wrapped.read(cbuf, off, len);
	}

	@Override
	public void close() throws IOException {
		if (wrapped != null) {
			wrapped.close();
		}
		else {
			original.close();
		}
	}
	
	private static Reader removeBOM(InputStream in, String encoding) throws IOException {
		byte[] detectionBuffer = new byte[UnicodeDetector.getMaximumBOMLength()];
		int bufferSize = in.read(detectionBuffer);
		ByteOrderMarker b = UnicodeDetector.detectBom(detectionBuffer, bufferSize);
		if (b != null) {
			Charset ref = Charset.forName(encoding);
			if (UnicodeDetector.isAmbigiousBOM(b.getCharset(), ref)) {
				b = ByteOrderMarker.fromString(encoding);
			}
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

	private static Reader detectCharset(InputStream in) throws IOException {
		byte[] detectionBuffer = new byte[UnicodeDetector.getSuggestedDetectionSampleSize()];
		int bufferSize = in.read(detectionBuffer);
		ByteOrderMarker b =UnicodeDetector.detectBom(detectionBuffer, bufferSize);
		if (b != null) {
			// we have to remove the BOM from the front
			InputStream prefix = new ByteArrayInputStream(detectionBuffer, b.getHeaderLength(), bufferSize - b.getHeaderLength());
			return new InputStreamReader(new ConcatInputStream(prefix, in), b.getCharset());
		}
		Charset cs = UnicodeDetector.detectByContent(detectionBuffer, bufferSize);
		if (cs == null) {
			cs = Charset.defaultCharset();
		}
		InputStream prefix = new ByteArrayInputStream(detectionBuffer, 0, bufferSize);
		return new InputStreamReader(new ConcatInputStream(prefix, in), cs);
	}
}
