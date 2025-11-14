package org.rascalmpl.util;

import static org.junit.Assert.assertArrayEquals;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Base64;
import java.util.Random;

import org.junit.Test;

public class StreamingBase64Tests {
    
    private Random r = new Random();


    @FunctionalInterface
    private interface DecoderFunction {
        byte[] decode(String s) throws IOException;
    }

    private void decodingRoundTrip(DecoderFunction decoder) throws IOException {
        for (int t = 0; t < 100; t++) {
            var input = new byte[1 + r.nextInt(8 * 1024)];
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

    @FunctionalInterface
    private interface EncoderFunction {
        String encode(byte[] data) throws IOException;
    }

    private void encodingRoundTrip(EncoderFunction encoder) throws IOException {
        for (int t = 0; t < 100; t++) {
            var input = new byte[1 + r.nextInt(8 * 1024)];
            r.nextBytes(input);
            var encoded = encoder.encode(input);
            System.out.println(encoded);
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


}
