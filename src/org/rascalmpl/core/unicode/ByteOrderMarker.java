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

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class ByteOrderMarker {
	private Charset charset;
	private Charset group;
	private int[] header;
	private boolean shouldBOM = true;
	public ByteOrderMarker(Charset charset, Charset group, int[] header) {
		this.group = group;
		this.charset = charset;
		this.header = header;
	}
	public ByteOrderMarker(Charset charset, Charset group, boolean shouldBom, int[] header) {
		this(charset, group, header);
		this.shouldBOM = shouldBom;
	}
	
	public Charset getCharset() {
		return charset;
	}
	public int[] getHeader() {
		return header;
	}
	public int getHeaderLength() {
		return header.length;
	}
	public Charset getGroup() {
		return group;
	}
	public boolean shouldBom() {
		return shouldBOM;
	}

	public static final ByteOrderMarker UTF8 = new ByteOrderMarker(Charset.forName("UTF-8"), null, false, new int[] {0xEF, 0xBB, 0xBF});
	public static final ByteOrderMarker UTF16LE = new ByteOrderMarker(Charset.forName("UTF-16BE"),Charset.forName("UTF-16"), new int[] {0xFE, 0xFF});
	public static final ByteOrderMarker UTF16BE = new ByteOrderMarker(Charset.forName("UTF-16LE"),Charset.forName("UTF-16"), new int[] {0xFF, 0xFE});
	public static final ByteOrderMarker UTF32LE = new ByteOrderMarker(Charset.forName("UTF-32LE"),Charset.forName("UTF-32"), new int[] {0xFF, 0xFE, 0x00, 0x00});
	public static final ByteOrderMarker UTF32BE = new ByteOrderMarker(Charset.forName("UTF-32BE"),Charset.forName("UTF-32"), new int[] {0x00, 0x00, 0xFE, 0xFF});
	
	private static final Map<Charset, ByteOrderMarker> names;
	
	static {
		names = new HashMap<Charset, ByteOrderMarker>();
		addBOM(UTF8);
		addBOM(UTF16LE);
		addBOM(UTF16BE);
		addBOM(UTF32LE);
		addBOM(UTF32BE);
		names.put(UTF16BE.group, UTF16BE);
		names.put(UTF32BE.group, UTF32BE);
	}
	private static void addBOM(ByteOrderMarker bom) {
		names.put(bom.charset, bom);
	}
	
	public static ByteOrderMarker fromString(String name) {
		return names.get(Charset.forName(name));
	}
	
	public boolean matches(byte[] b) {
		return matches(b, b.length);
	}
	public boolean matches(byte[] detectionBuffer, int len) {
		if (header.length > len) return false;
		for (int i = 0; i < header.length; i++) {
			if (header[i] != (detectionBuffer[i] & 0xff))
				return false;
		}
		return true;
	}

}