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
                System.err.println("Exception trying to read info file " + infoPath + ": " + e.getMessage());
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
                System.err.println("Exception trying to write metadata info file " + infoPath + ": " + e.getMessage());
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
