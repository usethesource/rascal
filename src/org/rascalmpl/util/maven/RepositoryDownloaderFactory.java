package org.rascalmpl.util.maven;

import java.net.http.HttpClient;

public class RepositoryDownloaderFactory {
    private HttpClient client;
    
    public RepositoryDownloaderFactory(HttpClient client) {
        this.client = client;
    }

    public RepositoryDownloader createDownloader(Repo repo) {
        if (repo.getUrl().startsWith("file:")) {
            return new FileRepositoryDownloader(repo);
        }
        return new HttpRepositoryDownloader(repo, client);
    }
}
