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

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IListWriter;
import org.rascalmpl.value.IMapWriter;
import org.rascalmpl.value.INode;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ISetWriter;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.io.binary.message.IValueReader;
import org.rascalmpl.value.io.binary.message.IValueWriter;
import org.rascalmpl.value.io.binary.util.LinearCircularLookupWindow;
import org.rascalmpl.value.io.binary.util.TrackLastRead;
import org.rascalmpl.value.io.binary.wire.binary.BinaryWireInputStream;
import org.rascalmpl.value.io.old.BinaryReader;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.values.uptr.RascalValueFactory;
import org.tukaani.xz.XZInputStream;

import com.github.luben.zstd.ZstdInputStream;

import io.usethesource.capsule.TransientMap;
import io.usethesource.capsule.TrieMap_5Bits;

/**
 * Reader for binary serialized IValues and Types.
 *
 */
public class IValueInputStream implements Closeable {
    private final BinaryWireInputStream reader;
    private final TypeStore ts;
    private final IValueFactory vf;
    private final boolean legacy;
    @SuppressWarnings("deprecation")
    private final BinaryReader legacyReader;

    /**
     * this will consume the whole stream, or at least more than needed due to buffering, so don't use the InputStream afterwards
     */
    @SuppressWarnings("deprecation")
    public IValueInputStream(InputStream in, IValueFactory vf, TypeStore ts) throws IOException {
        ts.extendStore(RascalValueFactory.getStore());
        this.ts = ts;
        this.vf = vf;
        byte[] currentHeader = new byte[Header.MAIN.length];
        in.read(currentHeader);
        if (!Arrays.equals(Header.MAIN, currentHeader)) {
            byte firstByte = currentHeader[0];
            // possible an old binary stream
            firstByte &= (BinaryReader.SHARED_FLAG ^ 0xFF); // remove the possible set shared bit
            if (BinaryReader.BOOL_HEADER <= firstByte && firstByte <= BinaryReader.IEEE754_ENCODED_DOUBLE_HEADER) {
                System.err.println("Old value format used, switching to legacy mode!");
                legacy = true;
                legacyReader = new BinaryReader(vf, ts, new SequenceInputStream(new ByteArrayInputStream(currentHeader), in));
                reader = null;
                return;
            }
            throw new IOException("Unsupported file");
        }
        legacy = false;
        legacyReader = null;

        int compression = in.read();
        switch (compression) {
            case Header.Compression.NONE:
                break;
            case Header.Compression.GZIP:
                in = new GZIPInputStream(in);
                break;
            case Header.Compression.XZ:
                in = new XZInputStream(in);
                break;
            case Header.Compression.ZSTD: {
                if (Compressor.zstdAvailable()) {
                    in = new ZstdInputStream(in);
                }
                else {
                    throw new IOException("There is not native zstd library available for the current architecture.");
                }
                break;
            }
            default:
                throw new IOException("Unsupported compression in file");
        }

        reader = new BinaryWireInputStream(in);
    }
    
    @SuppressWarnings("deprecation")
    public IValue read() throws IOException {
        if (legacy) {
            return legacyReader.deserialize();
        }
        return IValueReader.read(reader, vf, ts);
    }
    
    @Override
    @SuppressWarnings("deprecation")
    public void close() throws IOException {
        if (legacy) {
            legacyReader.close();
        }
        else {
            reader.close();
        }
    }
}

