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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.function.FailableFunction;
import org.apache.maven.model.Repository;

/**
 * A note about locking:
 * Different maven versions use different methods of locking files.
 * - maven 3.8 uses a lock file <artifact>.part.lock
 * - maven 3.9 uses a lock file <artifact>.lastUpdated
 * - maven 4.0 uses a file in a special .locks directory and locks all jars and metadata in a predefined order to avoid deadlocks.
 * We cannot support them all so for the time being we rely on first creating a temporary file
 * and then moving the file to the final location.
 */

/*package*/ class SimpleRepositoryDownloader {
    // TODO: what to do about non http(s) respositories?

    public final Repository repo;
    private final HttpClient client;
    private final Random rand;

    public SimpleRepositoryDownloader(Repository repo, HttpClient client) {
        this.repo = repo;
        this.client = client;
        rand = new Random();
    }

    public boolean download(String url, Path target, boolean force) {
        Path directory = target.getParent();
        if (Files.notExists(directory)) {
            try {
                Files.createDirectories(directory);
            }
            catch (IOException e) {
                return false;
            }
        }

        Optional<Path> result = download(url, target, force,
        (InputStream input) -> { 
            Path tempTarget = getTempFile(target);
            Files.copy(input, tempTarget);
            return tempTarget;
        },
        (Path tempArtifact) -> {
            return moveToTarget(tempArtifact, target, force);
        });
        return result.isPresent();
    }

    private Path getTempFile(Path target) {
        String tempFileName = target.getFileName().toString() + String.valueOf(rand.nextInt(Integer.MAX_VALUE)) + ".tmp";
        return target.resolveSibling(tempFileName);
    }

    public String downloadAndRead(String url, Path target, boolean force) {
        Optional<String> result = download(url, target, force,
        (InputStream input) -> {
            return IOUtils.toString(input, StandardCharsets.UTF_8.name());
        },
        (String content) -> {
            return writeToTarget(content, target, force);
        });
        return result.orElse(null);
    }

    public <R> Optional<R> download(String url, Path target, boolean force, 
        FailableFunction<InputStream, R, IOException> resultCreator,
        FailableFunction<R, Boolean, IOException> resultWriter) {
        try {
            try {
                var artifactUri = createUri(repo.getUrl(), url);
                var req = HttpRequest.newBuilder(artifactUri).GET().build();
                HttpResponse<InputStream> response = client.send(req, BodyHandlers.ofInputStream());

                if (response.statusCode() == 200) {
                    ChecksumInputStream input = new ChecksumInputStream(response.body());
                    R result = resultCreator.apply(input);

                    String sha1Checksum = input.getSha1Checksum();
                    String md5Checksum = input.getMd5Checksum();

                    if (!verifyChecksum(url, response.headers(), sha1Checksum, md5Checksum)) {
                        return Optional.empty();
                    }

                    // Only write checksums if copying succeeds so the checksums will always match the current file
                    if (resultWriter.apply(result)) {
                        writeChecksumToTarget(target.resolveSibling(target.getFileName() + ".sha1"), sha1Checksum);
                        writeChecksumToTarget(target.resolveSibling(target.getFileName() + ".md5"), md5Checksum);
                    }
                    return Optional.of(result);
                }
                return Optional.empty();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return Optional.empty();
            }
        } catch (URISyntaxException | IOException e) {
            return Optional.empty();
        }
    }

    private boolean verifyChecksum(String url, HttpHeaders headers, String actualSha1, String actualMd5) throws IOException, InterruptedException, URISyntaxException {
        // For checksum info see: https://maven.apache.org/resolver/expected-checksums.html
        String expectedSha1 = getChecksum(headers, Arrays.asList("x-checksum-sha1", "x-goog-meta-checksum-sha1"), url + ".sha1");
        if (expectedSha1 != null) {
            return expectedSha1.equals(actualSha1);
        }

        String expectedMd5 = getChecksum(headers, Arrays.asList("x-checksum-md5", "x-goog-meta-checksum-md5"), url + ".md5");
        if (expectedMd5 != null) {
            return expectedMd5.equals(actualMd5);
        }

        // No checksum to check against
        return true;
    }

    // Retrieve the checksum from one of the possible checksum headers. If not found, try to download it
    private String getChecksum(HttpHeaders headers, List<String> checksumHeaders, String checksumUrl)
        throws IOException, InterruptedException, URISyntaxException {
        for (String headerName : checksumHeaders) {
            Optional<String> value = headers.firstValue(headerName);
            if (value.isPresent()) {
                return value.get();
            }
        }

        // Maybe we should return null on an IOException? That would mean no checksum checking in that case.
        return downloadChecksum(createUri(repo.getUrl(), checksumUrl));
    }

    private String downloadChecksum(URI uri) throws IOException, InterruptedException {
        var req = HttpRequest.newBuilder(uri).GET().build();
        HttpResponse<String> result = client.send(req, BodyHandlers.ofString());
        return result.statusCode() == 200 ? result.body() : null;
    }

    private URI createUri(String url, String suffix) throws URISyntaxException {
        if (url.endsWith("/") && suffix.startsWith("/")) {
            suffix = suffix.substring(1);
        }
        return new URI(url + suffix);
    }

    private boolean moveToTarget(Path from, Path to, boolean force) throws IOException {
        try {
            if (force) {
                Files.move(from, to, StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.move(from, to);
            }
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                // Delete temperory file if something went wrong
                Files.deleteIfExists(from);
            } catch (IOException ignored) {
                // Beste effort
            }
        }
    }

    private boolean writeToTarget(String content, Path target, boolean force) throws IOException {
        Path tempTarget = getTempFile(target);
        try {
            Files.writeString(tempTarget, content);
        } catch (IOException e) {
            return false;
        }
        return moveToTarget(tempTarget, target, force);
    }

    private void writeChecksumToTarget(Path path, String checksum) throws IOException {
        if (checksum == null) {
            Files.delete(path);
        } else {
            Files.write(path, checksum.getBytes(StandardCharsets.UTF_8), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
        }
    }

}
