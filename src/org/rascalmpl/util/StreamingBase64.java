package org.rascalmpl.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.CharBuffer;
import java.util.function.Function;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;


/**
 * Streaming translations of base64 contents
 */
public class StreamingBase64 {

    /*
     * Note, in this class we use the fact that base64 chars are always ascii
     * So a char can be cast to a byte. No need for charset encoders.
     */

    private static InputStream stringToLatinBytes(String source) {
        return streamCharsAsBytes(CharBuffer.wrap(source), s -> s);
    }

    private static InputStream stringToLatinBytes(Reader source) {
        var buffer = new char[1024];
        return streamCharsAsBytes(CharBuffer.wrap(buffer).flip() /*empty*/, cb -> {
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

    private static InputStream streamCharsAsBytes(CharBuffer initial, Function<CharBuffer, CharBuffer> refill) {
        return new InputStream() {
            private boolean eof = false;
            private CharBuffer currentSource = initial;

            @Override
            public int available() throws IOException {
                if (!currentSource.hasRemaining()) {
                    if (eof) {
                        return 0;
                    }
                    currentSource = refill.apply(currentSource);
                    if (!currentSource.hasRemaining()) {
                        eof = true;
                        return 0;
                    }
                }
                return currentSource.remaining();
            }

            @Override
            public int read() throws IOException {
                if (available() == 0) {
                    return -1;
                }
                return currentSource.get() & 0xFF;
            }

            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                int remaining = available();
                if (remaining == 0) {
                    return -1;
                }
                int toRead = Math.min(remaining, len);
                for (int i = 0; i < toRead; i++) {
                    b[off + i] = (byte)(currentSource.get() & 0xFF);
                }
                return toRead;
            }
        };
    }

    // the Base64 class of apache commons does not support disabling the padding
    // so instead we have to filter it out for now
    private static OutputStream latinBytesTo(Writer target, boolean padding) {
        return new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                if (!padding && b == '=') {
                    return;
                }
                target.write(b);
            }
            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                // single char writes are translated to char array, so it's better 
                // to also our own char array and pass that
                // it's the fastest path that has the last amount of copies.
                char[] copy = new char[len];
                int p = 0;
                for (int i = off; i < off + len; i++) {
                    char c = (char)b[i];
                    if (!padding && c == '=') {
                        continue;
                    }
                    copy[p++] = c;
                }
                target.write(copy, 0, p);
            }
        };
    }

    // the Base64 class of apache commons does not support disabling the padding
    // so instead we have to filter it out for now
    private static OutputStream latinBytesTo(StringBuilder target, boolean padding) {
        return new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                if (!padding && b == '=') {
                    return;
                }
                target.append((char)((byte)0 & 0xFF));
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                for (int i = off; i < off + len; i++) {
                    char c = (char)b[i];
                    if (!padding && c == '=') {
                        continue;
                    }
                    // fast path as this appends to internal array
                    target.append(c);
                }
            }
        };
    }



    /**
     * Do a streaming base64 decode, reading base64 string while the inputstream is consumed
     */
    public static InputStream decode(Reader source) {
        return new Base64InputStream(stringToLatinBytes(source), false);
    }

    /**
     * Do a streaming base64 decode, reading base64 string while the inputstream is consumed
     */
    public static InputStream decode(String source) {
        return new Base64InputStream(stringToLatinBytes(source), false);
    }

    /**
     * Do a streaming base64 decode, reading base64 string (encoded as bytes) while the inputstream is consumed
     */
    public static InputStream decode(byte[] source) {
        return new Base64InputStream(new ByteArrayInputStream(source), false);
    }

    /** utility function to write the decoded bytes to a outputstream */
    public static void decode(Reader source, OutputStream target) throws IOException {
        copy(decode(source), target);
    }

    /** utility function to write the decoded bytes to a outputstream */
    public static void decode(String source, OutputStream target) throws IOException {
        copy(decode(source), target);
    }

    /** utility function to write the decoded bytes to a outputstream */
    public static void decode(byte[] source, OutputStream target) throws IOException {
        copy(decode(source), target);
    }

    private static void copy(InputStream decoder, OutputStream target) throws IOException {
        try (var source = decoder) {
            var buffer = new byte[3 * 1024]; // 4:3 ratio between base64 string and bytes produced.
            int read = 0;
            while ((read = source.read(buffer)) != -1) {
                target.write(buffer, 0, read);
            }
        }
    }



    /**
     * Create an OutputStream that on writing bytes, encodes them to the target writer
     */
    public static OutputStream encode(Writer target) {
        return encode(target, true);
    }

    /**
     * Create an OutputStream that on writing bytes, encodes them to the target writer, and you can disable the optional `=` padding characters
     */
    public static OutputStream encode(Writer target, boolean padding) {
        return new Base64OutputStream(latinBytesTo(target, padding), true, 1000, new byte[0]);
    } 

    /**
     * Create an OutputStream that on writing bytes, encodes them to the StringBuilder
     */
    public static OutputStream encode(StringBuilder target) {
        return encode(target, true);
    }

    /**
     * Create an OutputStream that on writing bytes, encodes them to the StringBuilder, and you can disable the optional `=` padding characters
     */
    public static OutputStream encode(StringBuilder target, boolean padding) {
        return new Base64OutputStream(latinBytesTo(target, padding), true, 1000, new byte[0]);
    }

    /**
     * Utility function that encodes a stream of bytes directly to a target
     */
    public static void encode(InputStream source, StringBuilder target, boolean padding) throws IOException {
        copy(source, (buffer, len) -> {
            for (int i = 0; i < len; i++) {
                char c = (char)buffer[i];
                // the Base64 class of apache does not support disabling the padding
                // so instead we have to filter it out for now
                if (!padding && c == '=') {
                    continue;
                }
                // fast path as this appends to internal array
                target.append(c);
            }
        });
    }
        
    /**
     * Utility function that encodes a stream of bytes directly to a target
     */
    public static void encode(InputStream source, Writer target, boolean padding) throws IOException {
        copy(source, (buffer, len) -> {
            var copy = new char[len];
            var written = 0;
            for (int i = 0; i < len; i++) {
                char c = (char)buffer[i];
                // the Base64 class of apache commons does not support disabling the padding
                // so instead we have to filter it out for now
                if (!padding && c == '=') {
                    continue;
                }
                copy[written++] = c;
            }
            target.write(copy, 0, written);
        });
    }

    @FunctionalInterface
    private static interface CharWriter {
        void write(byte[] c, int len) throws IOException;
    }

    private static void copy(InputStream source, CharWriter target) throws IOException {
        try (var encoder = new Base64InputStream(source, true, 1000, new byte[0])) {
            var buffer = new byte[4 * 1024]; // 3 source bytes generate 4 characters (encoded as bytes), so this aligns to buffer sizes
            int read;
            while ((read = encoder.read(buffer)) != -1) {
                target.write(buffer, read);
            }
        }
    }
}
