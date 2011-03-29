package org.rascalmpl.parser.gtd.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.rascalmpl.parser.gtd.util.ArrayList;

public class InputConverter{
	private final static int STREAM_READ_SEGMENT_SIZE = 8192;
	
	private InputConverter(){
		super();
	}
	
	public static char[] toChar(String s){
		return s.toCharArray();
	}
	
	// NOTE: The user has to close the stream.
	public static char[] toChar(InputStream inputStream) throws IOException{
		return toChar(new InputStreamReader(inputStream));
	}
	
	// NOTE: The user has to close the stream.
	public static char[] toChar(Reader reader) throws IOException{
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
	
	public static char[] toChar(File inputFile) throws IOException{
		int inputFileLength = (int) inputFile.length();
		char[] input = new char[inputFileLength];
		Reader in = null;
		try{
			in = new BufferedReader(new FileReader(inputFile));
			in.read(input, 0, inputFileLength);
		}finally{
			if(in != null){
				in.close();
			}
		}
		return input;
	}
}
