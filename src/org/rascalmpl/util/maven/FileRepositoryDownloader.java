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
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.checkerframework.checker.nullness.qual.Nullable;

/* This class is used for file:// repos and is currently only used in tests. This means
 * no support for checksums, etc. is implemented.
 */
public class FileRepositoryDownloader extends BaseRepositoryDownloader {
    public FileRepositoryDownloader(Repo repo) {
        super(repo);
    }

    private InputStream getInputStream(String path) throws IOException {
        try {
            return createUri(getRepo().getUrl(), path).toURL().openStream();
        }
        catch (IOException | URISyntaxException e) {
            throw new IOException("Could not open stream for " + path + " in repo " + getRepo().getUrl(), e);
        }
    }

    @Override
    public boolean download(String path, Path target, boolean force) {
        try {
            ensureTargetDirectoryExists(target);
            InputStream inputStream = getInputStream(path);
            if (force || !Files.exists(target)) {
                Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
            }
            return true;
        }
        catch (IOException e) {
            return false; // Could not open stream or write to target
        }
    }

    @Override
    public @Nullable String downloadAndRead(String path, Path target, boolean force) {
        try {
            ensureTargetDirectoryExists(target);
            InputStream inputStream = getInputStream(path);
            byte[] bytes = inputStream.readAllBytes();
            if (force || !Files.exists(target)) {
                Files.write(target, bytes);
            }
            return new String(bytes, StandardCharsets.UTF_8);
        }
        catch (IOException e) {
            return null; // Could not open stream or write to target
        }
    }

}
