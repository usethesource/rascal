package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize.util;

public interface TrackLastRead<T> {
    void read(T obj);
    T lookBack(int elements);
}
