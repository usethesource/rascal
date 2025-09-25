package org.rascalmpl.util.maven;

import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

public class XmlReaderTest {
    private String input = "<?xml :<?xml version=\"1.0\" encoding=\"%ENCODING%\"?>\nHello World!";

    private void testReader(String input, Charset charset) throws IOException {
        byte[] encoded = input.getBytes(charset);
        System.out.print(charset.name() + ":");
        for (int i = 0; i < 8; i++) {
            System.out.print(String.format(" %2X", encoded[i] & 0xFF));
        }
        System.out.println();
        Reader reader = XmlReader.createXmlReader(new ByteArrayInputStream(encoded));
        String result = IOUtils.toString(reader);

        Assert.assertEquals(input, result);
    }

    private void testReader(Charset charset) throws IOException {
        testReader(input.replace("%ENCODING%", charset.name()), charset);
    }

    @Test
    public void testUtf8() throws IOException {
        testReader(StandardCharsets.UTF_8);
    }

    @Test
    public void testUtf16() throws IOException {
        testReader(StandardCharsets.UTF_16);
    }

    @Test
    public void testUtf16BE() throws IOException {
        testReader(StandardCharsets.UTF_16BE);
    }

    @Test
    public void testUtf16LE() throws IOException {
        testReader(StandardCharsets.UTF_16LE);
    }

    @Test
    public void testUtf32BE() throws IOException {
        testReader(Charset.forName("UTF-32BE"));
    }

    @Test
    public void testUtf32LE() throws IOException {
        testReader(Charset.forName("UTF-32LE"));
    }

    @Test
    public void testEncodingMismatch() throws IOException {
        assertThrows(IOException.class, () -> {
            testReader(input.replace("%ENCODING%", "UTF-16"), StandardCharsets.UTF_8);
        });
    }
}
