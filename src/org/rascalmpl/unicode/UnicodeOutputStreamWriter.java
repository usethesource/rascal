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
package org.rascalmpl.unicode;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class UnicodeOutputStreamWriter extends OutputStreamWriter {

	public UnicodeOutputStreamWriter(OutputStream out, String charsetName) throws IOException {
		super(out, getCharset(charsetName));
		
		ByteOrderMarker bom = ByteOrderMarker.fromString(charsetName);
	 	if (bom != null && bom.shouldBom()) {
	 	    writeBOM(bom, out);
		}
	}

	public UnicodeOutputStreamWriter(OutputStream out, String charsetName, boolean append) throws IOException {
	    super(out, getCharset(charsetName));
	    
	    if (!append) {
	        ByteOrderMarker bom = ByteOrderMarker.fromString(charsetName);
	        if (bom != null && bom.shouldBom()) {
	            writeBOM(bom, out);
	        } 
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

	void writeBOM(ByteOrderMarker bom, OutputStream out) throws IOException {
	    for (int b: bom.getHeader()) {
	        out.write(b);
	    }
	}
}
