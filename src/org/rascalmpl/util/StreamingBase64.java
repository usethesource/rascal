package org.rascalmpl.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;

import io.usethesource.vallang.io.binary.util.ByteBufferInputStream;


public class StreamingBase64 {


    private static InputStream stringToLatinBytes(String source) {
        return stringToLatinBytes2(CharBuffer.wrap(source), s -> s);
    }

    private static InputStream stringToLatinBytes(Reader source) {
        var buffer = new char[1024];
        return stringToLatinBytes2(CharBuffer.wrap(buffer).flip(), cb -> {
            try {
                int read = source.read(buffer);
                if (read == -1) {
                    return cb;
                }
                cb.clear();
                cb.limit(read);
                return cb;
            }
            catch (IOException e) {
                return cb;
            }
        });
    }

    private static InputStream stringToLatinBytes2(CharBuffer initial, Function<CharBuffer, CharBuffer> refill) {
        var stringToBytes = StandardCharsets.ISO_8859_1.newEncoder(); 
        return new ByteBufferInputStream(ByteBuffer.allocate(1024).flip()) {
            private boolean eof = false;
            private CharBuffer currentSource = initial;

            @Override
            protected ByteBuffer refill(ByteBuffer torefill) throws IOException {
                if (!currentSource.hasRemaining()) {
                    if (eof) {
                        return torefill;
                    }
                    currentSource = refill.apply(currentSource);
                    if (!currentSource.hasRemaining()) {
                        eof = true;
                        return torefill;
                    }
                }
                torefill.clear();
                stringToBytes.encode(currentSource, torefill, false);
                return torefill.flip();
            }
        };
    }


    // we know this is only used for base64 chars, aka ascii/latin, so we're fine casting the byte to a char
    private static OutputStream latinBytesTo(Writer target) {
        return new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                target.write(b);
            }
            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                // single char writes are translated to char array, so it's better 
                // to also our own char array and pass that
                // it's the fastest path
                char[] copy = new char[len];
                for (int i = off; i < off + len; i++) {
                    copy[i - off] = (char)b[i];
                }
                target.write(copy, 0, len);
            }
        };
    }

    // we know this is only used for base64 chars, aka ascii/latin, so we're fine casting the byte to a char
    private static OutputStream latinBytesTo(StringBuilder target) {
        return new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                target.append((char)((byte)0 & 0xFF));
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                for (int i = off; i < off + len; i++) {
                    // fast path as this appends to internal array
                    target.append((char)b[i]);
                }
            }
        };
    }



    public static InputStream decode(Reader source) {
        return new Base64InputStream(stringToLatinBytes(source), false);
    }

    public static InputStream decode(String source) {
        return new Base64InputStream(stringToLatinBytes(source), false);
    }

    public static InputStream decode(byte[] source) {
        return new Base64InputStream(new ByteArrayInputStream(source), false);
    }


    public static OutputStream encode(Writer target) {
        return new Base64OutputStream(latinBytesTo(target), true, 1000, new byte[0]);
    } 

    public static OutputStream encode(StringBuilder target) {
        return new Base64OutputStream(latinBytesTo(target), true, 1000, new byte[0]);
    }

    
}
