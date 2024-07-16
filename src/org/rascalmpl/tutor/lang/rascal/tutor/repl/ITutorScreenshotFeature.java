package org.rascalmpl.tutor.lang.rascal.tutor.repl;

import java.io.IOException;

/**
 * A interface to be implemented by a depending project. The 
 * screenshot feature is injected into the tutor command executor 
 * by dynamic loading of the class mentioned in this resource:
 * org/rascalmpl/tutor/screenshotter.config
 * 
 * The goal is to not have dependencies on large projects
 * like selenium and Chrome in the core of the rascal project.
 * 
 * The tutor will work fine without a screenshotter, except that
 * screenshots will not be included with the documentation. It is
 * advisable to run the tutor using the rascal-maven-plugin, which
 * makes sure that the screenshot feature is properly injected.
 */
public interface ITutorScreenshotFeature {

    /**
     * The URL string is expected to be syntactically correct.
     * Typically it is localhost:<port>, to point at the right
     * server that is currently visualizizing something from the
     * REPL or the IDEServices.
     * 
     * @param  url localhost url
     * @return a base64 encoded PNG snapshot.
     * @throws IOExceptions when unexpected things happen
     */
    String takeScreenshotAsBase64PNG(String url) throws IOException;

}
