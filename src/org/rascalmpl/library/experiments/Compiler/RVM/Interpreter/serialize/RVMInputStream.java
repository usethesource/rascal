package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize;

import java.io.IOException;
import java.io.InputStream;

/**
 * RVMInputStream provides input operations for basic Java values
 * such als byte, short int, int long, String, etc.
 * 
 * Note: some logic here copied from the FST package!
 *
 */
public final class RVMInputStream extends InputStream {
	
    public int chunk_size = 8000;
    public static ThreadLocal<byte[]> cachedBuffer = new ThreadLocal<byte[]>();
    public byte buf[];
    public int pos;
    public int count; // available valid read bytes
    InputStream in;
    boolean fullyRead = false; // true if input source has been read til end
    public boolean byteBacked = false;

    public RVMInputStream(InputStream in) {
        initFromStream(in);
    }

    public void resetForReuse( byte b[], int length ) {
        reset();
        count = length;
        buf = b;
        pos = 0;
        byteBacked = true;
        fullyRead = true;
    }

    public void initFromStream(InputStream in) {
        fullyRead = false;
        byteBacked = false;
        pos = 0;
        this.in = in;
        if (buf == null) {
            buf = cachedBuffer.get();
            if (buf == null) {
                buf = new byte[chunk_size];
                cachedBuffer.set(buf);
            }
        }
        readNextChunk(in);
    }

    public boolean isFullyRead() {
        return fullyRead && pos >= count;
    }

    public void readNextChunk(InputStream in) {
        int read;
        try {
            if (buf.length < count + chunk_size) {
                ensureCapacity(Math.max( Math.min(Integer.MAX_VALUE-1,buf.length * 2), count + chunk_size)); // at least grab 5kb
            }
            read = in.read(buf, count, chunk_size);
            if (read > 0) {
                count += read;
            } else {
                fullyRead = true;
            }
        } catch (Exception iex) {
            fullyRead = true;
        }
    }

    public void ensureCapacity(int siz) {
        if (buf.length < siz && ! fullyRead) {
            byte newBuf[] = new byte[siz];
            System.arraycopy(buf, 0, newBuf, 0, buf.length);
            buf = newBuf;
            if (siz < 10 * 1024 * 1024) { // issue 19, don't go overboard with buffer caching
                cachedBuffer.set(buf);
            }
        }
    }

    public RVMInputStream(byte buf[]) {
        this.buf = buf;
        this.pos = 0;
        this.count = buf.length;
    }

    public RVMInputStream(byte buf[], int offset, int length) {
        this.buf = buf;
        this.pos = offset;
        this.count = Math.min(offset + length, buf.length);
    }

    public int read() {
        if (pos < count) {
            return (buf[pos++] & 0xff);
        }
        readNextChunk(in);
        if (fullyRead)
            return -1;
        return -1;
    }

    public int read(byte b[], int off, int len) {
        if (isFullyRead())
            return -1;
        while (! fullyRead && pos + len >= count) {
            readNextChunk(in);
        }
        int avail = count - pos;
        if (len > avail) {
            len = avail;
        }
        if (len <= 0) {
            return 0;
        }
        System.arraycopy(buf, pos, b, off, len);
        pos += len;
        return len;
    }

    public long skip(long n) {
        long k = count - pos;
        if (n < k) {
            k = n < 0 ? 0 : n;
        }
        pos += k;
        return k;
    }

    public int available() {
        return count - pos;
    }

    public boolean markSupported() {
        return false;
    }

    public void mark(int readAheadLimit) {
    }

    public void reset() {
        count = 0;
        pos = 0;
        fullyRead = false;
        byteBacked = false;
    }

    public void close() throws IOException {
        if ( in != null )
            in.close();
    }

    public void ensureReadAhead(int bytes) {
        if ( byteBacked )
            return;
        int targetCount = pos + bytes;
        while (!fullyRead && count < targetCount) {
            readNextChunk(in);
        }
    }
    
    public final byte readByte() throws IOException {
        ensureReadAhead(1);
        return buf[pos++];
    }
    
	public boolean readBool() throws IOException {
		final byte head = readByte();
		return head == 0 ? false : true;
	}
	
	public short readShort() throws IOException {
		ensureReadAhead(3);
		int head = readByte() & 0xff;
		if (head >= 0 && head < 255) {
			return (short) head;
		}
		int ch1 = readByte() & 0xff;
		int ch2 = readByte() & 0xff;
		return (short)((ch1 << 0) + (ch2 << 8));
	}
	
	public int readInt() throws IOException {
		final byte head = readByte();
		// -128 = short byte, -127 == 4 byte
		if (head > -127 && head <= 127) {
			//System.out.println("readInt: " + head);;
			return head;
		}
		if (head == -128) {
			int ch1 = (readByte() + 256) & 0xff;
			int ch2 = (readByte() + 256) & 0xff;

			//System.out.println("readInt: " + (short) ((ch2 << 8) + (ch1 << 0)));
			return (short) ((ch2 << 8) + (ch1 << 0));
		} else {
			int ch1 = (readByte() + 256) & 0xff;
			int ch2 = (readByte() + 256) & 0xff;
			int ch3 = (readByte() + 256) & 0xff;
			int ch4 = (readByte() + 256) & 0xff;
			int res = (ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1 << 0);
			//System.out.println("readInt: " + res);
			return res;
		}
	}
	
    public long readLong() throws IOException {
        ensureReadAhead(9);
        byte head = readByte();
        // -128 = short byte, -127 == 4 byte
        if (head > -126 && head <= 127) {
            return head;
        }
        if (head == -128) {
            int ch1 = (buf[count++] + 256) & 0xff;
            int ch2 = (buf[count++] + 256) & 0xff;
            return (short) ((ch2 << 8) + (ch1 << 0));
        } else if (head == -127) {
            int ch1 = (buf[count++] + 256) & 0xff;
            int ch2 = (buf[count++] + 256) & 0xff;
            int ch3 = (buf[count++] + 256) & 0xff;
            int ch4 = (buf[count++] + 256) & 0xff;
            int res = (ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1 << 0);
            return res;
        } else {
            ensureReadAhead(8);
            long ch8 = (buf[count++] + 256) & 0xff;
            long ch7 = (buf[count++] + 256) & 0xff;
            long ch6 = (buf[count++] + 256) & 0xff;
            long ch5 = (buf[count++] + 256) & 0xff;
            long ch4 = (buf[count++] + 256) & 0xff;
            long ch3 = (buf[count++] + 256) & 0xff;
            long ch2 = (buf[count++] + 256) & 0xff;
            long ch1 = (buf[count++] + 256) & 0xff;
            return ((ch1 << 56) + (ch2 << 48) + (ch3 << 40) + (ch4 << 32) + (ch5 << 24) + (ch6 << 16) + (ch7 << 8) + (ch8 << 0));
        }
    }

	public String readString() throws IOException {
		int size = readInt();
		byte[] data = new byte[size];
		for(int i = 0; i< size; i++){
			data[i] = readByte();
		}
		return new String(data, RVMOutputStream.CharEncoding);
	}

	public byte[] readByteArray() throws IOException {
		int n = readInt();
		byte[] bytes = new byte[n];
		read(bytes, 0, n);
		return bytes;
	}

}
