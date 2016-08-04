package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize.util;

public interface TrackLastWritten<T> {
    void write(T obj);
    int howLongAgo(T obj);
}
