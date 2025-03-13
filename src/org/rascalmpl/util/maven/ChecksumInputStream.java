package org.rascalmpl.util.maven;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChecksumInputStream extends InputStream {
    private final InputStream wrappedStream;
    private final MessageDigest sha1Digest;
    private final MessageDigest md5Digest;

    public ChecksumInputStream(InputStream inputStream)  {
        this.wrappedStream = inputStream;
        try {
            this.sha1Digest = MessageDigest.getInstance("SHA1");
            this.md5Digest = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int read() throws IOException {
        int byteRead = wrappedStream.read();
        if (byteRead != -1) {
            sha1Digest.update((byte) byteRead);
            md5Digest.update((byte) byteRead);
        }
        return byteRead;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int bytesRead = wrappedStream.read(b, off, len);
        if (bytesRead != -1) {
            sha1Digest.update(b, off, bytesRead);
            md5Digest.update(b, off, bytesRead);
        }
        return bytesRead;
    }

    private String getChecksum(MessageDigest digest) {
        StringBuilder sb = new StringBuilder();
        for (byte b : digest.digest()) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public String getSha1Checksum() {
        return getChecksum(sha1Digest);
    }

    public String getMd5Checksum() {
        return getChecksum(md5Digest);
    }

    @Override
    public void close() throws IOException {
        wrappedStream.close();
        sha1Digest.reset();
        md5Digest.reset();
    }
}