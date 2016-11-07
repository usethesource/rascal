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

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipParameters;
import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;
import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.interpreter.types.OverloadedFunctionType;
import org.rascalmpl.interpreter.types.ReifiedType;
import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IDateTime;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.IRational;
import org.rascalmpl.value.IReal;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.io.binary.message.IValueWriter;
import org.rascalmpl.value.io.binary.util.OpenAddressingLastWritten;
import org.rascalmpl.value.io.binary.util.PrePostIValueIterator;
import org.rascalmpl.value.io.binary.util.TrackLastWritten;
import org.rascalmpl.value.io.binary.wire.ValueWireOutputStream;
import org.rascalmpl.value.type.ITypeVisitor;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.visitors.IValueVisitor;
import org.rascalmpl.values.ValueFactoryFactory;

import com.github.luben.zstd.ZstdOutputStream;
import com.github.luben.zstd.util.Native;
            
/**
 * A binary serializer for IValues and Types.
 * 
 * For most use cases, construct an instance of IValueWriter and write one or more IValues to it.
 * If you are also using the {@link ValueWriteOutputStream}, there is a static method write method inteded for this use case.
 * 
 * Note that when writing multiple IValues, you have to store this arity yourself.
 *
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
    
    private static class WindowSizes {
        private final int uriWindow;
        private final int typeWindow;
        private final int valueWindow;
        private final int stringsWindow;
        public WindowSizes(int valueWindow, int uriWindow, int typeWindow, int stringsWindow) {
            this.stringsWindow = stringsWindow;
            this.typeWindow = typeWindow;
            this.uriWindow = uriWindow;
            this.valueWindow = valueWindow;
        }
    }
    
    private static final WindowSizes NO_WINDOW = new WindowSizes(0, 0, 0, 0);
    private static final WindowSizes TINY_WINDOW = new WindowSizes(500, 200, 100, 500);
    private static final WindowSizes SMALL_WINDOW = new WindowSizes(5_000, 1_000, 800, 1_000);
    private static final WindowSizes NORMAL_WINDOW = new WindowSizes(100_000, 40_000, 5_000, 10_000);
    
    
    
    public IValueOutputStream(OutputStream out) throws IOException {
        this(out, CompressionRate.Normal);
    }
    private CompressionRate compression;
    private OutputStream rawStream;
    private ValueWireOutputStream writer;
    public IValueOutputStream(OutputStream out, CompressionRate compression) throws IOException {
        out.write(Header.MAIN);
        rawStream = out;
        this.compression = compression;
        writer = null;
    }
    
    private static final int SMALL_SIZE = 512;
    private static final int NORMAL_SIZE = 8*1024;

    public void write(IValue value) throws IOException {
        int estimatedSize = IValueSizeEstimator.estimateIValueSize(value, NORMAL_SIZE);
        WindowSizes sizes = calculateWindowSize(estimatedSize);
        if (writer == null) {
            initializeWriter(estimatedSize, sizes);
        }
        IValueWriter.write(writer, sizes.typeWindow, sizes.valueWindow, sizes.uriWindow, value);
    }

    private WindowSizes calculateWindowSize(int estimatedSize) {
        WindowSizes sizes = NO_WINDOW;
        if (compression != CompressionRate.NoSharing) {
            if (estimatedSize < SMALL_SIZE) {
                sizes = TINY_WINDOW;
            }
            else if (estimatedSize < NORMAL_SIZE) {
                sizes = SMALL_WINDOW;
            }
            else {
                sizes = NORMAL_WINDOW;
            }
        }
        return sizes;
    }

    private static int fallbackIfNeeded(int compressionAlgorithm) {
        if (compressionAlgorithm == Header.Compression.ZSTD && ! Compressor.zstdAvailable()) {
            return Header.Compression.GZIP;
        }
        return compressionAlgorithm;
    }

    private void initializeWriter(int estimatedSize, WindowSizes sizes) throws IOException {
        if (estimatedSize < SMALL_SIZE) {
            compression = CompressionRate.None;
        }
        int algorithm = fallbackIfNeeded(compression.compressionAlgorithm);
        rawStream.write(algorithm);
        rawStream = Compressor.wrapStream(rawStream, algorithm, compression.compressionLevel);
        // writer is only initilized for first value
        writer = new ValueWireOutputStream(rawStream, sizes.stringsWindow);
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
