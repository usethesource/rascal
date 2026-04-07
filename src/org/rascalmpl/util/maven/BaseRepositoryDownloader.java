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
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import org.apache.maven.artifact.repository.metadata.Metadata;
import org.apache.maven.artifact.repository.metadata.io.xpp3.MetadataXpp3Reader;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

/**
 * Support class that can be extended to implement the RepositoryDownloader interface.
 * This class offers some utility functions and an implementation of `downloadMetadata`
 * that uses `downloadAndRead` (to be implemented by subclasses) to download and cache
 * metadata.
 * For each metadata file downloaded, an "<file>-info.properties" file is used to
 * keep track of when the last download has taken place. If this is less than 12
 * hours ago, the cached version is used. Otherwise the file is downloaded again.
 */
abstract public class BaseRepositoryDownloader implements RepositoryDownloader {
    private static final String PROP_LAST_UPDATED = "lastUpdated";
    private static final long MAX_INFO_AGE = 1000 * 60 * 60 * 12; // 12 hours
    private final Repo repo;

    public BaseRepositoryDownloader(Repo repo) {
        this.repo = repo;
    }

    @Override
    public Repo getRepo() {
        return repo;
    }

    protected URI createUri(String url, String suffix) throws URISyntaxException {
        if (url.endsWith("/") && suffix.startsWith("/")) {
            suffix = suffix.substring(1);
        }
        return new URI(url + suffix);
    }

    protected void ensureTargetDirectoryExists(Path target) throws IOException {
        // Ensure the directory exists, if not create it
        Path directory = target.getParent();
        if(Files.notExists(directory))  {
            Files.createDirectories(directory);
        }
    }

    @Override
    public @Nullable Metadata downloadMetadata(String url, Path target) {
        String source = null;

        String fileName = target.getFileName().toString();
        int dotIndex = fileName.lastIndexOf('.');
        String infoFilename = fileName.substring(0, dotIndex) + "-info.properties";
        Path infoPath = target.resolveSibling(infoFilename);
        if (Files.exists(infoPath)) {
            Properties info = new Properties();
            try {
                info.load(Files.newBufferedReader(infoPath));
                long lastUpdated = Long.parseLong(info.getProperty(PROP_LAST_UPDATED, "0"));
                if (System.currentTimeMillis() - lastUpdated < MAX_INFO_AGE) {
                    // If the info file is recent enough, read metadata from local repo
                    source = Files.readString(target);
                }
            }
            catch (IOException e) {
                // File info is of no use, continue with download
            }
        }

        if (source == null) {
            // No recent cached data, download the latest version
            source = downloadAndRead(url, target, true);
            if (source == null) {
                return null;
            }

            // Update the info file with the current timestamp
            Properties info = new Properties();
            info.setProperty(PROP_LAST_UPDATED, String.valueOf(System.currentTimeMillis()));
            try {
                info.store(Files.newBufferedWriter(infoPath), "Metadata info for " + fileName);
            }
            catch (IOException e) {
                // Too bad, we cannot store the info file so caching will be disabled.
            }
        }

        // Parse the metadata from the downloaded source
        try {
            MetadataXpp3Reader reader = new MetadataXpp3Reader();
            return reader.read(new StringReader(source));
        }
        catch (IOException|XmlPullParserException e) {
            return null;
        }
    }


}
