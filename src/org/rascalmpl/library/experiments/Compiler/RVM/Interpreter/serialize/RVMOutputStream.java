package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize;


import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * RVMOutputStream provides output operations for basic Java values
 * such als byte, short int, int long, String, etc.
 * 
 * Note: some logic here copied from the FST package!
 *
 */
public final class RVMOutputStream extends OutputStream {

    /**
     * The buffer where data is stored.
     */
    public byte buf[];
    /**
     * The number of valid bytes in the buffer.
     */
    public int pos;
    OutputStream outstream;
    private int off;
    static final String CharEncoding = "UTF8";

    public RVMOutputStream(OutputStream out) {
        this(4000, out);
    }

    public RVMOutputStream(int size, OutputStream out) {
        buf = new byte[size];
        outstream = out;
    }

    public OutputStream getOutstream() {
        return outstream;
    }

    public void setOutstream(OutputStream outstream) {
        this.outstream = outstream;
    }

    public byte[] getBuf() {
        return buf;
    }

    public void setBuf(byte[] buf) {
        this.buf = buf;
    }

    public final void ensureFree(int free) throws IOException {
        // inline ..
        if (pos + free - buf.length > 0)
            grow(pos + free);
    }

    public final void ensureCapacity(int minCapacity) throws IOException {
        if (minCapacity - buf.length > 0)
            grow(minCapacity);
    }

    private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = buf.length;
        int newCapacity = oldCapacity * 2;
        if (oldCapacity > 50 * 1024 * 1024) // for large object graphs, grow more carefully
            newCapacity = minCapacity + 1024 * 1024 * 20;
        else if (oldCapacity < 1001) {
            newCapacity = 4000; // large step initially
        }
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;

        try {
            buf = Arrays.copyOf(buf, newCapacity);
        } catch (OutOfMemoryError ome) {
            System.out.println("OME resize from " + buf.length + " to " + newCapacity + " clearing caches ..");
            throw new RuntimeException(ome);
        }
    }

    public void write(int b) throws IOException {
        ensureCapacity(pos + 1);
        buf[pos] = (byte) b;
        pos += 1;
    }

    public void write(byte b[], int off, int len) throws IOException {
        ensureCapacity(pos + len);
        System.arraycopy(b, off, buf, pos, len);
        pos += len;
    }

    /**
     * only works if no flush has been triggered (aka only write one object per stream instance)
     *
     * @param out
     * @throws IOException
     */
    public void copyTo(OutputStream out) throws IOException {
        out.write(buf, 0, pos);
    }

    public void reset() {
        pos = 0;
        off = 0;
    }

    public byte toByteArray()[] {
        return Arrays.copyOf(buf, pos);
    }

    public int size() {
        return pos;
    }

    public void close() throws IOException {
        flush();
        if (outstream != this)
            outstream.close();
    }

    public void flush() throws IOException {
        if (pos > 0 && outstream != null && outstream != this) {
            copyTo(outstream);
            off = pos;
            reset();
        }
        if (outstream != this && outstream != null)
            outstream.flush();
    }

    public void reset(byte[] out) {
        reset();
        buf = out;
    }

    // return offset of pos to stream position
    public int getOff() {
        return off;
    }
    
    public void writeByte(int b)  throws IOException{
    	write(b);
    }
    
    public void writeBool(boolean b)  throws IOException{
    	write(b ? 1 : 0);
    }
    
    public void writeShort(short s)  throws IOException{
		if (s < 255 && s >= 0) {
            write(s);
        } else {
        	ensureFree(3);
            buf[pos++] = (byte) 255;
            buf[pos++] = (byte) (s >>> 0);
            buf[pos++] = (byte) (s >>> 8);
        }
	}
	
	public void writeInt(int anInt) throws IOException {
		// -128 = short byte, -127 == 4 byte
		if (anInt > -127 && anInt <= 127) {
			if (buf.length <= pos + 1) {
				ensureFree(1);
			}
			buf[pos++] = (byte) anInt;
		} else if (anInt >= Short.MIN_VALUE && anInt <= Short.MAX_VALUE) {
			ensureFree(3);
			buf[pos++] = (byte) -128;
			buf[pos++] = (byte) (anInt >>> 0);
			buf[pos++] = (byte) (anInt >>> 8);
		} else {
			ensureFree(5);
			buf[pos++] = (byte) -127;
			buf[pos++] = (byte) ((anInt >>> 0) & 0xFF);
			buf[pos++] = (byte) ((anInt >>>  8) & 0xFF);
			buf[pos++] = (byte) ((anInt >>> 16) & 0xFF);
			buf[pos++] = (byte) ((anInt >>> 24) & 0xFF);
		}
	}
	
    public void writeLong(long anInt) throws IOException {
    	// -128 = short byte, -127 == 4 byte
        if (anInt > -126 && anInt <= 127) {
            writeByte((int) anInt);
        } else if (anInt >= Short.MIN_VALUE && anInt <= Short.MAX_VALUE) {
            ensureFree(3);
            buf[pos++] = (byte) -128;
            buf[pos++] = (byte) (anInt >>> 0);
            buf[pos++] = (byte) (anInt >>> 8);
        } else if (anInt >= Integer.MIN_VALUE && anInt <= Integer.MAX_VALUE) {
            ensureFree(5);
            buf[pos++] = (byte) -127;
            buf[pos++] = (byte) ((anInt >>> 0) & 0xFF);
            buf[pos++] = (byte) ((anInt >>>  8) & 0xFF);
            buf[pos++] = (byte) ((anInt >>> 16) & 0xFF);
            buf[pos++] = (byte) ((anInt >>> 24) & 0xFF);
        } else {
            ensureFree(9);
            buf[pos++] = (byte) -126;
            buf[pos++] = (byte) (anInt >>> 0);
            buf[pos++] = (byte) (anInt >>> 8);
            buf[pos++] = (byte) (anInt >>> 16);
            buf[pos++] = (byte) (anInt >>> 24);
            buf[pos++] = (byte) (anInt >>> 32);
            buf[pos++] = (byte) (anInt >>> 40);
            buf[pos++] = (byte) (anInt >>> 48);
            buf[pos++] = (byte) (anInt >>> 56);
        }
    }

    public void writeString(String s) throws IOException {
    	byte[] stringData = s.getBytes(CharEncoding);
    	writeInt(stringData.length);
    	write(stringData);
    }
}
