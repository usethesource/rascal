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
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.maven.model.Repository;
import org.apache.maven.repository.legacy.ChecksumFailedException;

/*package*/ class SimpleRepositoryDownloader {
    // TODO: what to do about non http(s) respositories?

    public final Repository repo;
    private final HttpClient client;

    public SimpleRepositoryDownloader(Repository repo, HttpClient client) {
        this.repo = repo;
        this.client = client;
    }

    public boolean download(String url, Path target, boolean force) {
        // TODO also download md5/sha256 files to verify we got the right files.
        // see here on the information in headers instead of a separate file request
        // https://maven.apache.org/resolver/expected-checksums.html
        // calculate it during file writing (we have to override BodyHandlers)
        try {
            Path tempArtifact = Files.createTempFile("maven-download", null);
            try {
                // Try to download checksums first (we want to copy them even when verifying using headers)
                String sha1Checksum = downloadChecksum(createUri(repo.getUrl(), url + ".sha1"));
                String md5Checksum = downloadChecksum(createUri(repo.getUrl(), url + ".md5"));

                // Now download the actual artifact
                var artifactUri = createUri(repo.getUrl(), url);
                var req = HttpRequest.newBuilder(artifactUri).GET().build();
                HttpResponse<Path> response = client.send(req, BodyHandlers.ofFile(tempArtifact));

                if (response.statusCode() == 200) {
                    verifyResult(url, response, sha1Checksum, md5Checksum, tempArtifact);

                    // Only write checksums if the file has changed so the checksums will always match the current file
                    if (copyFileToTarget(target, force, tempArtifact)) {
                        writeChecksumToTarget(target.resolveSibling(target.getFileName() + ".sha1"), sha1Checksum);
                        writeChecksumToTarget(target.resolveSibling(target.getFileName() + ".md5"), md5Checksum);
                    }
                    return true;
                }
                return false;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            } finally {
                cleanup(tempArtifact);
            }
        } catch (URISyntaxException | IOException | ChecksumFailedException e) {
            return false;
        }
    }

    private String downloadChecksum(URI uri) throws IOException, InterruptedException {
        var req = HttpRequest.newBuilder(uri).GET().build();
        HttpResponse<String> result = client.send(req, BodyHandlers.ofString());
        return result.statusCode() == 200 ? result.body() : null;
    }

    private void verifyResult(String url, HttpResponse<Path> response, String sha1Checksum, String md5Checksum, Path artifactFile) throws IOException, ChecksumFailedException {
        ChecksumVerifier verifier = new ChecksumVerifier();
        HttpHeaders headers = response.headers();
        String sha1 = getChecksum(headers, Arrays.asList("x-checksum-sha1", "x-goog-meta-checksum-sha1"), sha1Checksum);
        if (sha1 != null) {
            verifier.verifyChecksum(artifactFile, "SHA1", sha1);
        }

        String md5 = getChecksum(headers, Arrays.asList("x-checksum-md5", "x-goog-meta-checksum-md5"), md5Checksum);
        if (md5 != null) {
            verifier.verifyChecksum(artifactFile, "MD5", md5);
        }
    }

    // Retrieve the checksum from one of the possible checksum headers. If not found, return defaultChecksum
    private String getChecksum(HttpHeaders headers, List<String> checksumHeaders, String defaultChecksum) {
        for (String headerName : checksumHeaders) {
            Optional<String> value = headers.firstValue(headerName);
            if (value.isPresent()) {
                return value.get();
            }
        }

        return defaultChecksum;
    }

    private URI createUri(String url, String suffix) throws URISyntaxException {
        if (url.endsWith("/") && suffix.startsWith("/")) {
            suffix = suffix.substring(1);
        }
        return new URI(url + suffix);
    }

    private boolean copyFileToTarget(Path target, boolean force, Path tempFile) throws IOException {
        var directory = target.getParent();
        if (Files.notExists(directory)) {
            Files.createDirectories(directory);
        }
        if (force || Files.notExists(target)) {
            Files.move(tempFile, target, StandardCopyOption.REPLACE_EXISTING);
            return true;
        }

        return false;
    }

    private void writeChecksumToTarget(Path path, String checksum) throws IOException {
        if (checksum == null) {
            Files.delete(path);
        } else {
            Files.write(path, checksum.getBytes(), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
        }
    }

    private void cleanup(Path tempFile) {
        try {
            Files.delete(tempFile);
        } catch (IOException ignored) {}
    }

}
