/** 
 * Copyright (c) 2016, Davy Landman, Paul Klint, Centrum Wiskunde & Informatica (CWI) 
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
package org.rascalmpl.value.io.binary.stream;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

import org.rascalmpl.value.IValue;
import org.rascalmpl.value.io.binary.message.IValueWriter;
import org.rascalmpl.value.io.binary.util.DelayedCompressionOutputStream;
import org.rascalmpl.value.io.binary.util.WindowSizes;
import org.rascalmpl.value.io.binary.wire.IWireOutputStream;
import org.rascalmpl.value.io.binary.wire.binary.BinaryWireOutputStream;
            
/**
 * A binary serializer for IValues. <br/>
 * <br />
 * Note that when writing multiple IValues, you have to take care of storing this arity yourself.  <br/>
 * <br />
 * When you want to nest the IValue's in another stream, you will have to use the {@link org.rascalmpl.value.io.binary.message.IValueWriter IValueWriter} static methods. 
 * This does enforce you to adopt the same {@link org.rascalmpl.value.io.binary.wire.IWireOutputStream wire format} format.
 */
public class IValueOutputStream implements Closeable {
    

    /**
     * Compression of the serializer, balances lookback windows and compression algorithm
     */
    public enum CompressionRate {
        /**
         * Use only for debugging!
         */
        NoSharing(Header.Compression.NONE, 0),
        None(Header.Compression.NONE, 0),
        Light(Header.Compression.ZSTD, 1),
        Normal(Header.Compression.ZSTD, 5),
        Strong(Header.Compression.ZSTD, 13),
        Extreme(Header.Compression.XZ, 6), 
        ;

        private final int compressionAlgorithm;
        private final int compressionLevel;

        CompressionRate(int compressionAlgorithm, int compressionLevel) {
            this.compressionLevel = compressionLevel;
            this.compressionAlgorithm = compressionAlgorithm;
        } 
    }
    
    
    
    
    private CompressionRate compression;
    private OutputStream rawStream;
    private IWireOutputStream writer;

    public IValueOutputStream(OutputStream out) throws IOException {
        this(out, CompressionRate.Normal);
    }
    public IValueOutputStream(OutputStream out, CompressionRate compression) throws IOException {
        out.write(Header.MAIN);
        rawStream = out;
        this.compression = compression;
        writer = null;
    }
    

    public void write(IValue value) throws IOException {
        WindowSizes sizes = compression.compressionLevel == 0 ? WindowSizes.NO_WINDOW : WindowSizes.NORMAL_WINDOW;
        if (writer == null) {
            writer = initializeWriter(sizes);
        }
        IValueWriter.write(writer, sizes, value);
    }



    private static int fallbackIfNeeded(int compressionAlgorithm) {
        if (compressionAlgorithm == Header.Compression.ZSTD && ! Compressor.zstdAvailable()) {
            return Header.Compression.GZIP;
        }
        return compressionAlgorithm;
    }

    private IWireOutputStream initializeWriter(WindowSizes sizes) throws IOException {
        if (sizes == WindowSizes.NO_WINDOW || sizes == WindowSizes.TINY_WINDOW) {
            compression = CompressionRate.None;
        }
        int algorithm = fallbackIfNeeded(compression.compressionAlgorithm);
        rawStream = new DelayedCompressionOutputStream(rawStream, algorithm, o ->
            Compressor.wrapStream(o, algorithm, compression.compressionLevel)
        );
        return new BinaryWireOutputStream(rawStream, sizes.stringsWindow);
    }


    @Override
    public void close() throws IOException {
        if (writer != null) {
            writer.close();
        }
        else {
            rawStream.close();
        }
    }
}
