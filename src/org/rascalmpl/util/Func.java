package org.rascalmpl.util;

public interface Func<T, R, E extends Exception> {
    R apply(T param) throws E;
}
