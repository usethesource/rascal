package org.rascalmpl.util.maven;

import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;

public class ModelResolutionError extends Exception {
    private static final long serialVersionUID = -6899508684217780478L;
    private final transient IList messages;

    public ModelResolutionError(IListWriter messages) {
        super("Could not build a maven model, please inspect the messages field");
        this.messages = messages.done();
    }

    public IList getMessages() {
        return messages;
    }
}
