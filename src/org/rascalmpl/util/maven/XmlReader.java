package org.rascalmpl.util.maven;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.rascalmpl.uri.URIResolverRegistry;

import io.usethesource.vallang.ISourceLocation;

public class XmlReader {
    private static final Charset DEFAULT_XML_CHARSET = StandardCharsets.UTF_8;

    /*
     * Try to parse the BOM (Byte Order Mark) or "<?xml ...>" opening line of an xml document to determine the charset used.
     * Fall back to UTF-8 if the charset cannot be determined.
     */
    public static Reader createXmlReader(InputStream input) throws IOException {
        final int BUFFER_SIZE = 4096;

        BufferedInputStream bufferedInput = new BufferedInputStream(input);
        bufferedInput.mark(BUFFER_SIZE); // mark so we can reset

        // Read the first part of the file so we can detect the encoding
        byte[] buffer = new byte[BUFFER_SIZE];
        int len = bufferedInput.read(buffer);


        int skip = 0;
        Charset detectedCharset = DEFAULT_XML_CHARSET;
        if (len < 4) {
            // File is not long enough to be a valid xml document
        } else {
            /*
            The first bytes of different encodings with BOM (Byte Order Mark):
            UTF-8    -> EF BB BF 
            UTF-16BE -> FE FF
            UTF-16LE -> FF FE
            UTF-32BE -> 00 00 FE FF
            UTF-32LE -> FF FE 00 00
            
            The first 4 bytes of different encodings without BOM
            "<?xm" in UTF-8 -> 3C 3F 78 6D
            "<?" in UTF-16BE -> 00 3C 00 3F
            "<?" in UTF-16LE -> 3C 00 3F 00
            "<?" in UTF-32BE -> 00 00 00 3C
            "<?" in UTF-32LE -> 3C 00 00 00
            */

            int b0 = buffer[0] & 0xFF, b1 = buffer[1] & 0xFF, b2 = buffer[2] & 0xFF, b3 = buffer[3] & 0xFF;
            if ((b0 == 0xEF && b1 == 0xBB && b2 == 0xBF) || (b0 == 0x3C && b1 == 0x3F && b2 == 0x78 && b3 == 0x6D)) {
                detectedCharset = StandardCharsets.UTF_8;
                skip = b0 == 0xEF ? 3 : 0;
            }
            else if ((b0 == 0xFE && b1 == 0xFF) || (b0 == 0x00 && b1 == 0x3C && b2 == 0x00 && b3 == 0x3F)) {
                detectedCharset = StandardCharsets.UTF_16BE;
                skip = b0 == 0xFE ? 2 : 0;
            }
            else if ((b0 == 0xFF && b1 == 0xFE) || (b0 == 0x3C && b1 == 0x00 && b2 == 0x3F && b3 == 0x00)) {
                detectedCharset = StandardCharsets.UTF_16LE;
                skip = b0 == 0xFF ? 2 : 0;
            }
            else if ((b0 == 0x00 && b1 == 0x00 && b2 == 0xFE && b3 == 0xFF)
                || (b0 == 0x00 && b1 == 0x00 && b2 == 0x00 && b3 == 0x3C)) {
                detectedCharset = Charset.forName("UTF-32BE");
                skip = b3 == 0xFF ? 4 : 0;
            }
            else if ((b0 == 0xFF && b1 == 0xFE && b2 == 0x00 && b3 == 0x00)
                || (b0 == 0x3C && b1 == 0x00 && b2 == 0x00 && b3 == 0x00)) {
                detectedCharset = Charset.forName("UTF-32LE");
                skip = b0 == 0xFF ? 4 : 0;
            }
            else {
                // Probably not a <?xml so assume UTF-8
                detectedCharset = DEFAULT_XML_CHARSET;
            }
        }

        // Decode the header
        var header = new String(buffer, 0, len, detectedCharset);

        // Now see if we can detect the xml encoding to check that it matches the detected encoding
        Pattern p = Pattern.compile("<\\?xml.*encoding=[\"'](.+?)[\"'].*\\?>");
        Matcher m = p.matcher(header);
        if (m.find()) {
            Charset declaredCharset = Charset.forName(m.group(1));
            if (declaredCharset != detectedCharset) {
                if (declaredCharset != StandardCharsets.UTF_16 ||  (detectedCharset != StandardCharsets.UTF_16BE && detectedCharset != StandardCharsets.UTF_16LE)) {
                    throw new IOException("Malformed xml document, actual encoding does not match the declared encoding: " + detectedCharset + " != " + declaredCharset);
                }
            }
        }

        // Reset and return a Reader with the right encoding
        bufferedInput.reset();
        bufferedInput.skip(skip);
        return new InputStreamReader(bufferedInput, detectedCharset);
    }

    public static Reader createXmlReader(ISourceLocation loc) throws IOException {
        return createXmlReader(URIResolverRegistry.getInstance().getInputStream(loc));
    }
}
