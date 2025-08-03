package org.rascalmpl.util.maven;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
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
