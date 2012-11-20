/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
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
	private static char[] toChar(Reader reader) throws IOException {
		ArrayList<char[]> segments = new ArrayList<char[]>();
		
		// Gather segments.
		int nrOfWholeSegments = -1;
		int bytesRead;
		do{
			char[] segment = new char[STREAM_READ_SEGMENT_SIZE];
			bytesRead = reader.read(segment, 0, STREAM_READ_SEGMENT_SIZE);
			
			segments.add(segment);
			++nrOfWholeSegments;
		}while(bytesRead == STREAM_READ_SEGMENT_SIZE);
		
		// Glue the segments together.
		char[] segment = segments.get(nrOfWholeSegments);
		char[] input;
		if(bytesRead != -1){
			input = new char[(nrOfWholeSegments * STREAM_READ_SEGMENT_SIZE) + bytesRead];
			System.arraycopy(segment, 0, input, (nrOfWholeSegments * STREAM_READ_SEGMENT_SIZE), bytesRead);
		}else{
			input = new char[(nrOfWholeSegments * STREAM_READ_SEGMENT_SIZE)];
		}
		for(int i = nrOfWholeSegments - 1; i >= 0; --i){
			segment = segments.get(i);
			System.arraycopy(segment, 0, input, (i * STREAM_READ_SEGMENT_SIZE), STREAM_READ_SEGMENT_SIZE);
		}
		
		return input;
	}
}
