/** 
 * Copyright (c) 2016, Davy Landman, Centrum Wiskunde & Informatica (CWI) 
 * All rights reserved. 
 *  
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: 
 *  
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. 
 *  
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. 
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */ 
package org.rascalmpl.value.io.binary.util;

import java.io.IOException;
import java.io.OutputStream;

public class DelayedCompressionOutputStream extends OutputStream {

    private static final int COMPRESS_AFTER = 4*1024;
    private byte[] buffer;
    private int written;
    private OutputStream out;
    private WrappingCompressorFunction compress;
    private int compressHeader;
    
    @FunctionalInterface
    public interface WrappingCompressorFunction {
        OutputStream wrap(OutputStream toWrap) throws IOException;
    }

    public DelayedCompressionOutputStream(OutputStream out, int compressHeader, WrappingCompressorFunction compress) throws IOException {
        this.out = out;
        this.compressHeader = compressHeader;
        this.compress = compress;
        buffer = new byte[COMPRESS_AFTER];
        written = 0;
    }
    
    @Override
    public void flush() throws IOException {
        if (written != -1) {
            out.write(0); // no compression
            out.write(buffer, 0, written);
            written = -1;
        }
        out.flush();
    }
    
    @Override
    public void close() throws IOException {
        try (OutputStream out2 = out) {
            flush();
        }
    }
    
    @Override
    public void write(int b) throws IOException {
        write(new byte[] { (byte)b }, 0, 1); // normally shouldn't occur, so take slow path
    }
    @Override
    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }
    
    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        if (written != -1) {
            if (written + len < COMPRESS_AFTER) {
                // append buffer
                System.arraycopy(b, off, buffer, written, len);
                written += len;
                return;
            }
            else {
                // buffer overflowed, enough data for compression overhead
                out.write(compressHeader);
                out = compress.wrap(out);
                out.write(buffer, 0, written);
                written = -1;
            }
        }
        out.write(b, off, len);
    }
}
