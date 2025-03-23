package org.rascalmpl.util.maven;

import org.apache.maven.model.Repository;

public class Repo {
    private final Repository repository;

    public Repo(Repository repository) {
        this.repository = repository;
    }

    public Repository getMavenRepository() {
        return repository;
    }

    public String getId() {
        return repository.getId();
    }

    public String getLayout() {
        return repository.getLayout();
    }

    public String getUrl() {
        return repository.getUrl();
    }
}
