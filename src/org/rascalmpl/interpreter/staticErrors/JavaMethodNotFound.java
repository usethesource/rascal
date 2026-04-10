package org.rascalmpl.interpreter.staticErrors;

import io.usethesource.vallang.ISourceLocation;

public class JavaMethodNotFound extends StaticError {
    private static final long serialVersionUID = 6760221123820619061L;

    public JavaMethodNotFound(String message, ISourceLocation loc, Throwable cause) {
        super(message, loc, cause);
    }
    
}
