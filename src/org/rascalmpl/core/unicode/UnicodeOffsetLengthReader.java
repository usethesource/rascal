package org.rascalmpl.core.unicode;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Wraps a reader counting first a number (offset) of unicode characters before it starts reading
 * and then only reads a number of unicode characters (length) before it returns end-of-stream.
 * When length is -1, the reader reads until the end of the stream. 
 * 
 * Counting unicode characters is
 * interesting because of the utf16 encoding Java has in memory for unicode.
 */
public class UnicodeOffsetLengthReader extends FilterReader {
	private int charsRead;
	private int offset;
	private int length;
	
	public UnicodeOffsetLengthReader(Reader in, int offset, int len) {
		super(in);
		this.offset = offset;
		this.length = len;
	}
	
	private void offset() throws IOException {
		if (offset > 0) {
			char[] buf = new char[8096];

			while (offset > 0) {
				int res = in.read(buf, 0, Math.min(offset, buf.length));

				if (res == -1) {
					offset = 0;
					return;
				}

				offset -= res; // may be not enough due to surrogate pairs

				for (int i = 0; i < res; i++) {
					if (Character.isHighSurrogate(buf[i])) {
						offset++; // correct for earlier subtraction
					}
				}
			}
		}
	}
	
	@Override
	public int read() throws IOException {
		offset();
		if (length != -1 && charsRead >= length) {
			return -1;
		}
		
		int res = super.read();
		
		if (res != -1 && !Character.isHighSurrogate((char) res)) {
			charsRead++;
		}
		
		return res;
	}
	
	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		offset();
		
		if (length != -1 && charsRead >= length) {
			// we are at the end already
			return -1;
		}
		
		
		// just get what we can, we will cut the result below
		int res = super.read(cbuf, off, len);
		
		if (res == 0) {
			return res; // unlikely corner case
		}
		
		if (res != -1) {
			// now cut off the result
			int count = 0;
			for (int i = 0; i < res; i++) {
				count++;
				if (!Character.isHighSurrogate(cbuf[i])) {
					charsRead++;
					
					if (length != -1   && charsRead >= length) {
						// done
						return count;
					}
				}
			}
			return count;
		}
		
		return res;
	}
}