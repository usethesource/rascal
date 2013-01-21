/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.parser.gtd.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;

import org.rascalmpl.parser.gtd.util.ArrayList;
import org.rascalmpl.unicode.UnicodeInputStreamReader;

public class InputConverter{
	private final static int STREAM_READ_SEGMENT_SIZE = 8192;
	
	private InputConverter(){
		super();
	}
	
	public static char[] toChar(String s){
		return s.toCharArray();
	}
	
	// NOTE: The user has to close the stream.
	public static char[] toChar(InputStream inputStream, Charset charset) throws IOException{
		return toChar(new UnicodeInputStreamReader(inputStream, charset));
	}
	
	// NOTE: The user has to close the stream.
	public static char[] toChar(Reader reader) throws IOException {
		ArrayList<char[]> segments = new ArrayList<char[]>();
		ArrayList<Integer> segmentLengths = new ArrayList<Integer>();
		
		// Gather segments.
		int charsRead = 0;
		int totalNrOfChars = 0;
		do{
			char[] segment = new char[STREAM_READ_SEGMENT_SIZE];
			charsRead = reader.read(segment, 0, STREAM_READ_SEGMENT_SIZE);

			if(charsRead > 0) {
				segments.add(segment);
				segmentLengths.add(charsRead);
				totalNrOfChars += charsRead;
			}
		}while(charsRead != -1);

		assert reader.read() == -1;
		
		// Glue the segments together.
		char[] input = new char[totalNrOfChars];
		int pos = 0;
		for(int i = 0; i < segments.size(); i++) {
			char segment[] = segments.get(i);
			int length = segmentLengths.get(i);
			System.arraycopy(segment, 0, input, pos, length);
			pos += length;
		}
		assert pos == totalNrOfChars;
		return input;
	}
}
