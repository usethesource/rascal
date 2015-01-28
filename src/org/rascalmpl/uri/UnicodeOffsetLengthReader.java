package org.rascalmpl.uri;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;

public class UnicodeOffsetLengthReader extends FilterReader {
	private int charsRead;
	private int offset;
	private int length;
	private char previous;
	
	protected UnicodeOffsetLengthReader(Reader in, int offset, int len) {
		super(in);
		this.offset = offset;
		this.length = len;
	}
	
	private void offset() throws IOException {
		while (offset > 0) {
			this.previous = (char) super.read();
			
			if (!Character.isHighSurrogate(this.previous)) {
				offset--;
			}
		}
	}
	
	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		offset();
		
		if (this.charsRead >= this.length) {
			// we are at the end already
			return -1;
		}
		
		if (Character.isHighSurrogate(previous)) {
			// push the cached high surrogate on the stream
			cbuf[off] = previous;
			off += 1;
			len -= 1;
		}
		
		// just get what we can, we will cut the result below
		int res = super.read(cbuf, off, len);
		
		if (res == 0) {
			return res; // unlikely corner case
		}
		
		if (res != -1) {
			// now cut off the result
			int count = 0;
			for (int i = off; i < len + off; i++) {
				this.previous = cbuf[i];

				if (!Character.isHighSurrogate(cbuf[i])) {
					charsRead++;
					count++;
					
					if (charsRead >= length) {
						// done
						return count;
					}
				}
			}
			
			// if the last char was a high surrogate, then count was not incremented
			// and we will not report it readed.
			// it will be pushed onto the stream in subsequent calls
			return count;
		}
		
		return res;
	}
}