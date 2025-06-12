/*
 * Copyright (c) 2025, Swat.engineering
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.rascalmpl.util.maven;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class ChecksumInputStream extends InputStream {
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