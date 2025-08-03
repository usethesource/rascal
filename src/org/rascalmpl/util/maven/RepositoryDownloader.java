package org.rascalmpl.util.maven;

import java.nio.file.Path;

import org.apache.maven.artifact.repository.metadata.Metadata;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface RepositoryDownloader {
    public Repo getRepo();

    public boolean download(String url, Path target, boolean force);

    public @Nullable String downloadAndRead(String url, Path target, boolean force);

    public @Nullable Metadata downloadMetadata(String url, Path target);
}
