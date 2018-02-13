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
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class UnicodeOutputStreamWriter extends OutputStreamWriter {
	private boolean firstWrite;
	private ByteOrderMarker bom;
	private OutputStream out;

	public UnicodeOutputStreamWriter(OutputStream out, String charsetName)
			throws UnsupportedEncodingException {
		super(out, getCharset(charsetName));
		bom = ByteOrderMarker.fromString(charsetName);
	 	if (bom != null && bom.shouldBom()) {
			firstWrite = true;
			this.out = out;
		}
	 	else {
	 		firstWrite = false;
	 	}
	}

	public UnicodeOutputStreamWriter(OutputStream out, String charsetName, boolean append) throws UnsupportedEncodingException {
		this(out, charsetName);
		if (append) {
			// no writing of bom in case of append
			firstWrite = false;
			this.out = null;
		}
		
	}

	private static Charset getCharset(String charsetName) throws UnsupportedEncodingException {
		ByteOrderMarker bom = ByteOrderMarker.fromString(charsetName);
		if (bom != null) {
			return bom.getCharset();
		}
		if (Charset.isSupported(charsetName)) {
			return Charset.forName(charsetName);
		}
		throw new UnsupportedEncodingException("Charset " + charsetName + " is not supported");
	}

	private void assureBOM() throws IOException {
		if (firstWrite) {
			firstWrite = false;
			for (int b: bom.getHeader()) {
				this.out.write(b);
			}
			out = null; // remove duplicate reference
		}
	}
	
	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		assureBOM();
		super.write(cbuf, off, len);
	}

	
	@Override
	public void write(int c) throws IOException {
		assureBOM();
		super.write(c);
	}
	
	@Override
	public void write(String str, int off, int len) throws IOException {
		assureBOM();
		super.write(str, off, len);
	}
}
