package org.rascalmpl.util;

import static org.junit.Assert.assertArrayEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Base64;
import java.util.Random;

import org.junit.Test;
import org.rascalmpl.util.base64.StreamingBase64;

public class StreamingBase64Tests {
    
    private Random r = new Random();


    @FunctionalInterface
    private interface DecoderFunction {
        byte[] decode(String s) throws IOException;
    }

    private void decodingRoundTrip(DecoderFunction decoder) throws IOException {
        for (int t = 0; t < 100; t++) {
            var input = new byte[1 + r.nextInt(16 * 1024)];
            r.nextBytes(input);
            var inputEncoded = Base64.getEncoder().encodeToString(input);
            var output = decoder.decode(inputEncoded);
            assertArrayEquals(input, output);
        }
    }
    
    @Test
    public void roundTripBase64DecodeString() throws IOException {
        decodingRoundTrip(s -> StreamingBase64.decode(s).readAllBytes());
    }

    @Test
    public void roundTripBase64DecodeReader() throws IOException {
        decodingRoundTrip(s -> {
            try (var reader = new StringReader(s)) {
                return StreamingBase64.decode(reader).readAllBytes();
            }
        });
    }

    @Test
    public void roundTripBase64DecodeReaderFull() throws IOException {
        decodingRoundTrip(s -> {
            try (var reader = new StringReader(s); var result = new ByteArrayOutputStream()) {
                StreamingBase64.decode(reader, result);
                return result.toByteArray();
            }
        });
    }

    @FunctionalInterface
    private interface EncoderFunction {
        String encode(byte[] data) throws IOException;
    }

    private void encodingRoundTrip(EncoderFunction encoder) throws IOException {
        for (int t = 0; t < 100; t++) {
            var input = new byte[1 + r.nextInt(16 * 1024)];
            r.nextBytes(input);
            var encoded = encoder.encode(input);
            var output = Base64.getDecoder().decode(encoded);
            assertArrayEquals(input, output);
        }
    }

    @Test
    public void roundTripBase64EncodeWriter() throws IOException {
        encodingRoundTrip((input) -> {
            var writer = new StringWriter(); 
            try (var target = StreamingBase64.encode(writer)) {
                target.write(input);
            }
            return writer.getBuffer().toString();
        });
    }

    @Test
    public void roundTripBase64EncodeStringBuilder() throws IOException {
        encodingRoundTrip((input) -> {
            var writer = new StringBuilder(); 
            try (var target = StreamingBase64.encode(writer)) {
                target.write(input);
            }
            return writer.toString();
        });
    }

    @Test
    public void roundTripBase64FullCopy() throws IOException {
        encodingRoundTrip(input -> {
            var writer = new StringBuilder();
            StreamingBase64.encode(new ByteArrayInputStream(input), writer, true);
            return writer.toString();
        });
    }

    @Test
    public void roundTripBase64FullCopySlowReader() throws IOException {
        encodingRoundTrip(input -> {
            var writer = new StringBuilder();
            StreamingBase64.encode(new InputStream() {
                int pos = 0;
                @Override
                public int read() throws IOException {
                    if (pos == input.length) {
                        return -1;
                    }
                    return input[pos++] & 0xFF;
                }

                @Override
                public int read(byte[]b, int off, int len) throws IOException {
                    if (pos == input.length) {
                        return -1;
                    }
                    b[off] = input[pos++];
                    return 1;
                }
            }, writer, true);
            return writer.toString();
        });
    }


}
