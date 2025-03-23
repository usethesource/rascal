package org.rascalmpl.util.maven;

import org.apache.maven.model.Repository;
import org.apache.maven.settings.Mirror;

public class MirrorRepo extends Repo {
    private Mirror mirror;

    public MirrorRepo(Mirror mirror, Repository repository) {
        super(repository);
        this.mirror = mirror;
    }

    @Override
    public String getId() {
        return mirror.getId();
    }

    @Override
    public String getUrl() {
        return mirror.getUrl();
    }
}
