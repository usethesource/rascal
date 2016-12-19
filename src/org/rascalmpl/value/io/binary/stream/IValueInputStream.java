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
import java.util.Arrays;

import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.io.binary.message.IValueReader;
import org.rascalmpl.value.io.binary.wire.binary.BinaryWireInputStream;
import org.rascalmpl.value.io.old.BinaryReader;
import org.rascalmpl.value.type.TypeStore;

/**
 * Reader for binary serialized IValues written using the {@linkplain IValueOutputStream}. <br />
 * <br />
 * At the moment, it automatically detects the old serializer format, and try to read using the old {@linkplain  org.rascalmpl.value.io.old.BinaryReader}.
 */
@SuppressWarnings("deprecation")
public class IValueInputStream implements Closeable {
    private final BinaryWireInputStream reader;
    private final TypeStore ts;
    private final IValueFactory vf;
    private final boolean legacy;
    private final BinaryReader legacyReader;

    /**
     * This will <strong>consume</strong> the whole stream (or at least more than needed due to buffering), don't use the InputStream afterwards!
     */
    public IValueInputStream(InputStream in, IValueFactory vf, TypeStore ts) throws IOException {
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
        in = Compressor.wrapStream(in, compression);
        reader = new BinaryWireInputStream(in);
    }
    
    public IValue read() throws IOException {
        if (legacy) {
            return legacyReader.deserialize();
        }
        return IValueReader.read(reader, vf, ts);
    }
    
    @Override
    public void close() throws IOException {
        if (legacy) {
            legacyReader.close();
        }
        else {
            reader.close();
        }
    }
}

