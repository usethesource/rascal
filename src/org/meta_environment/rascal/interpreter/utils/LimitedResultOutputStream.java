package org.meta_environment.rascal.interpreter.utils;

import java.io.IOException;
import java.io.OutputStream;

public class LimitedResultOutputStream extends OutputStream{
	private final int limit;
	private final byte[] data;
	private int position;
	private boolean limitReached;
	
	public LimitedResultOutputStream(int limit){
		super();
		
		this.limit = limit;
		data = new byte[limit];
		position = 0;
		limitReached = false;
	}
	
	public void write(int b) throws IOException{
		if(limit == position){
			limitReached = true;
			throw new IOLimitReachedException();
		}
		
		data[position++] = (byte) b;
	}
	
	public String toString(){
		if(limitReached){
			int length = data.length;
			data[length - 3] = '.';
			data[length - 2] = '.';
			data[length - 1] = '.';
		}
		
		return new String(data, 0, position);
	}
	
	public static class IOLimitReachedException extends IOException{
		private static final long serialVersionUID = -1396788285349799099L;

		public IOLimitReachedException(){
			super("Limit reached");
		}
	}
}
