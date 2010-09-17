package org.rascalmpl.library.experiments.scm;

@SuppressWarnings("serial")
public class ScmProviderException extends Exception {

	public ScmProviderException() {
		super();
	}
	
    public ScmProviderException(String message) {
        super(message);
    }
    
    public ScmProviderException(String message, Throwable cause) {
        super(message, cause);
    }
 
}
