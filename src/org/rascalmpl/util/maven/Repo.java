package org.rascalmpl.util.maven;

import org.apache.maven.model.Repository;

public interface Repo {
    Repository getRepository();

    String getId();

    String getUrl();
    
    String getLayout();
}
