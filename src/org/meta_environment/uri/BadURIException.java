package org.meta_environment.uri;

public class BadURIException extends RuntimeException{
	private static final long serialVersionUID = 2091038871044656434L;

	public BadURIException(Exception ex){
		super(ex);
	}
}
