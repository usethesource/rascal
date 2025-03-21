package org.rascalmpl.util.maven;

import org.apache.maven.model.Repository;

public class RepositoryRepo implements Repo {
    private Repository repository;

    public RepositoryRepo(Repository repository) {
        this.repository = repository;
    }

    public Repository getRepository() {
        return repository;
    }

    @Override
    public String getId() {
        return repository.getId();
    }

    @Override
    public String getLayout() {
        return repository.getLayout();
    }

    @Override
    public String getUrl() {
        return repository.getUrl();
    }
}
