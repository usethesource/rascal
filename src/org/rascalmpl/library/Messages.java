package org.rascalmpl.library;

import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;

/**
 * Java API for the messages in the standard library module `Message`
 * 
 * This is the standard format for all error messages in Rascal projects and beyond.
 * Since some low-level core code also produces messages that should end up in UI,
 * we write here a bridge between the Java and Rascal representation. 
 * 
 * TODO Later when the standard library is bootstrapped, this code might be replaced
 * by the generated code from the compiler for the `Message` module.
 */
public class Messages {

    public static IValue warning(String message, ISourceLocation loc) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'warning'");
    }

    public static IValue error(String string, ISourceLocation loc) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'error'");
    }
    
}
