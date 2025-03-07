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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.apache.maven.model.Repository;

/*package*/ class SimpleRepositoryDownloader {
    // TODO: what to do about non http(s) respositories?
    
    public final Repository repo;
    private final HttpClient client;

    public SimpleRepositoryDownloader(Repository repo, HttpClient client) {
        this.repo = repo;
        this.client = client;
    }

    public boolean download(String url, Path target, boolean force) {
        try {
            var tempFile = Files.createTempFile("maven-download", null);
            try {
                var uri = createUri(repo.getUrl(), url);
                var req = HttpRequest.newBuilder(uri).GET().build();
                var result = client.send(req, BodyHandlers.ofFile(tempFile));
                if (result.statusCode() == 200) {
                    copyFileToTarget(target, force, tempFile);
                    return true;
                }
                return false;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            } finally {
                cleanup(tempFile);
            }
        } catch (URISyntaxException | IOException e) {
            return false;
        }
    }

    private URI createUri(String url, String suffix) throws URISyntaxException {
        if (url.endsWith("/") && suffix.startsWith("/")) {
            suffix = suffix.substring(1);
        }
        return new URI(url + suffix);
    }

    private void copyFileToTarget(Path target, boolean force, Path tempFile) throws IOException {
        var directory = target.getParent();
        if (Files.notExists(directory)) {
            Files.createDirectories(directory);
        }
        if (force || Files.notExists(target)) {
            Files.move(tempFile, target, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private void cleanup(Path tempFile) {
        try {
            Files.delete(tempFile);
        } catch (IOException ignored) {}
    }

}
